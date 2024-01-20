package com.example.campus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class my_info extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "my_info_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        mAuth = FirebaseAuth.getInstance(); //파이어 베이스로 사용자 가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //----------------------------------------------------------------------------
        //유저 정보 가져옴
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

            EditText id = findViewById(R.id.id_show); //아이디 입력란
            EditText pw = findViewById(R.id.pw_show); //비밀번호 입력란
            EditText uid_text = findViewById(R.id.user_id); //uid 입력란

            id.setText(email);
            uid_text.setText(uid);
        }
        //----------------------------------------------------------------------------
        //아이디 변경하기 버튼
        ImageView change_id = findViewById(R.id.id_change);
        change_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText id = findViewById(R.id.id_show); //아이디 입력란
                EditText pw = findViewById(R.id.pw_show); //비밀번호 입력란
                EditText uid_text = findViewById(R.id.user_id); //uid 입력란
                //아이디변경
                //이메일값 문자화 하고 뽑아냄
                String diffrent_id =  id.getText().toString();

                update_email(diffrent_id);
                showDialog("ID가 변경 되셧습니다");

            }
        });

        //비밀번호 변경하기 버튼
        ImageView change_pw = findViewById(R.id.pw_change);
        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("공사중");
            }
        });

        //계정삭제 버튼
        ImageView delete_id = findViewById(R.id.id_delete);
        delete_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete_id();

            }
        });
    }

    public void update_email(String email){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            Toast.makeText(my_info.this, "이메일 변경 완료", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void delete_id(){

                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("You really want to Delete your id?");
                builder.setTitle("Warning")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User account deleted.");
                                                    Toast.makeText(my_info.this, "ID삭제 완료", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Warnings");
                alert.show();


    }
    public void showDialog(String text){ //다이얼로그 메소드
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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