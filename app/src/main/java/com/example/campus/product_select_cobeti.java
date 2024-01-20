package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class product_select_cobeti extends AppCompatActivity {
    int img;
    String total; //상품선택할때 전체가격
    int amount; // 상품수량
    int price2; // 상품가격
    int shot = 0; // 샷
    boolean ice = true; // 얼음 유무
    int ice_int;
    public static final int order = 2000; //확인버튼 눌럿을때 반환값

    DatabaseHelper_product dbHelper;
    SQLiteDatabase database;

    String tableName = "Ordered_Product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_select_cobeti);


        Intent intent = getIntent(); //전 액티비티의 값들을 가져옴
        ice_effect(); // 얼음 있음으로 표시
        product_select_effect(); // 커피가아닐때 얼음과 샷추가 없애기
        //각각의 요소들을 가져옴
        ImageView photo = findViewById(R.id.product_image_cobeti);
        TextView name = findViewById(R.id.name_cobeti);
        TextView price = findViewById(R.id.price_cobeti);
        String manufacturer = intent.getStringExtra("manufacturer");



        img = Integer.parseInt(intent.getStringExtra("photo"));
        photo.setImageResource(img);
        name.setText(intent.getStringExtra("name"));
        String product_name = intent.getStringExtra("name");

        price.setText(intent.getStringExtra("price"));

        //수량
        TextView count2 = findViewById(R.id.count2_cobeti);
        amount = Integer.parseInt(count2.getText().toString());
        //가격
        price2 = Integer.parseInt(price.getText().toString());
        //합계
        total = Integer.toString(amount * price2 );

        TextView result = findViewById(R.id.result_cobeti);
        result.setText(total);

        //plus and minus button 수량
        ImageButton plus1 = findViewById(R.id.plus1_cobeti);
        plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView count2 = findViewById(R.id.count2);
                amount++;
                count2.setText(Integer.toString(amount));
                total = Integer.toString(amount * price2 );
                TextView result = findViewById(R.id.result);
                result.setText(total);

            }
        });
        ImageButton minus1 = findViewById(R.id.minus1_cobeti);
        minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView count2 = findViewById(R.id.count2);
                if(amount > 1){
                    amount--;
                    count2.setText(Integer.toString(amount));
                    total = Integer.toString(amount * price2 );
                    TextView result = findViewById(R.id.result);
                    result.setText(total);
                }else{
                    Toast.makeText(getApplicationContext(), "1 이하로는 할 수 없습니다", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // plus minus button 샷추가
        ImageButton plus2 = findViewById(R.id.plus2_cobeti);
        plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shot++;
                shot_text_set();
            }
        });
        ImageButton minus2 = findViewById(R.id.minus2_cobeti);
        minus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shot--;
                shot_text_set();
            }
        });

        //얼음 on / off
        Button on = findViewById(R.id.on_cobeti);
        Button off = findViewById(R.id.off_cobeti);
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ice = true;
                ice_int = 1;
                ice_effect();
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ice = false;
                ice_int = 0;
                ice_effect();
            }
        });

        //확인 버튼
        Button Ok_button = findViewById(R.id.ok_cobeti);
        Ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), MenuActivitiy_cobeti.class);
                createDatabase();
                createTable();



//                Toast.makeText(productselect.this, "ice : " + ice + " ice_int " + ice_int + product_name , Toast.LENGTH_SHORT).show();
                String sql = "insert into " + tableName + "(name, amount, price, shot, img, ice) "
                        + " values "
                        + "('" + product_name + "', " + amount + ", " + price2 + ", " + shot + ", " + img + ", " + ice_int + ")";
                database.execSQL(sql);

                Cursor cursor = database.rawQuery("select _number, name, amount, price, shot, img, ice from Ordered_Product", null);
                int recordCount = cursor.getCount();

                for (int i = 0; i < recordCount; i++){
                    cursor.moveToNext();
                    int number = cursor.getInt(0);
                    String product_name = cursor.getString(1);
                    int amount = cursor.getInt(2);
                    int price = cursor.getInt(3);
                    int shot = cursor.getInt(4);
                    int img = cursor.getInt(5);
                    int ice = cursor.getInt(6);
                    Toast.makeText(getApplicationContext(), product_name , Toast.LENGTH_SHORT).show();
                }

                cursor.close();

                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                Toast.makeText(getApplicationContext(), "구매상품에 추가되셧습니다", Toast.LENGTH_SHORT).show();
            }
        });
        // 취소버튼
        Button cancel_button = findViewById(R.id.cancel_cobeti);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), MenuActivitiy_cobeti.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();

                setResult(order,intent2);
                finish();
            }
        });
    }
    public void shot_text_set(){
        TextView count3 = findViewById(R.id.count3_cobeti); // 샷추가
        String none_shot = "없음";
        if (shot > 0){
            count3.setText(Integer.toString(shot));
        }else{
            count3.setText(none_shot);
        }
    }

    public void ice_effect(){ //얼음 있으면 효과줌
        Button on = findViewById(R.id.on_cobeti);
        Button off = findViewById(R.id.off_cobeti);

        if (ice == true){
            on.setBackgroundColor(Color.CYAN);
            off.setBackgroundColor(0);
        }else {
            off.setBackgroundColor(Color.RED);
            on.setBackgroundColor(0);
        }
    }

    public void createDatabase(){
        dbHelper = new DatabaseHelper_product(this);
        database = dbHelper.getWritableDatabase();
    }

    public void createTable(){
        database.execSQL("create table if not exists "+ tableName +"("
                + " _number integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " amount integer, "
                + " price integer, "
                + " shot integer, "
                + " ice integer)");
    }
    /*
        public void executeQuery() {
            Cursor cursor = database.rawQuery("select _number, name, amount, price, shot, img, ice from Ordered_Product", null);
            int recordCount = cursor.getCount();

            for (int i = 0; i < recordCount; i++){
                cursor.moveToNext();
                int number = cursor.getInt(0);
                String product_name = cursor.getString(1);
                int amount = cursor.getInt(2);
                int price = cursor.getInt(3);
                int shot = cursor.getInt(4);
                int img = cursor.getInt(5);
                int ice = cursor.getInt(6);
            }

            cursor.close();
        }
    */
    public void product_select_effect(){ //제품 종류가 커피가 아닐때 샷추가 없애기
        LinearLayout ice_lay = findViewById(R.id.ice_layout_cobeti);
        LinearLayout shot_lay = findViewById(R.id.shot_layout_cobeti);

        Intent intent = getIntent();
        String manufacturer = intent.getStringExtra("manufacturer");
        // manufacturer names = [coffee, juice, tea, side]


        switch (manufacturer){ //제품군 이름이 ~~일때 레이아웃 안보이게함
            case "juice":
                shot_lay.setVisibility(View.INVISIBLE);
                break;
            case "side":
            case "tea":
                shot_lay.setVisibility(View.INVISIBLE);
                ice_lay.setVisibility(View.INVISIBLE);
        }
    }
}
