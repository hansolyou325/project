package com.example.reservenew.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.example.reservenew.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private EditText et_email;
    private EditText et_pwd;
    private Button btn_login;
    private TextView tv_create_account;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private User login_user = new User();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        et_email            = (EditText) findViewById(R.id.et_login_email);
        et_pwd              = (EditText) findViewById(R.id.et_login_pwd);
        btn_login           = (Button) findViewById(R.id.btn_login_login);
        tv_create_account   = (TextView) findViewById(R.id.tv_login_createAccount);

        auto_login();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString().trim();
                String pwd   = et_pwd.getText().toString().trim();
                sign_in(email, pwd);
            }
        });

        tv_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegistActivity.class);
                startActivity(intent);
            }
        });

    }

    private void auto_login(){
        Log.d("[LOGIN_EMAIL]",Util.loadSharedPreference(getApplicationContext(),"email"));
        Log.d("[LOGIN_PWD]",Util.loadSharedPreference(getApplicationContext(),"pwd"));
        if(!Util.loadSharedPreference(getApplicationContext(),"email").equals("")){
            et_email.setText("---");
            et_pwd.setText("---");
            sign_in(Util.loadSharedPreference(getApplicationContext(),"email"),Util.loadSharedPreference(getApplicationContext(),"pwd"));
        }

    }

    private void sign_in(String email, String pwd){
        if (!Util.etTextBlankCK(et_email)){
            if(!Util.etTextBlankCK(et_pwd)){
                firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            saveIdPwd();
                            Intent intent = new Intent(getApplicationContext(),CertificationActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Util.showMsg(getApplicationContext(),"이메일와 비밀번호를 다시 확인해주세요.");
                        }
                    }
                });
            }else{
                Util.showMsg(getApplicationContext(),"비밀번호를 입력해 주세요.");
            }
        }else{
            Util.showMsg(getApplicationContext(),"이메일을 입력해 주세요.");
        }

    }

    public void saveIdPwd(){
        if(!et_email.getText().toString().equals("---"))
            if(!et_pwd.getText().toString().equals("---")){
                Util.saveSharedPreference(getApplicationContext(),"email",et_email.getText().toString());
                Util.saveSharedPreference(getApplicationContext(),"pwd",et_pwd.getText().toString());
            }
    }
}