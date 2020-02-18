package com.example.instargram_copy_project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchshow(search_edit.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void searchshow(String msg){
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
                                Map mapname = new HashMap<String, Object>();
                                Map mapid = new HashMap<String, Object>();
                                startToast(search_edit.getText().toString());
                                String name = document.get("userName").toString();
                                if((name.contains(search_edit.getText().toString()))==false) {
                                    continue;
                                }
                                else {
                                    mapname.put("name", name);
                                    mapid.put("id", document.getId());
                                    items.add(mapname.get("name").toString() + "\n" + mapid.get("id"));
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


