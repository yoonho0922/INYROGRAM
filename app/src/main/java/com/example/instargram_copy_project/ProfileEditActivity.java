package com.example.instargram_copy_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity {
    EditText name;
    EditText userName;
    EditText website;
    EditText intro;
    Button finishBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);



        name = findViewById(R.id.name);
        finishBtn = findViewById(R.id.finishBtn);

        finishBtn = (Button)findViewById(R.id.finishBtn);
        name = (EditText)findViewById(R.id.name);

        finishBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyPageActivity.class);
                intent.putExtra(" ", name.getText().toString());
                startActivity(intent);

            }
        });
    }



}
