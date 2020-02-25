package com.example.instargram_copy_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FriendPageActivity  extends AppCompatActivity {

    Button toggleButton1;
    private FirebaseAuth mAuth;
    private static final String TAG = "FreindPageActivity";
    Button toggleButton2;
    Button toggleButton3;
    Button toggleButton4;
    TextView followertv;
    TextView followertv1;
    TextView followingtv;
    TextView followingtv1;
    TextView postingtv;
    Button following;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView user_name;
    Button profileEditBtn;
    TextView name_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        navbar();
        name_profile = findViewById(R.id.name_profile);
        following = findViewById(R.id.following);
        user_name = findViewById(R.id.user_name);
        followertv1 = findViewById(R.id.textView11);
        followingtv1 = findViewById(R.id.textView12);
        followertv = findViewById(R.id.textView8);
        final String friendUserId = getIntent().getExtras().getString("Friend");
        Log.d(this.getClass().getName(),"friend로그10"+friendUserId);
        getFrinedName(friendUserId);
        showFollower(friendUserId);
        showFollowing(friendUserId);
        showPosting(friendUserId);
        showUnfollowing(friendUserId);
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Integer> friend_profile = new HashMap<>();
                friend_profile.put("followset",1);
                db.collection("Following").document(user.getUid()).collection("friends")
                        .document(friendUserId).set(friend_profile, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원정보 저장 완료");
                                follower(friendUserId);
                                aramfollowing(friendUserId);
                                //회원정보가 설정되어있음을 확인

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 저장 실패");
                                Log.w(TAG, "Error adding document", e);//회원정보가 설정되어있음을 확인
                            }
                        });
            }
        });
        followertv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent oIntent = new Intent(FriendPageActivity.this, FollowerListActivity.class);
                oIntent.putExtra("ID",friendUserId);
                startActivity(oIntent);
            }
        });
        followertv1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent oIntent = new Intent(FriendPageActivity.this, FollowerListActivity.class);
                oIntent.putExtra("ID",friendUserId);
                startActivity(oIntent);
            }
        });
        followingtv1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent oIntent = new Intent(FriendPageActivity.this, FollowingListActivity.class);
                oIntent.putExtra("ID",friendUserId);
                startActivity(oIntent);
            }
        });
        followingtv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent oIntent = new Intent(FriendPageActivity.this, FollowingListActivity.class);
                oIntent.putExtra("ID",friendUserId);
                startActivity(oIntent);
            }
        });
    }


    public String getFriendUserId(String friend_info){ //검색해서 누른 유저의 id를 얻음.

        StringTokenizer tokens = new StringTokenizer(friend_info, "userId=" );
        for( int x = 1; tokens.hasMoreElements(); x++ )
        {
           friend_info = tokens.nextToken();
        }
        user_name.setText(friend_info);
        return friend_info;
    }
    public void getFrinedName(String friendUserId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Profile").document(friendUserId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                user_name.setText(document.getString("userName"));
                name_profile.setText(document.getString("name"));
            }
        });
    }
    public void aramfollowing(String friendUserId){//팔로잉한 상대 친구에게 알람 가도록 설정
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Integer> friend_profile = new HashMap<>();
        friend_profile.put("followset",1); //팔로우 안했을때
        db.collection("Aram").document(friendUserId).collection("friends")
                .document(user.getUid()).set(friend_profile, SetOptions.merge());
    }
    public void follower(String friendUserId) {//팔로우 정보
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Integer> friend_profile = new HashMap<>();
        friend_profile.put("followset", 1);
        db.collection("Follower").document(friendUserId).collection("friends")
                .document(user.getUid()).set(friend_profile, SetOptions.merge());
    }
    public void showFollower(String friendUserId){ //팔로우 값을 가져옴
        db.collection("Follower").document(friendUserId).collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer follower_su = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                follower_su += 1;
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            followertv.setText(follower_su.toString());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public void showFollowing(String friendUserId) { //following수 출력
        followingtv = findViewById(R.id.textView9);
        db.collection("Following").document(friendUserId).collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer following_su = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                following_su += 1;
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            followingtv.setText(following_su.toString());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void showPosting(String friendUserId) { //포스팅수 출력
        postingtv = findViewById(R.id.textView2);
        db.collection("Post").document(friendUserId).collection("privatePost")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer posting_su = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                posting_su += 1;
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            postingtv.setText(posting_su.toString());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void navbar(){
        toggleButton1 = findViewById(R.id.toggleButton1);
        toggleButton2 = findViewById(R.id.toggleButton2);
        toggleButton3 = findViewById(R.id.toggleButton3);
        toggleButton4 = findViewById(R.id.toggleButton4);
        profileEditBtn = findViewById(R.id.profileEditBtn);


        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendPageActivity.this , HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendPageActivity.this , SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendPageActivity.this , PostingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendPageActivity.this , NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showUnfollowing(final String friend_info) { //만약 user가 해당 view 친구를 팔로잉 했을때 button에 언팔로워 라고 뜨게 한다.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //user의 정보를 사용할것임
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Following").document(user.getUid()).collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(friend_info)) {
                                    following = findViewById(R.id.following);
                                    following.setText("팔로잉 취소");
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                            }else{
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                });
    }

}


