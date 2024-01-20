package com.example.campus;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder>{
    ArrayList<Contentobj> items = new ArrayList<Contentobj>();
    ResultAdapterListener listener;
    //데이터베이스
    DatabaseHelper_product dbHelper;

    SQLiteDatabase database;

    String tableName = "Ordered_Product";



    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textView2;
        TextView textView3;

        ImageView imageView;
        TextView date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.product_name_content);
            textView2 = itemView.findViewById(R.id.amount_content);
            textView3 = itemView.findViewById(R.id.price_content);

            imageView = itemView.findViewById(R.id.img_content);
            date = itemView.findViewById(R.id.Date);



        }

        public void setItem(Contentobj item){
            textView.setText(item.getName());
            textView2.setText(Integer.toString(item.getAmount()));
            textView3.setText(Integer.toString(item.getPrice()));
            imageView.setImageResource(item.getImg());
            date.setText(item.getDate());

        }
    }

    @NonNull
    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.content_layout, parent, false);


        return new ContentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentAdapter.ViewHolder holder, int position) {
        Contentobj item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(Contentobj item){
        items.add(item);
    }
    public void setItems(ArrayList<Contentobj> items) {
        this.items = items;
    }
    public Contentobj getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, Contentobj item){
        items.set(position, item);
    }
    public void setOnItemClickListener(ResultAdapterListener listener) {
        this.listener = listener;
    }

    public void initDatabase(View view){ //db헬퍼로 데이터베이스 초기화
        dbHelper = new DatabaseHelper_product(view.getContext());
        database = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(database, 1,1);

    }
}
