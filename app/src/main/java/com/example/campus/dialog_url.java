package com.example.campus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class dialog_url extends AppCompatActivity {

    public static final int pay = 102;
    DatabaseHelper_product dbHelper;
    SQLiteDatabase database;
    String tableName = "Ordered_Product";

    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_url);

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


        }
        TextView total_pay = findViewById(R.id.result_money);
        total_pay.setText(Integer.toString(total));

        Button button3 = findViewById(R.id.button3); //확인버튼
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                EditText url = findViewById(R.id.use_money);
                String value = url.getText().toString();
                intent.putExtra("money",value);
                intent.putExtra("pay_money", Integer.toString(total));


                if(Integer.parseInt(value) == total){
                    dbHelper.ordered_database(database); //주문내역 테이블로 db 옮기기
                    dbHelper.user_database(database); // 유저테이블 생성후 유저이름과 주문번호 생성
                    initDatabase();
                }



                setResult(pay, intent);
                finish();
            }
        });
        Button button4 = findViewById(R.id.button4); //취소버튼
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                EditText url = findViewById(R.id.url);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    public void initDatabase(){ //db헬퍼로 데이터베이스 초기화
        dbHelper = new DatabaseHelper_product(this);
        database = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(database, 1,1);

    }
    public void createDatabase(){
        dbHelper = new DatabaseHelper_product(this);
        database = dbHelper.getWritableDatabase();
    }
}