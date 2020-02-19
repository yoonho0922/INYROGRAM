package com.example.instargram_copy_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity1 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "MemberInfoSetting";
    private ListView mListView;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    EditText search_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_edit = findViewById(R.id.search_edit);
        mListView = findViewById(R.id.listView);
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String msg = search_edit.getText().toString();
                searchshow(msg);
                items.clear();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Friend_info = (String) parent.getItemAtPosition(position);
                startToast(Friend_info);
                Intent oIntent = new Intent(SearchActivity1.this, FriendPageActivity.class);
                oIntent.putExtra("Friend",Friend_info);
                startActivity(oIntent);

            }
        });



    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void searchshow(final String msg){
        mAuth = FirebaseAuth.getInstance();
        mListView = (ListView) findViewById(R.id.listView);
        items = new ArrayList<String>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //user의 정보를 사용할것임
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map mapName = new HashMap<String, Object>();
                                Map mapId = new HashMap<String, Object>();
                                startToast(msg);
                                String name = document.get("userName").toString();
                                if(!(name.contains(msg))) {
                                    continue;
                                }
                                else {
                                    mapName.put("name", name);
                                    mapId.put("id", document.getId());
                                    items.add(mapName.get("name") + "\n" + mapId.get("id"));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        adapter = new ArrayAdapter<String>(SearchActivity1.this,
                                android.R.layout.simple_list_item_single_choice, items);
                        mListView.setAdapter(adapter);
                    }
                });

    }



}