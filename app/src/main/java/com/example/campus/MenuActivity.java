package com.example.campus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter adapter;

    public static final int pay = 103;

    //데이터베이스
    DatabaseHelper_product dbHelper;
    DatabaseHelper_order dbHelper2;
    SQLiteDatabase database;
    SQLiteDatabase database_order;

    String tableName = "Ordered_Product";
    String tableName2 = "index_product";
    String cafe_coffee = "coffee";
    String tea2 = "tea";
    String ade2 = "juice";
    String side_menu2 = "side";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        createDatabase();
        Cursor cursor = database.rawQuery("select _number, name, amount, price, shot, img, ice from Ordered_Product", null);
        int recordCount = cursor.getCount();


        //객체 리스트화 시켜서
        ArrayList<Product> beverage2 = new ArrayList<Product>();

        beverage2.add(new Product("아이스 아메리카노", "coffee", 2500, R.drawable.americano));
        beverage2.add(new Product("핫 아메리카노", "coffee", 2000, R.drawable.hotamericano));
        beverage2.add(new Product("돌체 라떼", "coffee", 4500, R.drawable.dollatte));
        beverage2.add(new Product("바닐라 라떼", "coffee", 4500, R.drawable.banilra));
        beverage2.add(new Product("카페 라떼", "coffee", 3000, R.drawable.caffe_latte));
        beverage2.add(new Product("패션후르츠청 차", "juice", 4000, R.drawable.blue_lemonade));
        beverage2.add(new Product("레몬청 차", "juice", 4000, R.drawable.lemontea));
        beverage2.add(new Product("자몽청 차", "juice", 4500, R.drawable.jamontea2));
        beverage2.add(new Product("루이보스 차", "juice", 4000, R.drawable.ruiboss));
        beverage2.add(new Product("히비스커스 차", "juice", 4000, R.drawable.hibitea));
        beverage2.add(new Product("카모마일 차", "juice", 4000, R.drawable.camomailetea));
        beverage2.add(new Product("망고 에이드", "juice", 4000, R.drawable.handoade));
        beverage2.add(new Product("자몽 에이드", "juice", 4000, R.drawable.jamontea));
        beverage2.add(new Product("레몬 에이드", "juice", 4000, R.drawable.lemonade));
        beverage2.add(new Product("조리퐁 쉐이크", "juice", 4000, R.drawable.jorishake));
        beverage2.add(new Product("그린티 쉐이크", "juice", 4000, R.drawable.greenteashake));
        beverage2.add(new Product("초코 쉐이크", "juice", 4000, R.drawable.chokoshake));
        beverage2.add(new Product("딸기 라떼", "juice", 4000, R.drawable.strawberrylatte));
        beverage2.add(new Product("수제 라온 버거", "tea", 5900, R.drawable.hamberger));
        beverage2.add(new Product("라온 샌드위치", "tea", 2500, R.drawable.sandwitch));
        beverage2.add(new Product("라면", "tea", 3000, R.drawable.lamen));
        beverage2.add(new Product("페퍼 민트", "tea", 2000, R.drawable.peper_mint));
        beverage2.add(new Product("딸기필링 도넛", "side", 3000, R.drawable.strawberrypillngdounet));
        beverage2.add(new Product("머랭쿠키", "side", 2000, R.drawable.maerangcookie));
        beverage2.add(new Product("꼬끄", "side", 1500, R.drawable.cooku));
        beverage2.add(new Product("고구마빵", "side", 2000, R.drawable.sweetpotatobread));
        beverage2.add(new Product("사브레 쿠키 set", "side", 3500, R.drawable.sabure));
        beverage2.add(new Product("황치즈 휘낭시에", "side", 1800, R.drawable.hinangshie));
        beverage2.add(new Product("약과 휘낭시에", "side", 1800, R.drawable.hinangshie2));
        beverage2.add(new Product("뉴욕 치즈 케이크", "side", 4000, R.drawable.newyorkcheesecake));
        beverage2.add(new Product("얼그레이 케이크", "side", 4000, R.drawable.algraycake));

        Button button = findViewById(R.id.button6); //결제하기 버튼 객체 생성
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), paypage.class);
//                startActivityForResult(intent, pay);

                Intent intent = new Intent(getApplicationContext(), Order_index.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycleView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ProductAdapter();

        for (int i = 0; i < beverage2.size(); i++) {
            adapter.addItem(beverage2.get(i));
        }


        recyclerView.setAdapter(adapter);


        ImageView blended = findViewById(R.id.all_menu); //전체버튼
        blended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.init_item();

                for (int i = 0; i < beverage2.size(); i++) {
                    adapter.addItem(beverage2.get(i));
                }

                recyclerView.setAdapter(adapter);

            }
        });

        ImageView coffee = findViewById(R.id.coffee);
        ImageView tea = findViewById(R.id.food);
        ImageView ade = findViewById(R.id.drink);
        ImageView side_menu = findViewById(R.id.side_menu);

        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.init_item();


                for (int i = 0; i < beverage2.size(); i++) {
                    if (beverage2.get(i).getManufacturer() == cafe_coffee) {
                        adapter.addItem(beverage2.get(i));
                    }

                }

                recyclerView.setAdapter(adapter);
            }
        });

        tea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.init_item();


                for (int i = 0; i < beverage2.size(); i++) {
                    if (beverage2.get(i).getManufacturer() == tea2) {
                        adapter.addItem(beverage2.get(i));
                    }

                }

                recyclerView.setAdapter(adapter);
            }
        });
        side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.init_item();


                for (int i = 0; i < beverage2.size(); i++) {
                    if (beverage2.get(i).getManufacturer() == side_menu2) {
                        adapter.addItem(beverage2.get(i));
                    }

                }

                recyclerView.setAdapter(adapter);
            }
        });

        ade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.init_item();


                for (int i = 0; i < beverage2.size(); i++) {
                    if (beverage2.get(i).getManufacturer() == ade2) {
                        adapter.addItem(beverage2.get(i));
                    }

                }

                recyclerView.setAdapter(adapter);
            }
        });
        adapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ProductAdapter.ViewHolder holder, View view, int position) {
                Product item = adapter.getItem(position);
                if (item.getPrice() > 0) {
                    Intent intent = new Intent(getApplicationContext(), productselect.class);
                    intent.putExtra("name", item.getName());
                    intent.putExtra("price", Integer.toString(item.getPrice()));
                    intent.putExtra("photo", Integer.toString(item.getImageRes()));
                    intent.putExtra("manufacturer", item.getManufacturer());
                    startActivity(intent);
                }

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_btn); //데이터 베이스 활용하기
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                createDatabase();
//                Cursor cursor = database.rawQuery("select _number, name, amount, price, shot, img, ice from Ordered_Product", null);
//                int recordCount = cursor.getCount();
//
//                for (int i = 0; i < recordCount; i++) {
//                    cursor.moveToNext();
//                    int number = cursor.getInt(0);
//                    String product_name = cursor.getString(1);
//                    int amount = cursor.getInt(2);
//                    int price = cursor.getInt(3);
//                    int shot = cursor.getInt(4);
//                    int img = cursor.getInt(5);
//                    int ice = cursor.getInt(6);
//                    Toast.makeText(getApplicationContext(), product_name, Toast.LENGTH_SHORT).show();
//                }
                Intent intent = new Intent(getApplicationContext(), Order_index.class);
                startActivity(intent);
            }
        });

        if (recordCount > 0) {
            button.setText("결제하기" + " ( " + recordCount + " )");
            button.setBackgroundColor(Color.parseColor("#4169E1"));
            fab.setBackgroundColor(R.drawable.cart_icon);

        }

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
    public void createDatabase(){
        dbHelper = new DatabaseHelper_product(this);
        database = dbHelper.getWritableDatabase();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        finish();//인텐트 종료
        overridePendingTransition(0, 0);//인텐트 효과 없애기
        Intent intent = getIntent(); //인텐트
        startActivity(intent); //액티비티 열기
        overridePendingTransition(0, 0);//인텐트 효과 없애기
    }
}