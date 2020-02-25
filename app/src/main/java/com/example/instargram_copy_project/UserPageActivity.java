package com.example.instargram_copy_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class UserPageActivity extends AppCompatActivity {

    Button toggleButton1;
    Button toggleButton2;
    Button toggleButton3;
    Button toggleButton4;

    ImageView imageView;
    TextView user_name;
    TextView name_profile;
    TextView web_profile;
    TextView intro_profile;

    TextView followerTv;
    TextView followingTv;
    Button followingBtn;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        navbar();

        // MainActivity에서 보낸 imgRes를 받기위해 getIntent()로 초기화
        Intent intent = getIntent();
        imageView = findViewById(R.id.imageView);
        user_name = findViewById(R.id.user_name);
        name_profile = findViewById(R.id.name_profile);
        web_profile = findViewById(R.id.web_profile);
        intro_profile = findViewById(R.id.intro_profile);

        // "imgRes" key로 받은 값은 int 형이기 때문에 getIntExtra(key, defaultValue);
        // 받는 값이 String 형이면 getStringExtra(key);
        imageView.setImageResource(intent.getIntExtra("imgRes", 0));
        user_name.setText(intent.getStringExtra("userName"));
        name_profile.setText(intent.getStringExtra("name"));
        web_profile.setText(intent.getStringExtra("website"));
        intro_profile.setText(intent.getStringExtra("intro"));

//        followingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                Map<String, Integer> friend_profile = new HashMap<>();
//                friend_profile.put("followset",1);
//                db.collection("Following").document(user.getUid()).collection("friends")
//                        .document(friendUserId).set(friend_profile, SetOptions.merge())
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                startToast("회원정보 저장 완료");
//                                follower(friendUserId);
//                                aramfollowing(friendUserId);
//                                //회원정보가 설정되어있음을 확인
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                startToast("회원정보 저장 실패");
//                                Log.w(TAG, "Error adding document", e);//회원정보가 설정되어있음을 확인
//                            }
//                        });
//            }
//        });
    }

    public void showFollower(String friendUserId){ //팔로우 값을 가져옴
        followerTv.findViewById(R.id.textView8);
        db.collection("Follower").document(friendUserId).collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer follower_su = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                follower_su += 1;
                            }
                            followerTv.setText(follower_su.toString());
                        } else {
                        }
                    }
                });

    }
    public void showFollowing(String friendUserId) { //following수 출력
        followingTv = findViewById(R.id.textView9);
        db.collection("Following").document(friendUserId).collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer following_su = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                following_su += 1;
                            }
                            followingTv.setText(following_su.toString());
                        } else {
                        }
                    }
                });
    }

    public void navbar(){
        toggleButton1 = findViewById(R.id.toggleButton1);
        toggleButton2 = findViewById(R.id.toggleButton2);
        toggleButton3 = findViewById(R.id.toggleButton3);
        toggleButton4 = findViewById(R.id.toggleButton4);


        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPageActivity.this , HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPageActivity.this , SearchActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPageActivity.this , PostingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPageActivity.this , NotificationActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);

            }
        });
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
