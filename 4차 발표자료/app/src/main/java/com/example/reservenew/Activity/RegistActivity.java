package com.example.reservenew.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.example.reservenew.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistActivity extends AppCompatActivity {
    final String TAG = "RegistActivity";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText et_email;
    private EditText et_name;
    private EditText et_pwd;
    private EditText et_pwd_cofirm;
    private EditText et_phone;

    private Button btn_submit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        et_name         = (EditText) findViewById(R.id.et_regist_name);
        et_email        = (EditText) findViewById(R.id.et_regist_email);
        et_pwd          = (EditText) findViewById(R.id.et_regist_pwd);
        et_pwd_cofirm   = (EditText) findViewById(R.id.et_regist_confirm);
        et_phone        = (EditText) findViewById(R.id.et_regist_phone);
        btn_submit      = (Button) findViewById(R.id.btn_regist_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.etTextBlankCK(et_name))
                    if(!Util.etTextBlankCK(et_email))
                        if(!Util.etTextBlankCK(et_pwd))
                            if(!Util.etTextBlankCK(et_pwd_cofirm))
                                if (!Util.etTextBlankCK(et_phone)) {
                                    if(et_pwd.getText().toString().equals(et_pwd_cofirm.getText().toString())){
                                        String email        = et_email.getText().toString();
                                        String name         = et_name.getText().toString();
                                        String pwd          = et_pwd.getText().toString();
                                        String phone_num    = et_phone.getText().toString();

                                        createUser(email,pwd,name,phone_num);
                                    }else
                                        Util.showMsg(getApplicationContext(),"비밀번호가 일치하지 않습니다.\n다시 확인해주세요.");
                                }else
                                    Util.showMsg(getApplicationContext(),"핸드폰 번호를 입력해 주세요.");
                            else
                                Util.showMsg(getApplicationContext(),"비밀번호 확인을 입력해 주세요.");
                        else
                            Util.showMsg(getApplicationContext(),"비밀번호를 입력해 주세요.");
                    else
                        Util.showMsg(getApplicationContext(),"이메일을 입력해 주세요.");
                else
                    Util.showMsg(getApplicationContext(),"이름을 입력해 주세요.");
            }
        });
    }

    private void createUser(String email, String password, String name, String phone_num) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User createuser = new User(user.getUid(),email,name,phone_num,"nomal","");
                            db.collection("User")
                                    .add(createuser.to_Map())
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            // 회원가입 성공시
                                            Toast.makeText(RegistActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });


                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(RegistActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}