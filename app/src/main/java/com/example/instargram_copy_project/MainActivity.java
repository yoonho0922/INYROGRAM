package com.example.instargram_copy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sunjung_main2);

        final ToggleButton toggle1 = findViewById(R.id.toggleButton1);
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle1.isChecked()) {
                    toggle1.setBackgroundDrawable(getResources().getDrawable(R.drawable.homebutton));
                }
                else{
                    toggle1.setBackgroundDrawable(getResources().getDrawable(R.drawable.homeonbutton));
                }
            }
        });

        final ToggleButton toggle2 = findViewById(R.id.toggleButton2);
        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle2.isChecked()) {
                    toggle2.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchbutton));
                }
                else{
                    toggle2.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchonbutton));
                }
            }
        });


        final ToggleButton toggle3 = findViewById(R.id.toggleButton3);
        toggle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle3.isChecked()) {
                    toggle3.setBackgroundDrawable(getResources().getDrawable(R.drawable.addbutton));
                }
                else{
                    toggle3.setBackgroundDrawable(getResources().getDrawable(R.drawable.addonbutton));
                }
            }
        });


        final ToggleButton toggle4 = findViewById(R.id.toggleButton4);
        toggle4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle4.isChecked()) {
                    toggle4.setBackgroundDrawable(getResources().getDrawable(R.drawable.heartbutton));
                }
                else{
                    toggle4.setBackgroundDrawable(getResources().getDrawable(R.drawable.heartonbutton));
                }
            }
        });


        final ToggleButton toggle5 = findViewById(R.id.toggleButton5);
        toggle5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle5.isChecked()) {
                    toggle5.setBackgroundDrawable(getResources().getDrawable(R.drawable.userbutton));
                }
                else{
                    toggle5.setBackgroundDrawable(getResources().getDrawable(R.drawable.useronbutton));
                }
            }
        });

    }
}
