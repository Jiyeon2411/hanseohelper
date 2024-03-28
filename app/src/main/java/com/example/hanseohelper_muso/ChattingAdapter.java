/* package com.example.hanseohelper_muso;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingAdapter extends BaseAdapter {

    ArrayList<ChattingInfo> chattingInfoArrayList;
    LayoutInflater layoutInflater = null;

    public ChattingAdapter(ArrayList<ChattingInfo> chattingInfoArrayList, LayoutInflater layoutInflater){
        this.chattingInfoArrayList = chattingInfoArrayList;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return (chattingInfoArrayList != null ? chattingInfoArrayList.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return chattingInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChattingInfo item = chattingInfoArrayList.get(position);

        View itemView;

        //보낸 메세지가 나의 메세지인지 여부 판별

        //나의 메세지일 경우
        if(item.getChatEmail().equals(SaveSharedPreference.getUserEmail(parent.getContext()))){
            itemView = layoutInflater.inflate(R.layout.chatting_view, parent, false);

            TextView message = (TextView) itemView.findViewById(R.id.chatting_msg);
            TextView time = (TextView) itemView.findViewById(R.id.chatting_time);

            message.setText(item.getChatMessage());
            time.setText(item.getChatTime());
        }

        //상대방의 메세지일 경우
        else{
            itemView = layoutInflater.inflate(R.layout.chatting_view2, parent, false);

            CircleImageView image = (CircleImageView) itemView.findViewById(R.id.chatting_img);
            TextView name = (TextView) itemView.findViewById(R.id.chatting_name);
            TextView message = (TextView) itemView.findViewById(R.id.chatting_msg);
            TextView time = (TextView) itemView.findViewById(R.id.chatting_time);

            Glide.with(itemView)
                    .load(item.getChatImage())
                    .into(image);
            name.setText(item.getChatName());
            message.setText(item.getChatMessage());
            time.setText(item.getChatTime());

        }

        return itemView;
    }
}
*/