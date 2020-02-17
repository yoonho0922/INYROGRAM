package com.example.instargram_copy_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.internal.InternalTokenProvider;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signup_button).setOnClickListener(onClickListener);
        findViewById(R.id.login_button1).setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { //클릭된 위젯의 id를 이용
            switch (v.getId()) {
                case R.id.signup_button:
                    Intent intent_profile = new Intent(SignupActivity.this, InitProfileEditActivity.class);
                    signUp(); // 회원가입 함수 실행
                    startActivity(intent_profile); // 회원가입 수행 후 프로필 받으러 감
                    break;
                case R.id.login_button1:
                    startLoginActivity(); // 로그인 창으로 간다
                    break;

            }
        }
    };

    // 회원가입 함수
    public void signUp() {
        // 이메일, 패스워드, 패스워드 확인 값을 받음
        String email = ((EditText) findViewById(R.id.idsignup)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordsignup)).getText().toString();
        String re_password = ((EditText) findViewById(R.id.repasswordsignup)).getText().toString();

        if (email.contains("@") && email.contains(".")) { // 아이디가 이메일 형식인지 확인
            if (password.equals(re_password) ) { // 비밀번호와 재비밀번호가 일치하는지 확인
                if (Integer.parseInt(password) > 6) { // 비밀번호가 여섯자리 이상인지 확인

                    mAuth.createUserWithEmailAndPassword(email, password) // 변수 email과 password의 저장된 값을 전송 (mAuth는 Firebase 객체)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser(); // 로그인 성공
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });
                    startToast("정상적으로 회원가입 되었습니다.");
                }
                else { // 비밀번호가 여섯자리 이상이 아닐 때
                    startToast("비밀번호는 여섯자리 이상으로 설정해주십시오.");
                }
            }
            else { // 비밀번호와 재비밀번호가 다를 때
                startToast("비밀번호가 같지 않습니다.");
            }
        }
        else{ // 아이디가 이메일 형식이 아닐 때
            startToast("올바른 이메일 형식을 입력해주십시오.");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // 로그인 창으로 가는 함수
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
