package com.example.instargram_copy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyPageActivity extends AppCompatActivity {

    Button toggleButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        toggleButton4 = findViewById(R.id.toggleButton4);

        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this , NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
    }
}
