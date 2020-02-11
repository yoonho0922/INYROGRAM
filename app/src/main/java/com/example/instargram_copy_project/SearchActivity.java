package com.example.instargram_copy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    ListView listView;



    String[] user_id = new String[]{"instar_id1", "instar_id2", "instar_id3"};
    String[] user_name = new String[]{"안윤호1", "안윤호2", "안윤호3"};
    int[] user_img = new int[]{R.drawable.dog1, R.drawable.dog2, R.drawable.dog3};

    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


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
}
