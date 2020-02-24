package com.example.instargram_copy_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class SearchActivity2 extends AppCompatActivity {
    private static final String TAG = "MemberInfoSetting";
    ListView listView;
    //ArrayList<String> items;
    ArrayAdapter<String> adapter;
    EditText msearch_edit;
    List items;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        msearch_edit = findViewById(R.id.search_edit);
        listView = findViewById(R.id.listView);
        msearch_edit.addTextChangedListener(new TextWatcher() {
            @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    msg = msearch_edit.getText().toString();
                    getDB(msg);
                    items.clear();
                }
            });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> selection = (Map<String, String>) parent.getItemAtPosition(position);
                String Friend_info = selection.get("userId");

                Intent oIntent = new Intent(SearchActivity2.this, FriendPageActivity.class);
                oIntent.putExtra("Friend",Friend_info);
                startActivity(oIntent);

            }
        });

    }
    public void getDB(final String s) {
        items = new ArrayList<Object>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HashMap<String, Object> map = new HashMap<String, Object>();

                            String userName = document.getString("userName");
                            String name = document.getString("name");
                            //사진도 업데이트
                            if(userName.contains(s) || name.contains(s)) {
                                map.put("userName", userName);
                                map.put("name", name);
                                map.put("userId", document.getId());
                                items.add(map);
                            }

                        }
                        goMain(items);
                    }
                });//db.collection END
    }
    public void goMain(List items){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        listView = findViewById(R.id.listView);
        StorageReference storageReference = firebaseStorage.getReference().child("fileName"); // DB에서 이름 불러와서 여기에다 "images/" 붙여서 넣으면 됨
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
//                Glide.with(GlideActivity.this).load(task.getResult()).into(imageView);
            }
        });
        String[] keys = new String[]{"userName","name"};
        int[] ids = new int[]{R.id.nameTextView,R.id.userNameTextView};


        SimpleAdapter customAdapter = new SimpleAdapter(this, items, R.layout.search_custom_view, keys, ids);
        listView.setAdapter(customAdapter);
    }
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
