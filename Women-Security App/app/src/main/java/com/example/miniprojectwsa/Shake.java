package com.example.miniprojectwsa;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.HashSet;
import java.util.Set;

public class Shake extends AppCompatActivity implements SensorEventListener, LocationListener {

    private static final int SHAKE_THRESHOLD = 400;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_SLOP_TIME_MS = 300;

    private LocationManager locationManager;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void detectShake(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > SHAKE_SLOP_TIME_MS) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    return;
                }
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    sendDistressMessageWithLocation(lastKnownLocation);
                } else {
                    Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                }
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    private void sendDistressMessageWithLocation(Location location) {
        SharedPreferences messagePrefs = getSharedPreferences("MessagePrefs", Context.MODE_PRIVATE);
        String editedMessage = messagePrefs.getString("emergency_message", "");
        String defaultMessage = "Emergency! Please help!\n" +
                "https://www.google.com/maps/search/?api=1&query=" +
                location.getLatitude() + "," + location.getLongitude();

        String message = editedMessage.isEmpty() ? defaultMessage : editedMessage + "\n" +
                "https://www.google.com/maps/search/?api=1&query=" +
                location.getLatitude() + "," + location.getLongitude();

        SharedPreferences sharedPreferences = getSharedPreferences("ContactsPrefs", Context.MODE_PRIVATE);
        Set<String> contactsSet = sharedPreferences.getStringSet("contacts", new HashSet<String>());

        if (contactsSet.isEmpty()) {
            Toast.makeText(this, "No contacts to send distress message to.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String contact : contactsSet) {
            sendSMS(contact, message);
        }
        Toast.makeText(this, "Alert message sent with current location", Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Alert message sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send distress message to " + phoneNumber, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

