package com.example.campus;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper_product extends SQLiteOpenHelper {
    public static String NAME = "Product.db";
    public static int VERSION = 1;
    FirebaseAuth mAuth;

    public DatabaseHelper_product(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists Ordered_Product("
                + " _number integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " amount integer, "
                + " price integer, "
                + " shot integer, "
                + " img integer, "
                + " ice integer)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > 1) {
            db.execSQL("DROP TABLE IF EXISTS Ordered_Product");
        }

        db.execSQL("DROP TABLE IF EXISTS Ordered_Product");
        onCreate(db);
    }

    public void UpdateDataBase(SQLiteDatabase db){
        String Table_name1 = "Ordered_Product";
        String Table_name2 = "Ordered_Product2";
        //DB2 생성
        String sql_create = "create table if not exists Ordered_Product2("
                + " _number integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " amount integer, "
                + " price integer, "
                + " shot integer, "
                + " img integer, "
                + " ice integer)";

        db.execSQL(sql_create);


        //Table 1 조회 및 Table2 에삽입
        Cursor cursor = db.rawQuery("select _number, name, amount, price, shot, img, ice from Ordered_Product", null);
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

            String sql_insert = "insert into " + Table_name2 + "(name, amount, price, shot, img, ice) "
                    + " values "
                    + "('" + product_name + "', " + amount + ", " + price + ", " + shot + ", " + img + ", " + ice + ")";
            db.execSQL(sql_insert);
        }

        //Table 1 초기화
        db.execSQL("DROP TABLE IF EXISTS Ordered_Product");
        onCreate(db);

        //Table 2 조회 및 Table1 에삽입
        cursor = db.rawQuery("select _number, name, amount, price, shot, img, ice from Ordered_Product2", null);
        int recordCount2 = cursor.getCount();

        for (int i = 0; i < recordCount2; i++) {
            cursor.moveToNext();
            int number = cursor.getInt(0);
            String product_name = cursor.getString(1);
            int amount = cursor.getInt(2);
            int price = cursor.getInt(3);
            int shot = cursor.getInt(4);
            int img = cursor.getInt(5);
            int ice = cursor.getInt(6);

            String sql_insert = "insert into " + Table_name1 + "(name, amount, price, shot, img, ice) "
                    + " values "
                    + "('" + product_name + "', " + amount + ", " + price + ", " + shot + ", " + img + ", " + ice + ")";
            db.execSQL(sql_insert);
        }
        //Table 2 삭제
        db.execSQL("DROP TABLE IF EXISTS Ordered_Product2");

    }
    //주문내역 테이블 생성 적재
    public void ordered_database(SQLiteDatabase db){
        String Table_name1 = "Ordered_Product";
        String Table_name3 = "Ordered_Product3";
        //DB2 생성
        String sql_create = "create table if not exists Ordered_Product3("
                + " _number integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " amount integer, "
                + " price integer, "
                + " shot integer, "
                + " img integer, "
                + " ice integer,"
                + " date text)";

        db.execSQL(sql_create);
        // 현재 날짜 구하기



        //Table 1 조회 및 Table2 에삽입
        Cursor cursor = db.rawQuery("select _number, name, amount, price, shot, img, ice from Ordered_Product", null);
        int recordCount = cursor.getCount();

        //날짜 구하기
//        long now = System.currentTimeMillis();
//        Date date = new Date(now);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String getTime = dateFormat.format(date);

        Date dateNow = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());
        // "M월 dd일" 에는 원하는 형식을 넣어주면 됩니다.

        String txt_name = format.format(dateNow);
//        String txt_exception = ".txt";
        String year = txt_name.substring(0, 4);
        String month = txt_name.substring(5, 7);
        String dayOfMonth = txt_name.substring(8);
        String time = "'" + year + "년" + month + "월" + dayOfMonth+ "일" + "'" ;

        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();
            int number = cursor.getInt(0);
            String product_name = cursor.getString(1);
            int amount = cursor.getInt(2);
            int price = cursor.getInt(3);
            int shot = cursor.getInt(4);
            int img = cursor.getInt(5);
            int ice = cursor.getInt(6);






            String sql_insert = "insert into " + Table_name3 + "(name, amount, price, shot, img, ice, date) "
                    + " values "
                    + "('" + product_name + "', " + amount + ", " + price + ", " + shot + ", " + img + ", " + ice + ","+ time +")";
            db.execSQL(sql_insert);
        }

    }

    public void drop_database(SQLiteDatabase db){db.execSQL("DROP TABLE IF EXISTS Ordered_Product3");}

    public void user_database(SQLiteDatabase db){//주문번호를 만들기 위한 데이터베이스
        String user_table = "order_number_table"; //테이블이름
        String sql_create = "create table if not exists order_number_table(" //테이블생성
                + " _number integer PRIMARY KEY autoincrement, "
                + " name text)";
        db.execSQL(sql_create);


        //로그인 아이디 분석

        mAuth = FirebaseAuth.getInstance(); //파이어 베이스로 사용자 가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName(); //이름
            String email = user.getEmail(); //이메일
            Uri photoUrl = user.getPhotoUrl(); // 사진

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified(); //이메일 인증상태

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid(); // uid 번호
            //------------------------------------------------------------------------
            //각각 객체 선언하고 정보 가져왓음




            String sql_insert = "insert into " + user_table + "(name) "
                    + " values "
                    + "('" + name + "')";
            db.execSQL(sql_insert);
        }



    }
}
