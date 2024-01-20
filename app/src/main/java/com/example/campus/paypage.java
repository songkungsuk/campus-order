package com.example.campus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class paypage extends AppCompatActivity {

    public static final int REQUEST_CODE_MENU = 101; //결제입력창으로 들어가는 페이지


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_MENU){
//            Toast.makeText(getApplicationContext(), "결제 시작",Toast.LENGTH_LONG).show();
//
//        }
        if (resultCode == RESULT_OK){
            Toast.makeText(getApplicationContext(), "결제 취소",Toast.LENGTH_LONG).show();
        }
        if (resultCode == 102){
            try {
                String money = data.getStringExtra("money"); //입력한값 string으로 도출
                String pay_money = data.getStringExtra("pay_money");
                if(money != null){
                    int money_int = Integer.parseInt(money); //String 을 int로 변환
                    int pay_int = Integer.parseInt(pay_money);
                    if (money_int == pay_int){
                        Toast.makeText(getApplicationContext(), "결제 금액 "+ money_int + "원이 완료되었습니다",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), order_number.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else if(money_int < 0){ //간단한 조건문처리
                        Toast.makeText(getApplicationContext(), "결제에 오류가 발생했습니다 다시시도해주십시오",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "결제에 오류가 발생했습니다 다시시도해주십시오",Toast.LENGTH_LONG).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "결제에 오류가 발생했습니다 다시시도해주십시오",Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypage);


        ImageButton button = findViewById(R.id.oder_pay); //기타결제 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialog_url.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        ImageButton imageView = findViewById(R.id.imageButton2); //toss
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://toss.im/"));
                startActivity(intent);
            }
        });



    }

}