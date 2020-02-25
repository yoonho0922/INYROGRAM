package com.example.instargram_copy_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    List items;
    SearchCustomAdapter adapter;

    List profileImage;
    List intro;
    List name;
    List userName;
    List website;

    Button toggleButton1;
    Button toggleButton3;
    Button toggleButton4;
    Button toggleButton5;

    ListView listView;


    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        navbar();

        getDB();

    }

    public void getDB(){
        items = new ArrayList<Object>();
        intro = new ArrayList<String>();
        profileImage = new ArrayList<String>();
        name = new ArrayList<String>();
        userName = new ArrayList<String>();
        website = new ArrayList<String>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("profile_image")!=""){
                                    profileImage.add(document.getString("profile_image"));
                                }
                                intro.add(document.getString("intro"));
                                name.add(document.getString("name"));
                                userName.add(document.getString("userName"));
                                website.add(document.getString("website"));
                                Log.d(this.getClass().getName(),"searchActivity로그1"+profileImage.toString());
                            }
                        } else {
                            //에러시
                        }
                        goMain();
                        //여기까지 값이 살아있으니까 여기서 처리해야 된다!!
                    }
                });//db.collection END
        Log.d(this.getClass().getName(),"로그3"+name.toString()); //여기는 null값
    }// getDB END

    public void goMain() {

        Log.d(this.getClass().getName(),"로그4"+userName.toString());
        listView = (ListView) findViewById(R.id.listView);

        adapter = new SearchCustomAdapter();

        setData();

        SearchCustomDTO dto = new SearchCustomDTO();

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * ListView의 Item을 Click 할 때 수행할 동작
             *
             * @param parent   클릭이 발생한 AdapterView.
             * @param view     클릭 한 AdapterView 내의 View(Adapter에 의해 제공되는 View).
             * @param position 클릭 한 Item의 position
             * @param id       클릭 된 Item의 Id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // adapter.getItem(position)의 return 값은 Object 형
                // 실제 Item의 자료형은 CustomDTO 형이기 때문에
                // 형변환을 시켜야 getResId() 메소드를 호출할 수 있습니다.
                String profileImage = ((SearchCustomDTO)adapter.getItem(position)).getProfileImage();
                String intro = ((SearchCustomDTO)adapter.getItem(position)).getIntro();
                String name = ((SearchCustomDTO)adapter.getItem(position)).getName();
                String userName = ((SearchCustomDTO)adapter.getItem(position)).getUserName();
                String website = ((SearchCustomDTO)adapter.getItem(position)).getWebsite();

                Log.d(this.getClass().getName(),"로그10"+intro);
                Log.d(this.getClass().getName(),"로그10"+((SearchCustomDTO)adapter.getItem(position)).getIntro());

                // new Intent(현재 Activity의 Context, 시작할 Activity 클래스)
                Intent intent = new Intent(SearchActivity.this, UserPageActivity.class);
                // putExtra(key, value)
                if(profileImage!=""){
                    intent.putExtra("profileImage", profileImage);
                }
                intent.putExtra("intro", intro);
                intent.putExtra("name", name);
                intent.putExtra("userName", userName);
                intent.putExtra("website", website);

                Log.d(this.getClass().getName(),"로그11"+intent.getStringExtra("intro"));
                Log.d(this.getClass().getName(),"로그11"+intent.getIntExtra("imgRes", 0));



                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    void setData() {

        Log.d(this.getClass().getName(),"로그5"+name.toString());
        Log.d(this.getClass().getName(),"로그5"+userName.toString());
//        int[] profile_img = new int[]{R.drawable.profile1, R.drawable.profile2, R.drawable.profile3, R.drawable.profile4,
//                R.drawable.profile5,R.drawable.profile6,R.drawable.profile7,R.drawable.profile8};


        for (int i = 0; i < name.size(); i++) {     //DB에서 받은 데이터를 DTO에 set
            SearchCustomDTO dto = new SearchCustomDTO();
            if(profileImage.get(i)!=""){
                Log.d(this.getClass().getName(),"search로그6 : "+i+" "+profileImage.get(i));
                dto.setProfileImage(profileImage.get(i).toString());
        }
            dto.setIntro(intro.get(i).toString());
            dto.setName(name.get(i).toString());
            dto.setUserName(userName.get(i).toString());
            dto.setWebsite(website.get(i).toString());
            Log.d(this.getClass().getName(),"로그7"+name.get(i).toString());
            adapter.addItem(dto);
            Log.d(this.getClass().getName(),"로그8"+dto.getName());
        }
    }

    public void navbar(){
        toggleButton1 = findViewById(R.id.toggleButton1);
        toggleButton3 = findViewById(R.id.toggleButton3);
        toggleButton4 = findViewById(R.id.toggleButton4);
        toggleButton5 = findViewById(R.id.toggleButton5);


        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this , HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this , PostingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this , NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        toggleButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this , MyPageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
