package com.example.instargram_copy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Search extends AppCompatActivity {

    ListView listView;

    TextView idTextView;
    TextView nameTextView;

    String[] user_id = new String[]{"instar_id1", "instar_id2", "instar_id3"};
    String[] user_name = new String[]{"인스타이름1", "인스타이름2", "인스타이름3"};



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

            list.add(map);
            i++;
        }

        String[] keys = new String[]{"id", "name"};
        int[] ids = new int[]{R.id.idTextView,R.id.nameTextView};

        idTextView = findViewById(R.id.idTextView);
        nameTextView = findViewById(R.id.nameTextView);

        SimpleAdapter customAdapter = new SimpleAdapter(this, list, R.layout.search_custom_view, keys, ids);
        listView.setAdapter(customAdapter);

    }
}
