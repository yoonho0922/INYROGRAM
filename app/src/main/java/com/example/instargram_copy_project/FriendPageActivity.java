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
    Button following;
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
        String friend_info = getIntent().getExtras().getString("Friend");
        final String friendUserId = getFriendUserId(friend_info);//검색해서 누를 유저의 id를 firned_info에 저장
        getFrinedName(friendUserId);
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                ArrayList friendList = new ArrayList<String>();
                friendList.add(friendUserId);
                Map<String, ArrayList> friend = new HashMap<>();
                friend.put("friend",friendList);
                db.collection("Following").document(user.getUid()).set(friend, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원정보 저장 완료");
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
    }

    public String getFriendUserId(String friend_info){ //검색해서 누른 유저의 id를 얻음.

        StringTokenizer tokens = new StringTokenizer(friend_info, "\n" );
        for( int x = 1; tokens.hasMoreElements(); x++ )
        {
            friend_info = tokens.nextToken();
        }
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

}


