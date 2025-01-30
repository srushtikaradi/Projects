package com.example.miniprojectwsa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        ImageView viewContactButton = findViewById(R.id.eye);
        ImageView addContactButton = findViewById(R.id.add);
        ImageView deleteContactButton = findViewById(R.id.delete);
        ImageView safetyManualButton = findViewById(R.id.safety);
        ImageView instructionsButton = findViewById(R.id.instruction);
        ImageView locationButton = findViewById(R.id.locate);
        ImageButton editMessageButton = findViewById(R.id.imageButton);

        viewContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, ViewContact.class));
            }
        });

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, AddContact.class));
            }
        });

        deleteContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, DeleteContact.class));
            }
        });

        safetyManualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, SafetyManual.class));
            }
        });

        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, Instruction.class));
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, Shake.class));
            }
        });

        editMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, EditMessage.class));
            }
        });
    }

    public void onBackPressed(View view) {
        finish();
    }
}
