package com.example.instargram_copy_project;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class SearchCustomAdapter extends BaseAdapter {

    private ArrayList<SearchCustomDTO> listCustom = new ArrayList<>();

    // ListView에 보여질 Item 수
    @Override
    public int getCount() {
        return listCustom.size();
    }

    // 하나의 Item(ImageView 1, TextView 2)
    @Override
    public Object getItem(int position) {
        return listCustom.get(position);
    }

    // Item의 id : Item을 구별하기 위한 것으로 position 사용
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 실제로 Item이 보여지는 부분
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_custom_view, null, false);

            holder = new CustomViewHolder();
//            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);



            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        final ImageView test3 = convertView.findViewById(R.id.imageView);

        SearchCustomDTO dto = listCustom.get(position);

//        holder.imageView.setImageResource(dto.getResId());

        Log.d(this.getClass().getName(),"searchCustom로그5"+dto.getProfileImage());
        holder.nameTextView.setText(dto.getName());
        holder.userNameTextView.setText(dto.getUserName());

        if(dto.getProfileImage()!="") {
            FirebaseStorage.getInstance().getReference().child(dto.getProfileImage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(context).load(task.getResult()).into(test3);
                }
            });
        }

        return convertView;
    }

    class CustomViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView userNameTextView;
    }

    // MainActivity에서 Adapter에있는 ArrayList에 data를 추가시켜주는 함수
    public void addItem(SearchCustomDTO dto) {
        listCustom.add(dto);
    }
}