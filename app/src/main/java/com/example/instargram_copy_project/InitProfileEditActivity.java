package com.example.instargram_copy_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InitProfileEditActivity extends AppCompatActivity {
    EditText name;
    EditText userName;
    EditText website;
    EditText intro;
    Button finishBtn;
    Button profileEditBtn;
    ImageView imageView;
    private ImageView ivPreview;

    private Uri filePath;
    private FirebaseAuth mAuth;
    private static final String TAG = "MemberInfoSetting";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_profile_edit);
        mAuth = FirebaseAuth.getInstance();



        name = findViewById(R.id.name);
        finishBtn = findViewById(R.id.finishBtn);
        profileEditBtn = findViewById(R.id.profileEditBtn);
        finishBtn = (Button)findViewById(R.id.finishBtn);
        name = (EditText)findViewById(R.id.name);

        profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                FirebaseUser currentUser = mAuth.getCurrentUser();
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
                uploadFile();
            }
        });



        finishBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyPageActivity.class);
                intent.putExtra(" ", name.getText().toString());
                proFile();
                startActivity(intent);



            }
        });
    }



    public void proFile(){ //입력한 프로필 저장하는 함수
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String userName = ((EditText) findViewById(R.id.userName)).getText().toString();//정보를 가지고옴
        String website = ((EditText) findViewById(R.id.website)).getText().toString();
        String intro = ((EditText) findViewById(R.id.intro)).getText().toString();
        String profileImage = "unknownImg.png";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //user의 정보를 사용할것임
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, String> profile = new HashMap<>();
        profile.put("name", name);
        profile.put("userName", userName);
        profile.put("website", website);
        profile.put("intro", intro);
        profile.put("profileImage",profileImage);
        //회원정보가 한번이라도 저장이 되었다


        db.collection("Profile").document(user.getUid()).set(profile, SetOptions.merge())
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();
            imageView = findViewById(R.id.imageView);

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //user의 정보를 사용할것임
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = "profile_image/"+formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://inyrogram.appspot.com").child(filename);
            Map<String, Object> data = new HashMap<>();
            data.put("profile_image", filename);

            db.collection("Profile").document(user.getUid()) //userid에 데이터저장
                    .set(data, SetOptions.merge());
            //      db.collection("Profile").document("Profile_image").set(data, SetOptions.merge());

            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}
