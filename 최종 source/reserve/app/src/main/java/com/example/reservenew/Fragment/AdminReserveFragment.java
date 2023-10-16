package com.example.reservenew.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reservenew.Adapter.AdminReservaationListAdapter;
import com.example.reservenew.Adapter.ReservaationListAdapter;
import com.example.reservenew.Model.Reservation;
import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminReserveFragment extends Fragment {
    final String TAG = "ReserveFragment";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private ListView listView;
    private AdminReservaationListAdapter reservaationListAdapter;
    private ArrayList<Reservation> reservationArrayList;
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<String> user_name_list = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_reserve, container, false);

        listView = (ListView) rootView.findViewById(R.id.lv_reservation_list);
        reservationArrayList = new ArrayList<Reservation>();
        reservaationListAdapter = new AdminReservaationListAdapter(getContext(),reservationArrayList, user_name_list);
        listView.setAdapter(reservaationListAdapter);

        try {
            get_user();
        }catch (Exception e){
            e.printStackTrace();
        }


        return rootView;
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
                                    Reservation reservation = new Reservation();
                                    reservation.readSnapShot(document);
                                    reservationArrayList.add(reservation);
                                    for(User user_obj : users){
                                        if(user_obj.getUid().equals(reservation.getUid())){
                                            user_name_list.add(user_obj.getName());
                                        }
                                    }
                                    reservaationListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
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
                                User get_user = new User();
                                get_user.readSnapShot(document);
                                users.add(get_user);
                            }
                            get_reservation();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
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
            AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
            transaction.replace(R.id.menu_frame_layout, adminHomeFragment);
            transaction.commit();
        }
    };


}
