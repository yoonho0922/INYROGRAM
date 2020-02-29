package com.example.instargram_copy_project;

import androidx.annotation.NonNull;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class AdapterActivity extends BaseAdapter {

    private ArrayList<Map> item;

    // ListViewAdapter의 생성자
    public AdapterActivity(ArrayList item) {
        this.item = item;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return item.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_custom_view, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득


        final TextView userNameUp  = convertView.findViewById(R.id.userName_up);
        final TextView userNameUnder  = convertView.findViewById(R.id.userName_under);
        final TextView placeTv = convertView.findViewById(R.id.placetextView);
        final TextView contentTv = convertView.findViewById(R.id.content_textView);

        final ImageView profileImageView = convertView.findViewById(R.id.picture_profile);
        final ImageView test3 = convertView.findViewById(R.id.picture_posting);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final Map listViewItem = item.get(position);
        final String userUID = listViewItem.get("userUID").toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Profile").document(userUID); //현재 유저의 프로필 접근


        // 아이템 내 각 위젯에 데이터 반영
        if(listViewItem.get("fileName")!=null) {
            placeTv.setText(listViewItem.get("place").toString());
            contentTv.setText(listViewItem.get("content").toString());
            //profile에서 닉네임 가져오기
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    userNameUp.setText(document.getString("userName"));//
                    userNameUnder.setText(document.getString("userName"));

                    //프로필 사진
                    final String profileImage = document.getString("profile_image");
                    if(profileImage != "") {
                        FirebaseStorage.getInstance().getReference().child(profileImage).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Glide.with(context).load(task.getResult()).into(profileImageView);
                            }
                        });
                    }
                }
            });

            FirebaseStorage.getInstance().getReference().child(listViewItem.get("fileName").toString()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(context).load(task.getResult()).into(test3);
                }
            });
        }


        return convertView;
    }


    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return item.get(position) ;
    }

}

