package com.example.campus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;


public class Fragment_3 extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_3, container, false);
        mAuth = FirebaseAuth.getInstance();
        LinearLayout myinfo = v.findViewById(R.id.myinfo); // 내정보
        LinearLayout history = v.findViewById(R.id.order_history); // 주문내역
        LinearLayout settings = v.findViewById(R.id.settings); // 설정
        LinearLayout QNA = v.findViewById(R.id.QA); // 문의하기

        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getView();

                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null) { //로그인이 되어있으면
                    Intent intent = new Intent(view.getContext(), my_info.class);
                    startActivity(intent);

                }else {
                    showDialog("로그인후 이용해주세요");
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() { //주문내역
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ordered_content.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("공사중");
            }
        });

        QNA.setOnClickListener(new View.OnClickListener() {//주문번호
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), order_number.class);
                startActivity(intent);
            }
        });






        return v;
    }

    public void showDialog(String text){ //다이얼로그 메소드
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(text);
        builder.setTitle("Error")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Error");
        alert.show();
    }
}