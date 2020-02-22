package com.example.instargram_copy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserPageActivity extends AppCompatActivity {

    Button toggleButton1;
    Button toggleButton2;
    Button toggleButton3;
    Button toggleButton4;

    ImageView imageView;
    TextView user_name;
    TextView name_profile;
    TextView web_profile;
    TextView intro_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        navbar();

        // MainActivity에서 보낸 imgRes를 받기위해 getIntent()로 초기화
        Intent intent = getIntent();
        imageView = findViewById(R.id.imageView);
        user_name = findViewById(R.id.user_name);
        name_profile = findViewById(R.id.name_profile);
        web_profile = findViewById(R.id.web_profile);
        intro_profile = findViewById(R.id.intro_profile);

        // "imgRes" key로 받은 값은 int 형이기 때문에 getIntExtra(key, defaultValue);
        // 받는 값이 String 형이면 getStringExtra(key);
        imageView.setImageResource(intent.getIntExtra("imgRes", 0));
        user_name.setText(intent.getStringExtra("userName"));
        name_profile.setText(intent.getStringExtra("name"));
        web_profile.setText(intent.getStringExtra("website"));
        intro_profile.setText(intent.getStringExtra("intro"));
    }

    public void navbar(){
        toggleButton1 = findViewById(R.id.toggleButton1);
        toggleButton2 = findViewById(R.id.toggleButton2);
        toggleButton3 = findViewById(R.id.toggleButton3);
        toggleButton4 = findViewById(R.id.toggleButton4);


        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPageActivity.this , HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPageActivity.this , SearchActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPageActivity.this , PostingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPageActivity.this , NotificationActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
