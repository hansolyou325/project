package com.example.reservenew.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reservenew.Activity.MainActivity;
import com.example.reservenew.Model.Hotel;
import com.example.reservenew.Model.Reservation;
import com.example.reservenew.Model.Room;
import com.example.reservenew.Model.User;
import com.example.reservenew.R;
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


public class PaymentFragment extends Fragment {
    final String TAG = "PaymentFragment";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String room_id;
    private String hotel_id;

    private TextView tv_type;
    private TextView tv_bed;
    private TextView tv_check_in_time;
    private TextView tv_check_out_time;
    private TextView tv_check_in_date;
    private TextView tv_check_out_date;
    private TextView tv_price;
    private LinearLayout btn_credit;
    private LinearLayout btn_naver;
    private LinearLayout btn_kakao;

    private Button btn_submit;

    private DatePickerDialog check_in_dialog;
    private DatePickerDialog check_out_dialog;

    private Hotel hotel = new Hotel();;
    private Room  room  = new Room();
    private int select_index = 0;

    public PaymentFragment(String hotel_id, String room_id){
        this.room_id = room_id;
        this.hotel_id = hotel_id;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_payment, container, false);

        tv_type = (TextView) rootView.findViewById(R.id.tv_payment_type);
        tv_bed  = (TextView) rootView.findViewById(R.id.tv_payment_bed);
        tv_check_in_time    = (TextView) rootView.findViewById(R.id.tv_payment_check_in);
        tv_check_out_time   = (TextView) rootView.findViewById(R.id.tv_payment_check_out);
        tv_check_in_date    = (TextView) rootView.findViewById(R.id.tv_payment_check_in_date);
        tv_check_out_date   = (TextView) rootView.findViewById(R.id.tv_payment_check_out_date);
        tv_price            = (TextView) rootView.findViewById(R.id.tv_payment_price);
        btn_submit          = (Button) rootView.findViewById(R.id.btn_payment_submit);

        btn_credit          = (LinearLayout) rootView.findViewById(R.id.ll_pament_credit);
        btn_naver           = (LinearLayout) rootView.findViewById(R.id.ll_pament_naver);
        btn_kakao           = (LinearLayout) rootView.findViewById(R.id.ll_pament_kakao);


        btn_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_index = 1;
                btn_credit.setBackground(getContext().getDrawable(R.drawable.round_box_gray));
                btn_naver.setBackground(getContext().getDrawable(R.drawable.round_box));
                btn_kakao.setBackground(getContext().getDrawable(R.drawable.round_box));
            }
        });

        btn_naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_index = 2;
                btn_credit.setBackground(getContext().getDrawable(R.drawable.round_box));
                btn_naver.setBackground(getContext().getDrawable(R.drawable.round_box_gray));
                btn_kakao.setBackground(getContext().getDrawable(R.drawable.round_box));
            }
        });

        btn_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_index = 3;
                btn_credit.setBackground(getContext().getDrawable(R.drawable.round_box));
                btn_naver.setBackground(getContext().getDrawable(R.drawable.round_box));
                btn_kakao.setBackground(getContext().getDrawable(R.drawable.round_box_gray));
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentTest();
                /*reservation();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.menu_frame_layout, homeFragment);
                transaction.commit();*/
            }
        });

        DatePickerDialog.OnDateSetListener onDateSetListener_1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date_str = String.valueOf(i) + "-" + (i1+1) + "-" + i2;
                tv_check_in_date.setText(date_str);
            }
        };

        DatePickerDialog.OnDateSetListener onDateSetListener_2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date_str = String.valueOf(i) + "-" + (i1+1) + "-" + i2;
                tv_check_out_date.setText(date_str);

                Date s_date = null;
                Date e_date = null;
                try {
                    s_date = new SimpleDateFormat("yyyy-MM-dd").parse(tv_check_in_date.getText().toString());
                    e_date = new SimpleDateFormat("yyyy-MM-dd").parse(tv_check_out_date.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar s_cmpDate = Calendar.getInstance();
                s_cmpDate.setTime(s_date);

                Calendar e_cmpDate = Calendar.getInstance();
                e_cmpDate.setTime(e_date);

                long diffSec = (e_cmpDate.getTimeInMillis() - s_cmpDate.getTimeInMillis()) / 1000;
                long diffDays = diffSec / (24*60*60); //일자수 차이


                int cost = Long.valueOf(diffDays).intValue() * Integer.parseInt(hotel.getPrice_per_night().replace(",",""));
                tv_price.setText(String.valueOf(cost));
            }
        };
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        String[] str = getTime.split("-");

        int year    = Integer.parseInt(str[0]);
        int month   = Integer.parseInt(str[1]);
        int day     = Integer.parseInt(str[2]);

        check_in_dialog     = new DatePickerDialog(getContext(), onDateSetListener_1, year, month-1, day);
        check_out_dialog    = new DatePickerDialog(getContext(), onDateSetListener_2, year, month-1, day+1);

        tv_check_in_date.setText(getTime);
        tv_check_out_date.setText(getTime);

        tv_check_in_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_in_dialog.show();
            }
        });

        tv_check_out_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_out_dialog.show();
            }
        });


        get_hotel();
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
            HotelListFragment roomListFragment = new HotelListFragment();
            transaction.replace(R.id.menu_frame_layout, roomListFragment);
            transaction.commit();
        }
    };

    private void get_hotel(){
        // 콜렉션 선택
        db.collection("Hotel")
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
                                if (document.getString("id").equals(hotel_id)){
                                    hotel.readSnapShot(document);

                                    tv_type.setText(hotel.getType());
                                    tv_bed.setText(hotel.getBed());
                                    tv_price.setText(hotel.getPrice_per_night());
                                }


                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void set_room(){
        Map<String,Object> map = room.to_Map();
        map.put("head_count", "1");
        map.put("is_available","false");
        db.collection("Room").document(room.getDocumentSnapshot().getId()).update(map);
    }

    private void get_room(){
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

                                    tv_check_in_time.setText(room.getCheck_in_time());
                                    tv_check_out_time.setText(room.getCheck_out_time());

                                }


                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void reservation(){
        Reservation reservation = new Reservation(user.getUid(),room.getId(),room.getType(),room.getRoom_number(),tv_price.getText().toString(),
                room.getCheck_in_time(),room.getCheck_out_time(),tv_check_in_date.getText().toString(),tv_check_out_date.getText().toString());
        db.collection("Reservation")
                .add(reservation.to_Map())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        set_room();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        db.collection("Payment")
                .add(reservation.to_Map())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    public void PaymentTest() {
        BootUser user = new BootUser().setPhone(this.user.getPhoneNumber()); // 구매자 정보

        BootExtra extra = new BootExtra()
                .setCardQuota("0"); // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)

        Double price = Double.valueOf(tv_price.getText().toString().replace(",",""));


        List items = new ArrayList<>();
        BootItem item1 = new BootItem().setName(room.getType()+","+room.getRoom_number()).setId("ITEM_CODE_HOLE").setQty(1).setPrice(100d);
        //BootItem item1 = new BootItem().setName(room.getType()+","+room.getRoom_number()).setId("ITEM_CODE_HOLE").setQty(1).setPrice(price);
        items.add(item1);

        String payment_name ="naverpay";
        switch (select_index){
            case 1:
                payment_name = "card";
                break;
            case 2:
                payment_name = "naverpay";
                break;
            case 3:
                payment_name = "kaokaopay";
                break;
        }


        Payload payload = new Payload();
        payload.setApplicationId("6400397f3049c8001f365dbc")
                .setOrderName("호텔 예약 결제")
                .setPg("kcp")
                .setMethod(payment_name)
                .setOrderId("1234")
                //.setPrice(price)
                .setPrice(100d)
                .setUser(user)
                .setExtra(extra)
                .setItems(items);

        Map map = new HashMap<>();
        map.put("1", "abcdef");
        map.put("2", "abcdef55");
        map.put("3", 1234);
        payload.setMetadata(map);
//        payload.setMetadata(new Gson().toJson(map));

        Bootpay.init(getActivity().getSupportFragmentManager(), getContext())
                .setPayload(payload)
                .setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                        Log.d("bootpay", "cancel: " + data);
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("bootpay", "error: " + data);
                    }

                    @Override
                    public void onClose() {
                        Log.d("bootpay", "close: ");
                        Bootpay.removePaymentWindow();
                    }

                    @Override
                    public void onIssued(String data) {
                        Log.d("bootpay", "issued: " +data);
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("bootpay", "confirm: " + data);

                        return true; //재고가 있어서 결제를 진행하려 할때 true (방법 2)
//                        return false; //결제를 진행하지 않을때 false
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("done", data);
                        reservation();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        HomeFragment homeFragment = new HomeFragment();
                        transaction.replace(R.id.menu_frame_layout, homeFragment);
                        transaction.commit();
                    }
                }).requestPayment();
    }




}
