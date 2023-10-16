package com.example.reservenew.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.example.reservenew.Activity.AdminActivity;
import com.example.reservenew.Activity.MainActivity;
import com.example.reservenew.Fragment.AdminReserveFragment;
import com.example.reservenew.Fragment.HomeFragment;
import com.example.reservenew.Fragment.PaymentFragment;
import com.example.reservenew.Model.Reservation;
import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.example.reservenew.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminReservaationListAdapter extends BaseAdapter {

    private String TAG = "AdminReservationListAdapter";
    private Context context;
    private ArrayList<Reservation> reservations;
    private ArrayList<String> users;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    private String reservation_id = null;

    public AdminReservaationListAdapter(Context context, ArrayList<Reservation> reservations, ArrayList<String> user_names){
        this.reservations       = reservations;
        this.context            = context;
        this.users              = user_names;
    }
    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Object getItem(int i) {
        return reservations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        LayoutInflater layoutInflatet = LayoutInflater.from(context);
        if (converView == null){
            converView = layoutInflatet.inflate(R.layout.reserve_admin_list_item, null);
        }

        TextView tv_type            = converView.findViewById(R.id.tv_reserve_list_item_type);
        TextView tv_room_num        = converView.findViewById(R.id.tv_reserve_list_item_room_num);
        TextView tv_check_in_time   = converView.findViewById(R.id.tv_reserve_list_item_check_in_time);
        TextView tv_check_out_time  = converView.findViewById(R.id.tv_reserve_list_item_check_out_time);
        TextView tv_check_in_date   = converView.findViewById(R.id.tv_reserve_list_item_check_in_date);
        TextView tv_check_out_date  = converView.findViewById(R.id.tv_reserve_list_item_check_out_date);
        TextView tv_price           = converView.findViewById(R.id.tv_reserve_list_item_price);
        TextView tv_user_name       = converView.findViewById(R.id.tv_reserve_list_user_name);

        // update 예약 취소 버튼 생성
        Button btn_reservation_cancel = converView.findViewById(R.id.btn_reserve_list_cancel);


        Reservation reservation = reservations.get(i);
        tv_type.setText(reservation.getType());
        tv_room_num.setText(reservation.getRoom_number());
        tv_check_in_time.setText(reservation.getCheck_in_time());
        tv_check_out_time.setText(reservation.getCheck_out_time());
        tv_check_in_date.setText(reservation.getCheck_in_date());
        tv_check_out_date.setText(reservation.getCheck_out_date());
        tv_price.setText(reservation.getTotal_price());
        tv_user_name.setText(users.get(i));


        
        // update 예약 정보 doc 의 id 를 얻어와 reservation_id 에 저장
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
                                Reservation reservationDoc = new Reservation();
                                reservationDoc.readSnapShot(document);
                                if(reservation.getRoom_id().equals(reservationDoc.getRoom_id())){
                                    reservation_id = document.getId();
                                    Log.v("reservationID", reservation_id);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        // update 예약 취소 버튼 이벤트 리스너
        // add 예약 취소 동작 확인 
        // add 예약 취소 후 프래그먼트 리프레쉬 추가
        btn_reservation_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("Reservation").document(reservation_id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        db.collection("Room").document(reservation.getRoom_id())
                                                .update("head_count", "0")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, reservation.getRoom_id() + " 's headcount is successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });

                                        db.collection("Room").document(reservation.getRoom_id())
                                                .update("is_available", "true")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, reservation.getRoom_id() + " 's available is successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });

                                        // 관리자 예약 조회 화면으로 재이동
                                        Util.showMsg(context,reservation.getRoom_id() + "호의 예약이 취소되었습니다.\n" + reservation_id);
                                        FragmentTransaction transaction = ((AdminActivity) context).getSupportFragmentManager().beginTransaction();
                                        AdminReserveFragment adminReserveFragment = new AdminReserveFragment();
                                        transaction.replace(R.id.menu_frame_layout, adminReserveFragment);
                                        transaction.commit();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });

                    }
                }
        );



        return converView;
    }

}
