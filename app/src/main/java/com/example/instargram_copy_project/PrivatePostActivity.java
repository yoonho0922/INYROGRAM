package com.example.instargram_copy_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PrivatePostActivity extends AppCompatActivity {
    TextView userName_up;
    TextView placeTextView;
    ImageView picture_profile;
    ImageView picture_posting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_custom_view);
        final String friendUserId = getIntent().getExtras().getString("Friend");
    }
}
