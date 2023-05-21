package com.example.reservenew.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.reservenew.Model.Reservation;
import com.example.reservenew.Model.Room;
import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment {
    final String TAG = "HomeFragment";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Lock");

    private TextView tv_type;
    private TextView tv_room_num;
    private TextView tv_name;
    private TextView tv_check_in_date;
    private TextView tv_check_out_date;
    private TextView tv_check_in_time;
    private TextView tv_check_out_time;

    private TextView tv_head_count;
    private TextView tv_capacity;
    private Button btn_add_person;
    private Button btn_lock;

    private LinearLayout ll_hotel_list;

    private LinearLayout ll_home_exist1;
    private LinearLayout ll_home_exist2;

    private TextView tv_none1;
    private TextView tv_none2;

    private User login_user = new User();
    private Reservation reservation = new Reservation();
    private Room room = new Room();


    private boolean lock = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
        ll_hotel_list = rootView.findViewById(R.id.lv_home_room_list);
        tv_type = (TextView) rootView.findViewById(R.id.tv_home_type);
        tv_room_num = (TextView) rootView.findViewById(R.id.tv_home_room_number);
        tv_name =   (TextView) rootView.findViewById(R.id.tv_home_room_name);
        tv_check_in_time    = (TextView) rootView.findViewById(R.id.tv_home_room_check_in_time);
        tv_check_out_time   = (TextView) rootView.findViewById(R.id.tv_home_room_check_out_time);
        tv_check_in_date    = (TextView) rootView.findViewById(R.id.tv_home_room_check_in_date);
        tv_check_out_date   = (TextView) rootView.findViewById(R.id.tv_home_room_check_out_date);

        tv_head_count       = (TextView) rootView.findViewById(R.id.tv_home_head_count);
        tv_capacity         = (TextView) rootView.findViewById(R.id.tv_home_capacity);
        btn_add_person      = (Button) rootView.findViewById(R.id.btn_home_add_person);
        btn_lock            = (Button) rootView.findViewById(R.id.btn_home_lock);

        ll_home_exist1      = (LinearLayout) rootView.findViewById(R.id.ll_home_exist1);
        ll_home_exist2      = (LinearLayout) rootView.findViewById(R.id.ll_home_exist2);
        tv_none1            = (TextView) rootView.findViewById(R.id.tv_home_none1);
        tv_none2            = (TextView) rootView.findViewById(R.id.tv_home_none2);


        ll_hotel_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                HotelListFragment roomListFragment = new HotelListFragment();
                transaction.replace(R.id.menu_frame_layout, roomListFragment);
                transaction.commit();
            }
        });



        btn_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lock){
                    lock = false;
                    btn_lock.setText("문 열기");
                    myRef.child(reservation.getRoom_number()).child("lock").setValue("0");
                }else{
                    lock = true;
                    btn_lock.setText("문 닫기");
                    myRef.child(reservation.getRoom_number()).child("lock").setValue("1");
                }
            }
        });

        btn_add_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                UserAddFragment addUserFragment = new UserAddFragment(reservation);
                transaction.replace(R.id.menu_frame_layout, addUserFragment);
                transaction.commit();
            }
        });

        get_user();

        return rootView;
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
                                    get_reservation();
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void get_reservation(){
        // 콜렉션 선택
        db.collection("Reservation")
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
                                reservation.readSnapShot(document);
                                boolean flag = false;
                                if(!reservation.getAdd_user().equals("")){
                                    String str = reservation.getAdd_user();
                                    String[] arr = str.split(",");
                                    for(String uid : arr){
                                        if(uid.equals(user.getUid())){
                                            flag = true;
                                        }
                                    }

                                    int pserson_count = arr.length + 1;
                                }
                                if (document.getString("uid").equals(user.getUid()) || flag){
                                    btn_lock.setVisibility(View.VISIBLE);
                                    ll_home_exist1.setVisibility(View.VISIBLE);
                                    ll_home_exist2.setVisibility(View.VISIBLE);
                                    tv_none1.setVisibility(View.GONE);
                                    tv_none2.setVisibility(View.GONE);

                                    // 숙소 예약이 있으면 숙소 조회 버튼 visible => gone
                                    ll_hotel_list.setVisibility(View.GONE);

                                    tv_type.setText(reservation.getType());
                                    tv_room_num.setText(reservation.getRoom_number());
                                    tv_check_in_time.setText(reservation.getCheck_in_time());
                                    tv_check_out_time.setText(reservation.getCheck_out_time());
                                    tv_check_in_date.setText(reservation.getCheck_in_date());
                                    tv_check_out_date.setText(reservation.getCheck_out_date());
                                    get_room(reservation.getRoom_id());



                                    myRef.child(reservation.getRoom_number()).child("lock").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String value = snapshot.getValue(String.class);
                                            Log.d("[REALTIME]",value);
                                            if(value.equals("0")){
                                                lock = false;
                                            }else{
                                                lock = true;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void get_room(String room_id){
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
                                    tv_head_count.setText(room.getHeaad_count());
                                    tv_capacity.setText(room.getCapacity());
                                    if(room.getHeaad_count().equals(room.getCapacity())){
                                        btn_add_person.setEnabled(false);
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
