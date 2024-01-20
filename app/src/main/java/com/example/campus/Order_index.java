package com.example.campus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Order_index extends AppCompatActivity {


    RecyclerView recyclerView;
    ProductAdapter adapter;

    //데이터베이스
    DatabaseHelper_product dbHelper;
    DatabaseHelper_order dbHelper2;
    SQLiteDatabase database;
    SQLiteDatabase database_order;

    String tableName = "Ordered_Product";
    String tableName2 = "index_product";


    int total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_index);



//
        ArrayList<product_selected> order = new ArrayList<product_selected>();
//
        recyclerView = findViewById(R.id.orderindex);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ResultAdapter adapter = new ResultAdapter();



        createDatabase();
        Cursor cursor = database.rawQuery("select _number, name, amount, price, shot, img, ice from Ordered_Product", null);
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
            total = total + price * amount;
            order.add(new product_selected(product_name, img, amount, price*amount));
            adapter.addItem(order.get(i));

        }

        recyclerView.setAdapter(adapter);

        TextView result = findViewById(R.id.result_total);
        result.setText(Integer.toString(total));

        Button paybtn = findViewById(R.id.paybtn);
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), paypage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(intent, 103);

            }
        });


        if (recordCount == 0){
            LinearLayout linearLayout = findViewById(R.id.no_cart);
            linearLayout.setVisibility(View.VISIBLE);
            LinearLayout linearLayout1 = findViewById(R.id.total_layout);
            linearLayout1.setVisibility(View.INVISIBLE);
            paybtn.setVisibility(View.GONE);

        }


    }

    public void createDatabase(){
        dbHelper = new DatabaseHelper_product(this);
        database = dbHelper.getWritableDatabase();
    }
}