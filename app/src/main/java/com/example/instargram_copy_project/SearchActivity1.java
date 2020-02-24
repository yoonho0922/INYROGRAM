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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity1 extends AppCompatActivity {
    private static final String TAG = "MemberInfoSetting";
    ListView mListView;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    EditText msearch_edit;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        msearch_edit = findViewById(R.id.search_edit);
        mListView = findViewById(R.id.listView);
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
                searchshow(msg);
                items.clear();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Friend_info = (String) parent.getItemAtPosition(position);
                Intent oIntent = new Intent(SearchActivity1.this, FriendPageActivity.class);
                oIntent.putExtra("Friend",Friend_info);
                startActivity(oIntent);

            }
        });



    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void searchshow(final String s) {
        mListView = findViewById(R.id.listView);
        items = new ArrayList<>();
        startToast(s);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> mapName = new HashMap<String, Object>();
                                Map<String, Object> mapId = new HashMap<String, Object>();
                                String username = document.getString("userName");

                                if(username.contains(s)){
                                    mapName.put("name", username);
                                    mapId.put("id", document.getId());
                                    items.add(mapName.get("name") + "\n" + mapId.get("id"));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                 }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        adapter = new ArrayAdapter<>(SearchActivity1.this,
                                android.R.layout.simple_list_item_single_choice, items);
                        mListView.setAdapter(adapter);
                    }
                });
    }


}