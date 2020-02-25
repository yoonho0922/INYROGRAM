package com.example.instargram_copy_project;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import  com.google.firebase.firestore.FirebaseFirestore;import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowingListActivity extends AppCompatActivity {
    TextView user_name;
    ListView listView;
    //ArrayList<String> items;
    ArrayAdapter<String> adapter;
    EditText msearch_edit;
    List items;
    Button toggleButton2;
    Button toggleButton3;
    Button toggleButton4;
    Button toggleButton5;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followinglist);
        listView = findViewById(R.id.listView);
        final String friendUserId = getIntent().getExtras().getString("ID");
        getFrinedName(friendUserId);
        getDB(friendUserId);
        navbar();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> selection = (Map<String, String>) parent.getItemAtPosition(position);
                String Friend_info = selection.get("userId");

                Intent oIntent = new Intent(FollowingListActivity.this, FriendPageActivity.class);
                oIntent.putExtra("Friend", Friend_info);
                startActivity(oIntent);

            }
        });
    }

    public void getDB(final String id) {
        items = new ArrayList<Map>();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Following").document(id).collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            final HashMap<String, Object> map = new HashMap<String, Object>();
                            String friendid = document.getId();
                            DocumentReference docRef = db.collection("Profile").document(friendid);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    String name = document.getString("name");

                                    String userName = document.getString("userName");
                                    map.put("userName", userName);
                                    map.put("name", name);
                                    map.put("userId", document.getId());
                                    items.add(map);
                                    goMain(items);
                                }
                            });

                        }
                    }
                });//db.collection END
    }

    public void goMain(List items) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("fileName"); // DB에서 이름 불러와서 여기에다 "images/" 붙여서 넣으면 됨
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
//                Glide.with(GlideActivity.this).load(task.getResult()).into(imageView);
            }
        });
        String[] keys = new String[]{"userName", "name"};
        int[] ids = new int[]{R.id.nameTextView, R.id.userNameTextView};
        listView = findViewById(R.id.listView);


        SimpleAdapter customAdapter = new SimpleAdapter(this, items, R.layout.search_custom_view, keys, ids);
        listView.setAdapter(customAdapter);
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    public void getFrinedName(String friendUserId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Profile").document(friendUserId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                user_name = findViewById(R.id.user_name);
                DocumentSnapshot document = task.getResult();
                user_name.setText(document.get("userName").toString());
            }
        });
    }
    public void navbar(){
        toggleButton2 = findViewById(R.id.toggleButton2);
        toggleButton3 = findViewById(R.id.toggleButton3);
        toggleButton4 = findViewById(R.id.toggleButton4);
        toggleButton5 = findViewById(R.id.toggleButton5);


        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowingListActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowingListActivity.this , PostingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowingListActivity.this , NotificationActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowingListActivity.this , MyPageActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
    }


}