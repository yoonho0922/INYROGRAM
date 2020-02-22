package com.example.instargram_copy_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_custom_view, null, false);

            holder = new CustomViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        SearchCustomDTO dto = listCustom.get(position);

        holder.imageView.setImageResource(dto.getResId());
        holder.nameTextView.setText(dto.getName());
        holder.userNameTextView.setText(dto.getUserName());

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