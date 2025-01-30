package com.example.miniprojectwsa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditMessage extends AppCompatActivity {

    private EditText messageEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmessage);

        messageEditText = findViewById(R.id.editTextMessage);
        saveButton = findViewById(R.id.buttonSave);

        loadMessage();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessage();
            }
        });
    }

    private void loadMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MessagePrefs", Context.MODE_PRIVATE);
        String savedMessage = sharedPreferences.getString("emergency_message", "");

        if (!TextUtils.isEmpty(savedMessage)) {
            messageEditText.setText(savedMessage);
        }
    }

    private void saveMessage() {
        String message = messageEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(message)) {
            SharedPreferences sharedPreferences = getSharedPreferences("MessagePrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("emergency_message", message);
            editor.apply();
            Toast.makeText(this, "Message saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }
}

