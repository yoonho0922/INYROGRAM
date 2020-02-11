package com.example.instargram_copy_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    //// 사용자 정보 (프로필사진, ID, 장소, 내용) ////
    ListView listView;

    int[] user_img = new int[]{R.drawable.dog1, R.drawable.dog2, R.drawable.dog3}; // 프로필 사진
    String[] user_id = new String[]{"lim_jaeyoung0428", "yoon_nno", "sje__22"}; // 사용자 ID
    String[] user_place = new String[]{"Sadang Station", "Seoul City Hall", "Itaewon"}; // 장소
    int[] posting_picture = new int[]{R.drawable.postingex1, R.drawable.postingex2, R.drawable.postingex3}; // 포스팅 사진
    String[] user_posting = new String[]{"Have a nice day :)", "Hello World!", "고기 먹고 싶다 하아아아아"}; // 글 내용
    String[] like = new String[]{"좋아요 360개", "좋아요 100,000개", "좋아요 100개"}; // 좋아요 개수 (일단 넣어봄 사실 동적으로 해야하는데)

    ArrayList<HashMap<String, Object>> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //// 리스트뷰 안에 내용 - 배열에 정보 넣기 ////
        int i = 0;
        while (i < user_id.length) {
            HashMap<String, Object> map = new HashMap<>();

            map.put("img", user_img[i]); // 프로필 사진
            map.put("id", user_id[i]); // 사용자 ID
            map.put("place", user_place[i]); // 장소
            map.put("posting_picture", posting_picture[i]); // 포스팅 사진
            map.put("posting", user_posting[i]); // 글 내용
            map.put("like", like[i]); // 좋아요 개수 (일단 그냥 넣어본 것임)


            list.add(map);
            i++;
        }

        //// 리스트뷰 안에 내용 - key와 view의 아이디를 매핑 ////
        String[] keys = new String[]{"img", "id", "place", "posting_picture", "id", "posting", "like"};
        int[] ids = new int[]{R.id.picture_profile,R.id.idtextView_up,R.id.placetextView,R.id.picture_posting,R.id.idtextView_under,R.id.postingtextView,R.id.liketextView};

        listView = findViewById(R.id.listView);

        SimpleAdapter customAdapter = new SimpleAdapter(this, list, R.layout.home_custom_view, keys, ids);
        listView.setAdapter(customAdapter);


        //// 하단 바 토글버튼 ////
        final ToggleButton toggle1 = findViewById(R.id.toggleButton1);
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle1.isChecked()) {
                    toggle1.setBackgroundDrawable(getResources().getDrawable(R.drawable.homebutton));
                }
                else{
                    toggle1.setBackgroundDrawable(getResources().getDrawable(R.drawable.homeonbutton));
                }
            }
        });

        final ToggleButton toggle2 = findViewById(R.id.toggleButton2);
        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle2.isChecked()) {
                    toggle2.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchbutton));
                }
                else{
                    toggle2.setBackgroundDrawable(getResources().getDrawable(R.drawable.searchonbutton));
                }
            }
        });


        final ToggleButton toggle3 = findViewById(R.id.toggleButton3);
        toggle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle3.isChecked()) {
                    toggle3.setBackgroundDrawable(getResources().getDrawable(R.drawable.addbutton));
                }
                else{
                    toggle3.setBackgroundDrawable(getResources().getDrawable(R.drawable.addonbutton));
                }
            }
        });


        final ToggleButton toggle4 = findViewById(R.id.toggleButton4);
        toggle4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle4.isChecked()) {
                    toggle4.setBackgroundDrawable(getResources().getDrawable(R.drawable.heartbutton));
                }
                else{
                    toggle4.setBackgroundDrawable(getResources().getDrawable(R.drawable.heartonbutton));
                }
            }
        });


        final ToggleButton toggle5 = findViewById(R.id.toggleButton5);
        toggle5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggle5.isChecked()) {
                    toggle5.setBackgroundDrawable(getResources().getDrawable(R.drawable.userbutton));
                }
                else{
                    toggle5.setBackgroundDrawable(getResources().getDrawable(R.drawable.useronbutton));
                }
            }
        });

    }
}
