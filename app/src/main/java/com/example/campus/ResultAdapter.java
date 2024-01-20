package com.example.campus;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder>{

    ArrayList<product_selected> items = new ArrayList<product_selected>();
    ResultAdapterListener listener;
    //데이터베이스
    DatabaseHelper_product dbHelper;

    SQLiteDatabase database;


    String tableName = "Ordered_Product";
    String tableName2 = "index_product";


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textView2;
        TextView textView3;

        ImageView imageView;
        TextView delete_item;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.product_name_result);
            textView2 = itemView.findViewById(R.id.amount_result);
            textView3 = itemView.findViewById(R.id.price_result);

            imageView = itemView.findViewById(R.id.img_result);

            delete_item = itemView.findViewById(R.id.delete_item);
            delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //데이터베이스
                    DatabaseHelper_product dbHelper;
                    SQLiteDatabase database;
                    dbHelper = new DatabaseHelper_product(view.getContext());
                    database = dbHelper.getWritableDatabase();

                    int position = getAdapterPosition();
                    int position2 = position + 1;


                    database.execSQL("DELETE FROM Ordered_Product WHERE _number = '" + position2 + "';");
                    dbHelper.UpdateDataBase(database);

                    Cursor cursor = database.rawQuery("select _number, name, amount, price, shot, img, ice from Ordered_Product", null);
                    int recordCount = cursor.getCount();

                    if(recordCount == 0){
                        ((Activity)view.getContext()).finish(); //현재 액티비티 종료 실시
                    }else {
                        Intent intent = ((Activity)view.getContext()).getIntent();
                        ((Activity)view.getContext()).finish(); //현재 액티비티 종료 실시
                        ((Activity)view.getContext()).overridePendingTransition(0, 0); //효과 없애기
                        view.getContext().startActivity(intent); //현재 액티비티 재실행 실시
                        ((Activity)view.getContext()).overridePendingTransition(0, 0); //효과 없애기

                    }




                }
            });

        }

        public void setItem(product_selected item){
            textView.setText(item.getName());
            textView2.setText(Integer.toString(item.getAmount()));
            textView3.setText(Integer.toString(item.getPrice()));

            imageView.setImageResource(item.getImg());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.result_layout, parent, false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        product_selected item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(product_selected item){
        items.add(item);
    }
    public void setItems(ArrayList<product_selected> items) {
        this.items = items;
    }
    public product_selected getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, product_selected item){
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
