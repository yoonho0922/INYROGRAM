package com.example.instargram_copy_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        user_name = findViewById(R.id.user_name);
        name_profile = findViewById(R.id.name_profile);
        web_profile = findViewById(R.id.web_profile);
        intro_profile = findViewById(R.id.intro_profile);

        followingBtn = findViewById(R.id.following_btn);

        // "imgRes" key로 받은 값은 int 형이기 때문에 getIntExtra(key, defaultValue);
        // 받는 값이 String 형이면 getStringExtra(key);
        user_name.setText(intent.getStringExtra("userName"));
        name_profile.setText(intent.getStringExtra("name"));
        web_profile.setText(intent.getStringExtra("website"));
        intro_profile.setText(intent.getStringExtra("intro"));


        final String userUID = intent.getStringExtra("userUID");
        showProfileImage(userUID);
        showFollower(userUID);
        showFollowing(userUID);


        followerTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent oIntent = new Intent(UserPageActivity.this, FollowerListActivity.class);
                oIntent.putExtra("ID",userUID);
                startActivity(oIntent);
            }
        });
        followingTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent oIntent = new Intent(UserPageActivity.this, FollowingListActivity.class);
                oIntent.putExtra("ID",userUID);
                startActivity(oIntent);
            }
        });

        followingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Integer> friend_profile = new HashMap<>();
                friend_profile.put("followset",1);
                db.collection("Following").document(user.getUid()).collection("friends")
                        .document(userUID).set(friend_profile, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원정보 저장 완료");
                                follower(userUID);
                                aramfollowing(userUID);
                                //회원정보가 설정되어있음을 확인

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 저장 실패");
                            }
                        });
            }
        });
    }

    public void showProfileImage(String userUID){
        DocumentReference docRef = db.collection("Profile").document(userUID);
        imageView = findViewById(R.id.imageView);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                String profileImage = document.getString("profile_image");
                //프사 저장 안됐을 경우
                if(profileImage == ""){
                    return;
                }

                //DB에서 사진 가져와서 이미지 넣기
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference().child(profileImage); // DB에서 이름 불러와서 여기에다 "images/" 붙여서 넣으면 됨
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Glide.with(UserPageActivity.this).load(task.getResult()).into(imageView);
                    }
                });

            }
        });
    }

    public void showFollower(String userUID){ //팔로우 값을 가져옴
        followerTv = findViewById(R.id.textView8);
        db.collection("Follower").document(userUID).collection("friends")
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
    public void showFollowing(String userUID) { //following수 출력
        followingTv = findViewById(R.id.textView9);
        db.collection("Following").document(userUID).collection("friends")
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

    public void follower(String friendUserId) {//팔로우 정보
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Integer> friend_profile = new HashMap<>();
        friend_profile.put("followset", 1);
        db.collection("Follower").document(friendUserId).collection("friends")
                .document(user.getUid()).set(friend_profile, SetOptions.merge());
    }

    public void aramfollowing(String friendUserId){//팔로잉한 상대 친구에게 알람 가도록 설정
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Integer> friend_profile = new HashMap<>();
        friend_profile.put("followset",1); //팔로우 안했을때
        db.collection("Aram").document(friendUserId).collection("friends")
                .document(user.getUid()).set(friend_profile, SetOptions.merge());
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
