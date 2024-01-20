package com.example.campus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class sign_up extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText id; //아이디
    EditText pw; // 비밀 번호
    EditText check_pw; // 비밀 번호 확인
    EditText phone_num; // 휴대폰 번호
    EditText correct_num; // 인증 번호
    ImageView sign_in;  // 회원가입 버튼
    ImageView back_btn; // 뒤로가기 버튼
    ImageView confirm_num; // 인증확인
    ImageView confirm_btn; // 인증하기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        //edit text
        id = findViewById(R.id.id_join);
        pw = findViewById(R.id.password_join);
        check_pw = findViewById(R.id.password2_join);
        phone_num = findViewById(R.id.phone_number_input);
        correct_num = findViewById(R.id.input_number);
        //----여기는 이미지뷰 ---
        sign_in = findViewById(R.id.sign_in_button);//회원가입
        back_btn = findViewById(R.id.back_button); // 뒤로가기
        confirm_btn = findViewById(R.id.confirm_button); //핸드폰번호로 인증하기
        confirm_num = findViewById(R.id.confirm_number); // 인증번호 확인

        //Ask Permissions to users
        requirePermission();

        Intent intent = getIntent();
        showNumber(intent);
        onNewIntent(intent);
        /*
        * 회원가입 규칙
        1. 모든 요소가[아이디, 비밀번호, 비밀번호 확인, 핸드폰번호, 인증번호] 있어야함
        2. 비밀번호는 비밀번호확인이랑 같아야함
        3. 휴대폰번호는 아직 상관없음
        4. 인증번호는 sms서비스로 들어온 content 숫자가 일치해야함

        * 위 사항이 충족되면 가입시키고 로그인화면으로 유도
        * */
        sign_in.setOnClickListener(new View.OnClickListener() { // 회원가입버튼 클릭시 위 주석이 충족되어야함
            @Override
            public void onClick(View v) {
                String str_id = id.getText().toString();
                String str_pw = pw.getText().toString();
                String str_checkpw = check_pw.getText().toString();
                String str_phone = phone_num.getText().toString();
                String str_num = correct_num.getText().toString();

                //모든 에디트 텍스트가 비어있지 않아야함 : 규칙 1

                if(str_id.length() == 0){
                    showDialog("아이디를 입력해주세요");

                }
                else if(str_pw.length() == 0){
                    showDialog("비밀번호를 입력해주세요");
                }
                else if(str_checkpw.length() == 0){
                    showDialog("비밀번호 확인을 입력해주세요");
                }
                else if(str_phone.length() == 0){
                    showDialog("전화번호를 입력해주세요");
                }
                else if(str_num.length() == 0){
                    showDialog("인증번호를 입력해주세요");
                }
                else if (str_pw.equals(str_checkpw) == false){ //비밀번호는 비밀번호 확인이랑 같아야함 : 규칙 2
                    showDialog("비밀번호를 일치시켜주세요");
                }else{
                    createAccount(str_id, str_pw);
                    Intent intent1 = new Intent(getApplicationContext(), login.class);

                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                }

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String phoneNumber = phone_num.getText().toString(); //수신자 전화번호
                    String message = "123456"; // 실제 인증번호를 포함하는 메시지를 작성합니다.
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                }catch (Exception e){

                }
            }
        });
    }
    //권한 물어보는거
    private void requirePermission(){
        String[] permissions = {Manifest.permission.RECEIVE_SMS};
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }
    //계정생성
    private void createAccount(String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        String TAG = "hello_firebase";

        if (email != null && password != null) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);

                            }
                        }
                    });

        }
    }

    private void updateUI(FirebaseUser user) {
        Toast.makeText(getApplicationContext(), "회원가입 실패 아이디형식 이메일이아님", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showNumber(intent);
    }
    private void showNumber(Intent intent){
        if(intent != null){

            String number = intent.getStringExtra("number");

            //Show number through TextView
            correct_num.setText(number);

        }
    }

    public void showDialog(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(sign_up.this);
        builder.setMessage(text);
        builder.setTitle("Error")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Error");
        alert.show();
    }

    public void sendmessage(String message){ // 실제 인증번호를 포함하는 메시지를 작성합니다.
//        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder()
//                .addData("message", message)
//                .setToken("수신자 FCM 토큰")
//                .build());
//        FirebaseMessaging.getInstance().send(
//                new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
//                        .setMessageId(id)
//                        .addData("key", "value")
//                        .build());
    }
}