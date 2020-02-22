package com.example.instargram_copy_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    List items;
    SearchCustomAdapter adapter;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = new HashMap<String, Object>();

                                String name = document.getString("name");
                                String userName = document.getString("userName");

                                map.put("name", name);
                                map.put("userName", userName);
                                items.add(map);
                            }
                        } else {
                            //에러시
                        }
                        goMain(items);
                    }
                });//db.collection END
    }// getDB END

    public void goMain(List items) {

        adapter = new SearchCustomAdapter();
        listView = (ListView) findViewById(R.id.listView);

        setData();

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
//                int imgRes = ((SearchCustomDTO)adapter.getItem(position)).getResId();

                // new Intent(현재 Activity의 Context, 시작할 Activity 클래스)
                Intent intent = new Intent(SearchActivity.this, UserPageActivity.class);
                // putExtra(key, value)
//                intent.putExtra("imgRes", imgRes);
                startActivity(intent);
                finish();
            }
        });
    }

    void setData() {
//        TypedArray arrResId = getResources().obtainTypedArray(R.array.resId);
        int[] profile_img = new int[]{R.drawable.profile1, R.drawable.profile2, R.drawable.profile3};
        String[] name = {"윤호","김신","진구"};
        String[] userName = {"yoon","shin","goo"};

        for (int i = 0; i < profile_img.length; i++) {
            SearchCustomDTO dto = new SearchCustomDTO();
            dto.setResId(profile_img[i]);
            dto.setName(name[i]);
            dto.setUserName(userName[i]);

            adapter.addItem(dto);
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
