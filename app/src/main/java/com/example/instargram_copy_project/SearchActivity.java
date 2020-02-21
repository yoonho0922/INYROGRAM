package com.example.instargram_copy_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    List items;

    Button toggleButton1;
    Button toggleButton3;
    Button toggleButton4;
    Button toggleButton5;

    ListView listView;

    int[] profile_img = new int[]{R.drawable.profile1, R.drawable.profile2, R.drawable.profile3};

    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        navbar();

        getDB();

    }

    public void getDB(){
        items = new ArrayList<Object>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = new HashMap<String, Object>();

                                String name = document.getString("name");
                                String userName = document.getString("userName");

                                map.put("name", name);
                                map.put("userName", userName);
                                items.add(map);
                            }
                        } else {
                            //에러시
                        }
                        goMain(items);
                    }
                });//db.collection END
    }// getDB END

    public void goMain(List items){
        startToast(items.toString());

//        int i = 0;
//        while (i < profile_img.length) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("profile_img", profile_img[i]);
//
//            list.add(map);
//            i++;
//        }

        // key와 view의 아이디를 매핑
        String[] keys = new String[]{"name", "userName"};
        int[] ids = new int[]{R.id.idTextView,R.id.nameTextView};

        listView = findViewById(R.id.listView);

        SimpleAdapter customAdapter = new SimpleAdapter(this, items, R.layout.search_custom_view, keys, ids);
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

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
