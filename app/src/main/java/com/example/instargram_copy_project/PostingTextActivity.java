package com.example.instargram_copy_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PostingTextActivity extends AppCompatActivity {

    private static final String TAG = "MemberInfoSetting";

    EditText content;
    Button posting_btn;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //user의 정보를 사용할것임
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("Profile").document(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_text);

        posting_btn = findViewById(R.id.posting_btn);

        posting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posting();
            }
        });
    }

    public void posting(){
        String userUid = user.getUid(); //유저 UID 가져오기
        String imgUid = "";  //이미지 UID 가져오기
        String content = ((EditText) findViewById(R.id.content)).getText().toString();  //문구
//        Integer likeCount = 0;  //좋아요 수

        Map<String, String> post = new HashMap<>();
        post.put("userUid", userUid);
        post.put("imgUid",imgUid);
        post.put("content", content);

        db.collection("Post").document().set(post, SetOptions.merge()); //DB 업로드
        startToast("업로드 성공!");

        //홈 화면으로 이동
        Intent intent = new Intent(PostingTextActivity.this, HomeActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


}
