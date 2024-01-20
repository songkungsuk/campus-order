package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class order_number extends AppCompatActivity {
    DatabaseHelper_product dbHelper;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_number);

        createDatabase();


        //데이터 조회 및 추가
        Cursor cursor = database.rawQuery("select _number from order_number_table", null);
        int recordCount = cursor.getCount();
        recordCount++;
        String number = Integer.toString(recordCount) + "번";


        TextView order_number = findViewById(R.id.order_number);
        order_number.setText(number);
        //메인화면으로 돌아가기 버튼
        Button goto_main = findViewById(R.id.goto_main);
        goto_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    public void createDatabase(){
        dbHelper = new DatabaseHelper_product(this);
        database = dbHelper.getWritableDatabase();
    }
}
