package com.example.instargram_copy_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrivatePostActivity extends AppCompatActivity {

    private static final String TAG = "MemberInfoSetting";
    ArrayList items;

    Button toggleButton2;
    Button toggleButton3;
    Button toggleButton4;
    ListView listView;
    Button toggleButton5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final String docId = getIntent().getExtras().getString("docId");
        final String userId = getIntent().getExtras().getString("Friend");
        getDB(userId, docId);

    }

    public void navbar() {
        toggleButton2 = findViewById(R.id.toggleButton2);
        toggleButton3 = findViewById(R.id.toggleButton3);
        toggleButton4 = findViewById(R.id.toggleButton4);
        toggleButton5 = findViewById(R.id.toggleButton5);


        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivatePostActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivatePostActivity.this, PostingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivatePostActivity.this, NotificationActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivatePostActivity.this, MyPageActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void getDB(String userId, String docId) {
        items = new ArrayList<Map>();
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //user의 정보를 사용할것임
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Post").document(userId).collection("privatePost")
                .document(docId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                HashMap<String, String> map = new HashMap<String, String>();
                String content = document.getString("content");
                String fileName = document.getString("fileName");
                String place = document.getString("place");
                String userUID = document.getString("userUid");

                map.put("content", content);
                map.put("fileName", fileName);
                map.put("place", place);
                map.put("userUID", userUID);
                items.add(map);
                listView = findViewById(R.id.listView);
                AdapterActivity customAdapter = new AdapterActivity(items);
                listView.setAdapter(customAdapter);
            }
        });

    }
}

