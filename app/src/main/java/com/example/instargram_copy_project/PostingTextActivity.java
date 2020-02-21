package com.example.instargram_copy_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostingTextActivity extends AppCompatActivity {

    private static final String TAG = "MemberInfoSetting";
    List items;
    String msg;

    Button get_db_btn;
    TextView db_text_view;

    //// 사용자 정보 (프로필사진, ID, 장소, 내용) ////
    ListView listView;

    int[] user_img = new int[]{R.drawable.dog1, R.drawable.dog2, R.drawable.dog3}; // 프로필 사진
    String[] user_id = new String[]{"lim_jaeyoung0428", "yoon_nno", "sje__22"}; // 사용자 ID
    String[] user_place = new String[]{"Sadang Station", "Seoul City Hall", "Itaewon"}; // 장소
    int[] posting_picture = new int[]{R.drawable.postingex1, R.drawable.postingex2, R.drawable.postingex3}; // 포스팅 사진
    String[] user_posting = new String[]{"Have a nice day :)", "Hello World!", "고기 먹고 싶다 하아아아아"}; // 글 내용
    String[] like = new String[]{"좋아요 360개", "좋아요 100,000개", "좋아요 100개"}; // 좋아요 개수 (일단 넣어봄 사실 동적으로 해야하는데)

    ArrayList<HashMap<String, Object>> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_text);

        getDB();


    }

    public void getDB(){
        db_text_view = findViewById(R.id.db_text_view);
        items = new ArrayList<Object>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                String content = document.getString("content");
                                String fileName = document.getString("fileName");
                                String place = document.getString("place");
                                String userUID = document.getString("userUID");

                                map.put("content", content);
                                map.put("fileName", fileName);
                                map.put("place", place);
                                map.put("userUID", userUID);

                                items.add(map);
                                startToast(document.toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        db_text_view.setText(items.toString());
                    }
                });//db.collection END



        }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }




}
