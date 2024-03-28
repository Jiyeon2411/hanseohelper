package com.example.hanseohelper_muso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.CustomViewHolder> {

    private ArrayList<requestList> arrayList;
    private Context context;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public RequestAdapter(ArrayList<requestList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cafelist_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getReqImage())
                .into(holder.iv_category);
        holder.tv_category.setText(arrayList.get(position).getReqCategory());
        holder.tv_theme.setText(arrayList.get(position).getReqTheme());
        holder.tv_pay.setText(arrayList.get(position).getReqPay() + "원");

        holder.itemView.setOnClickListener(new View.OnClickListener() { // 추가한 부분
            @Override
            public void onClick(View view) {

                String theme = holder.tv_theme.getText().toString(); //holder로 가져온 값을 변수에 넣기

                Intent intent2 = ((Activity) context).getIntent();
                String id = intent2.getStringExtra("ID");

                Intent intent;//인텐트 선언
                intent = new Intent(context, requestapply.class); //requestapply.class부분에 원하는 화면 연결
                intent.putExtra("ID",id);
                intent.putExtra("theme", theme); //변수값 인텐트로 넘기기
                context.startActivity(intent); //액티비티 열기

            } // 추가한부분

        });

    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_category;
        TextView tv_category;
        TextView tv_theme;
        TextView tv_pay;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_category = itemView.findViewById(R.id.IV_Category);
            this.tv_category = itemView.findViewById(R.id.TV_Category);
            this.tv_theme = itemView.findViewById(R.id.TV_theme);
            this.tv_pay = itemView.findViewById(R.id.TV_pay);
        }
    }
}