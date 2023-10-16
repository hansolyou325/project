package com.example.reservenew.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reservenew.Adapter.AddUserListAdapter;
import com.example.reservenew.Model.Hotel;
import com.example.reservenew.Model.Reservation;
import com.example.reservenew.Model.Room;
import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.example.reservenew.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootExtra;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;


public class UserAddFragment extends Fragment {
    final String TAG = "UserAddFragment";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private Button btn_submit;
    private EditText et_email;
    private EditText et_phone;
    private ListView lv_user_list;

    private Reservation reservation;
    private Room room = new Room();

    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<User> reservate_user_list = new ArrayList<User>();
    private AddUserListAdapter adapter;

    private String head_count;


    public UserAddFragment(Reservation reservation){
        this.reservation = reservation;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_user_add, container, false);

        btn_submit  = rootView.findViewById(R.id.btn_user_add_submit);
        et_email    = rootView.findViewById(R.id.et_user_add_email);
        et_phone    = rootView.findViewById(R.id.et_user_add_phone);
        lv_user_list= rootView.findViewById(R.id.lv_user_add_list);

        adapter = new AddUserListAdapter(getContext(),reservate_user_list);
        lv_user_list.setAdapter(adapter);



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.etTextBlankCK(et_email)){
                    if(!Util.etTextBlankCK(et_phone)){
                        User temp_user = check_user(et_email.getText().toString(), et_phone.getText().toString());
                        if(temp_user != null){
                            add_user(temp_user.getUid());

                            // 유저 추가 이후 HomeFragment 로 이동
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            HomeFragment homeFragment = new HomeFragment();
                            transaction.replace(R.id.menu_frame_layout, homeFragment);
                            transaction.commit();
                            
                        }else{
                            Util.showMsg(getContext(),"유저 정보를\n다시 확인해주세요.");
                        }
                    }else{
                        Util.showMsg(getContext(),"핸드폰 번호를\n입력해 주세요.");
                    }
                }else{
                    Util.showMsg(getContext(),"이메일을 입력해 주세요.");
                }

            }
        });

        get_user();
        get_room();
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            transaction.replace(R.id.menu_frame_layout, homeFragment);
            transaction.commit();
        }
    };

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
                            boolean flag = false;
                            // 스냅샷을 순회
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User get_user = new User();
                                get_user.readSnapShot(document);
                                users.add(get_user);
                            }
                            get_user_list();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private User check_user(String email, String phone){
        User result = null;
        for(User user : users){
            if(user.getEmail().equals(email)){
                if(user.getPhone().equals(phone)){
                    result = user;
                }
            }
        }

        return result;
    }


    private User getUser(String uid){
        // 모든 user가 있는 users 에서 uid 와 일치하는 user 를 반환
        User result = null;

        for(User user : users){
            if(user.getUid().equals(uid)){
                result = user;
            }
        }
        return result;
    }

    private void get_user_list(){
        // reservate_user_list (예약한 인원) 에 reservation doc 에 기록된 uid 에 해당하는 user 를 가져오기
        reservate_user_list.add(getUser(reservation.getUid()));
        adapter.notifyDataSetChanged();

        String add_user_str = reservation.getAdd_user();

        if (add_user_str.length()!=0){
            String[] split = add_user_str.split(",");
            for (String uid : split){
                reservate_user_list.add(getUser(uid));
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void add_user(String uid){
        Map<String,Object> map = reservation.to_Map();
        String[] split;

        if(reservation.getAdd_user().length()!= 0){
            map.put("add_user",reservation.getAdd_user()+","+uid);

            // reservation 의 doc 을 추가한 add_user 필드를 반영하여 업데이트
            db.collection("Reservation").document(reservation.getDocumentSnapshot().getId()).update(map);
            split = (reservation.getAdd_user()+","+uid).split(",");
            head_count = String.valueOf(split.length+1);

            // 더해준 유저가 reservation 에 반영이 되어 split.length 가 잘 나오는지 확인
            Log.v("add user check", "split length : " + split.length);
            Log.v("count check", "count : " + head_count);
        }else{
            head_count = "2";
            map.put("add_user",uid);
            // reservation 의 doc 을 추가한 add_user 필드를 반영하여 업데이트
            db.collection("Reservation").document(reservation.getDocumentSnapshot().getId()).update(map);
            Log.v("count check", "count : " + head_count);
        }

        // 예약한 방의 head_count 를 count 로 업데이트
        Map<String,Object> room_map = room.to_Map();
        room_map.put("head_count", head_count);
        db.collection("Room").document(room.getDocumentSnapshot().getId()).update(room_map);

    }

    private void get_room(){
        String room_id = reservation.getRoom_id();
        // 콜렉션 선택
        db.collection("Room")
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
                                if (document.getString("id").equals(room_id)){
                                    room.readSnapShot(document);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }





}
