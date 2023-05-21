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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.example.reservenew.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CertificationActivity extends AppCompatActivity {
    final String TAG = "CertificationActivity";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView tv_label;
    private EditText et_pwd_setting;
    private EditText et_pwd_confirm;
    private EditText et_pwd;
    private LinearLayout ll_setting;
    private LinearLayout ll_pwd;
    private TextView tv_move_login;

    private Button btn_submit;
    private User login_user = new User();

    private boolean setting_flag = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);

        tv_label            = (TextView) findViewById(R.id.tv_certification_label);
        et_pwd              = (EditText) findViewById(R.id.et_certification_pwd);
        et_pwd_setting      = (EditText) findViewById(R.id.et_certification_pwd_setting);
        et_pwd_confirm      = (EditText) findViewById(R.id.et_certification_pwd_confirm);
        btn_submit          = (Button) findViewById(R.id.btn_certification_submit);
        ll_setting          = (LinearLayout) findViewById(R.id.ll_certification_setting);
        ll_pwd              = (LinearLayout) findViewById(R.id.ll_certification_pwd);
        tv_move_login       = (TextView) findViewById(R.id.tv_certification_move_login);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setting_flag){
                    // 세팅 완료된
                    if (login_user.getAuth_code().equals(et_pwd.getText().toString())){
                        move_main();
                    }else{
                        Util.showMsg(getApplicationContext(),"인증코드가 다릅니다.\n 다시 확인해 주세요.");
                    }
                }else{
                    // 비밀번호 등록
                    if(!Util.etTextBlankCK(et_pwd_setting))
                        if(!Util.etTextBlankCK(et_pwd_confirm))
                            if(et_pwd_setting.getText().toString().length() == 6)
                                if(et_pwd_setting.getText().toString().equals(et_pwd_confirm.getText().toString())){
                                    String code = et_pwd_setting.getText().toString();
                                    set_certification(code);
                                }
                            else
                                Util.showMsg(getApplicationContext(),"6자리 숫자를 입력해주세요.");
                        else
                            Util.showMsg(getApplicationContext(),"인증번호 확인을 입력해주세요.");
                    else
                        Util.showMsg(getApplicationContext(),"설정할 인증번호를 입력해주세요.");

                }
            }
        });

        tv_move_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                Util.saveSharedPreference(getApplicationContext(),"email","");
                Util.saveSharedPreference(getApplicationContext(),"pwd","");
                startActivity(intent);
                finish();
            }
        });

        et_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_pwd.setText("");
            }
        });
        ll_setting.setVisibility(View.GONE);
        ll_pwd.setVisibility(View.VISIBLE);

        get_user();
    }

    private void move_main(){
        if(login_user.getGrade().equals("admin")){
            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void set_certification(String code){
        Map<String,Object> map = login_user.to_Map();
        map.put("auth_code",code);
        db.collection("User").document(login_user.getDocumentSnapshot().getId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                move_main();
            }
        });
    }

    private void get_user(){
        // 콜렉션 선택
        db.collection("User")
                // 스냅샷 가져옴
                .get()
                // 가져오기 작업 완료리스너 선언
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    // 작업 완료시
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // 작업에 성공했다면,
                        if (task.isSuccessful()) {
                            // 스냅샷을 순회
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // 만약 로그인한 아이디 와 현재 순회중인 document의 email값이 같다면,
                                if(document.getString("id").equals(user.getUid())){
                                    login_user.readSnapShot(document);
                                    if(login_user.getAuth_code().equals("")){
                                        setting_flag = false;
                                        ll_setting.setVisibility(View.VISIBLE);
                                        ll_pwd.setVisibility(View.GONE);
                                        tv_label.setText("사용자 인증 비밀번호\n6자리를 입력해 주세요.");
                                    }else{
                                        setting_flag = true;
                                        ll_setting.setVisibility(View.GONE);
                                        ll_pwd.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}