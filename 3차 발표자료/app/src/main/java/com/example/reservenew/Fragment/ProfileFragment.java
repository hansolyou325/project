package com.example.reservenew.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.reservenew.Activity.CertificationActivity;
import com.example.reservenew.Activity.LoginActivity;
import com.example.reservenew.Activity.MainActivity;
import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.example.reservenew.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ProfileFragment extends Fragment {
    final String TAG = "ProfileFragment";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView tv_email;
    private TextView tv_name;
    private LinearLayout ll_logout;
    private LinearLayout ll_change_pwd;
    private LinearLayout ll_change_auth;

    private User login_user = new User();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_profile, container, false);

        tv_email        = (TextView) rootView.findViewById(R.id.tv_profile_email);
        tv_name         = (TextView) rootView.findViewById(R.id.tv_profile_name);
        ll_change_auth  = (LinearLayout) rootView.findViewById(R.id.ll_profile_auth_code);
        ll_change_pwd   = (LinearLayout) rootView.findViewById(R.id.ll_profile_password);
        ll_logout       = (LinearLayout) rootView.findViewById(R.id.ll_profile_logout);


        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.saveSharedPreference(getContext(),"email","");
                Util.saveSharedPreference(getContext(),"pwd","");
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ll_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(login_user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Util.showMsg(getContext(),"이메일로 비밀번호 재설정\n링크가 발송되었습니다.");
                        }
                    }
                });
            }
        });

        ll_change_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init_certification();
            }
        });
        get_user();
        return rootView;
    }

    private void init_certification(){
        Map<String,Object> map = login_user.to_Map();
        map.put("auth_code","");
        db.collection("User").document(login_user.getDocumentSnapshot().getId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(getContext(), CertificationActivity.class);
                startActivity(intent);
                getActivity().finish();
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

                                    tv_name.setText(login_user.getName());
                                    tv_email.setText(login_user.getEmail());
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
