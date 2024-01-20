package com.example.campus;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;


public class Fragment_1 extends Fragment {
    private FirebaseAuth mAuth;
    Intent intent;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_1, container, false);
        //------------------------------로그인 버튼 -------------------------------------------------------------------
        ImageView login_btn = v.findViewById(R.id.login_button);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), login.class);
                startActivity(intent);
            }
        });

        //-------------------------------주문하기 버튼----------------------------------------------------------------------
        //라온 주문 하기 버튼 누르는 것
        ImageView order_btn1 = v.findViewById(R.id.Order_Button1);
        order_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null) { //로그인이 되어있으면
                    Intent intent = new Intent(getActivity().getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                }else {
                    showDialog("로그인후 이용해주세요");
                }
            }
        });
        //코베티 주문 하기 버튼 누르는 것
        ImageView order_btn2 = v.findViewById(R.id.Order_Button2);
        order_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null) { //로그인이 되어있으면
                    Intent intent = new Intent(getActivity().getApplicationContext(), MenuActivitiy_cobeti.class);
                    startActivity(intent);
                }else {
                    showDialog("로그인후 이용해주세요");
                }

            }
        });

        //--------------------------------------------------매장 정보 확인 하기 버튼---------------------------------------------------------------------
        //라온 매장 정보 확인 하기 버튼
        ImageView cafe_info1 = v.findViewById(R.id.shop_info1);
        cafe_info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Cafe_info_Activity.class);
                startActivity(intent);
            }
        });
        //코베티 매장 정보 확인 하기 버튼
        ImageView cafe_info2 =v.findViewById(R.id.shop_info2);
        cafe_info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Cafe_info_Activitiy2.class);
                startActivity(intent);

            }
        });
        //--------------------------------------로그아웃 버튼 --------------
        ImageView logout = v.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = ((Activity)v.getContext()).getIntent();
                ((Activity)v.getContext()).finish(); //현재 액티비티 종료 실시
                ((Activity)v.getContext()).overridePendingTransition(0, 0); //효과 없애기
                v.getContext().startActivity(intent); //현재 액티비티 재실행 실시
                ((Activity)v.getContext()).overridePendingTransition(0, 0); //효과 없애기
            }
        });
        //------------------------카메라 버튼 ------------------------------------------
        FloatingActionButton camerabtn = v.findViewById(R.id.camera);
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCamera();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){ //로그인이 되어있으면
                View v = getView();
                TextView idView = v.findViewById(R.id.idView); //아이디 보여주는곳 객체화
                ImageView logout = v.findViewById(R.id.logout_button);
                ImageView login = v.findViewById(R.id.login_button);
                String user_name = currentUser.getEmail(); //이메일주소 꺼내옴
                String user = user_name.concat(" 님 환영합니다"); // ~~님 환영합니다
                idView.setText(user); //아이디로 텍스트 꺼냄
                idView.setVisibility(View.VISIBLE); //아이디보여주는곳 보여짐
                logout.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
            }
            else {
                View v = getView();
                TextView idView = v.findViewById(R.id.idView); //아이디 보여주는곳 객체화
                ImageView logout = v.findViewById(R.id.logout_button);
                ImageView login = v.findViewById(R.id.login_button);
                idView.setVisibility(View.INVISIBLE); //아이디보여주는곳 보여짐
                logout.setVisibility(View.INVISIBLE);
                login.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showDialog(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(text);
        builder.setTitle("Error")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        Intent intent = new Intent(getActivity().getApplicationContext(), login.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Error");
        alert.show();
    }

    public void OpenCamera() { //메소드 내용
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //인텐트 생성후 카메라 앱사용
        startActivity(intent); //카메라앱사용 startActivitForResult 메소드는 반환값이 있어야함
    }
}