package com.example.instargram_copy_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    GridView gridView;
    TextView postingtv;
    Button following;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView user_name;
    Button profileEditBtn;
    ArrayList items;
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
        gridView = findViewById(R.id.gridView);
        final String friendUserId = getIntent().getExtras().getString("Friend");
        Log.d(this.getClass().getName(),"friend로그10"+friendUserId);
        showProfileImage(friendUserId);
        getFrinedName(friendUserId);
        showFollower(friendUserId);
        showFollowing(friendUserId);
        showPosting(friendUserId);
        showUnfollowing(friendUserId);
        getDB(friendUserId);
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Integer> friend_profile = new HashMap<>();
                if(following.getText().equals("팔로잉 취소"))
                {
                    db.collection("Following").document(user.getUid()).collection("friends").document(friendUserId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startToast("팔로잉 취소 완료");
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                }
                else {
                    friend_profile.put("followset", 1);
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
                }

        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> selection = (Map<String, String>) parent.getItemAtPosition(position);
                String doc_info = selection.get("docId");
                Intent oIntent = new Intent(FriendPageActivity.this, PrivatePostActivity.class);
                oIntent.putExtra("docId",doc_info);
                oIntent.putExtra("Friend", friendUserId);
                startActivity(oIntent);

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
    public void getDB(String friend_info) {
        items = new ArrayList<Map>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //user의 정보를 사용할것임
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Post").document(friend_info).collection("privatePost")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            String fileName = document.getString("fileName");
                            String docId = document.getId();
                            map.put("fileName", fileName);
                            map.put("docId", docId);
                            items.add(map);
                        }
                        gridView = findViewById(R.id.gridView);
                        MyPageAdapterActivity customAdapter = new MyPageAdapterActivity(items);
                        gridView.setAdapter(customAdapter);

                    }
                });//db.collection
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

    public void showProfileImage(String userUID){
        DocumentReference docRef = db.collection("Profile").document(userUID);
        final ImageView imageView = findViewById(R.id.imageView);

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
                        Glide.with(FriendPageActivity.this).load(task.getResult()).into(imageView);
                    }
                });

            }
        });
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
    public void aramfollowing(final String friendUserId){//팔로잉한 상대 친구에게 알람 가도록 설정
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Map<String,String> friend_profile = new HashMap<>();
        friend_profile.put("set","1");
        db.collection("Aram").document(friendUserId).collection("FollowAram")
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


