package com.example.instargram_copy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    Button toggleButton1;
    Button toggleButton3;
    Button toggleButton4;
    Button toggleButton5;

    ListView listView;

    String[] user_id = new String[]{"instar_id1", "instar_id2", "instar_id3"};
    String[] user_name = new String[]{"안윤호1", "안윤호2", "안윤호3"};
    int[] user_img = new int[]{R.drawable.dog1, R.drawable.dog2, R.drawable.dog3};

    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        navbar();

        int i = 0;
        while (i < user_id.length) {
            HashMap<String, Object> map = new HashMap<>();

            map.put("id", user_id[i]);
            map.put("name", user_name[i]);
            map.put("img", user_img[i]);

            list.add(map);
            i++;
        }

        // key와 view의 아이디를 매핑
        String[] keys = new String[]{"id", "name","img"};
        int[] ids = new int[]{R.id.idTextView,R.id.nameTextView,R.id.imageView};

        listView = findViewById(R.id.listView);

        SimpleAdapter customAdapter = new SimpleAdapter(this, list, R.layout.search_custom_view, keys, ids);
        listView.setAdapter(customAdapter);

    }

    public void navbar(){
        toggleButton1 = findViewById(R.id.toggleButton1);
        toggleButton3 = findViewById(R.id.toggleButton3);
        toggleButton4 = findViewById(R.id.toggleButton4);
        toggleButton5 = findViewById(R.id.toggleButton5);


        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this , HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this , PostingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this , NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        toggleButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this , MyPageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
    }
}
