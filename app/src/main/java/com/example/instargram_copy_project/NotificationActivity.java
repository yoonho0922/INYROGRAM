package com.example.instargram_copy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends AppCompatActivity {

    Button toggleButton5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toggleButton5 = findViewById(R.id.toggleButton5);

        toggleButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationActivity.this , MyPageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });

    }
}
