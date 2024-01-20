package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

//주문 내역확인하는곳
public class ordered_content extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter adapter;

    //데이터베이스
    DatabaseHelper_product dbHelper;
    DatabaseHelper_order dbHelper2;
    SQLiteDatabase database;


    String tableName = "Ordered_Product3";
    String tableName2 = "index_product";
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_content);

        ArrayList<Contentobj> order = new ArrayList<Contentobj>();
//
        recyclerView = findViewById(R.id.content_recyerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ContentAdapter adapter = new ContentAdapter();



        createDatabase();
        Cursor cursor = database.rawQuery("select _number, name, amount, price, shot, img, ice, date from Ordered_Product3", null);
        int recordCount = cursor.getCount();

        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();
            int number = cursor.getInt(0);
            String product_name = cursor.getString(1);
            int amount = cursor.getInt(2);
            int price = cursor.getInt(3);
            int shot = cursor.getInt(4);
            int img = cursor.getInt(5);
            int ice = cursor.getInt(6);
            String date = cursor.getString(7);
            total = total + price * amount;
            order.add(new Contentobj(product_name, img, amount, price*amount, date));
            adapter.addItem(order.get(i));

        }

        recyclerView.setAdapter(adapter);



    }

    public void createDatabase(){
        dbHelper = new DatabaseHelper_product(this);
        database = dbHelper.getWritableDatabase();
    }

}