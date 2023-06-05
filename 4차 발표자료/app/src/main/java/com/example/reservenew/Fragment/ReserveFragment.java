package com.example.reservenew.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.reservenew.Adapter.ReservaationListAdapter;
import com.example.reservenew.Model.Reservation;
import com.example.reservenew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReserveFragment extends Fragment {
    final String TAG = "ReserveFragment";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private ListView listView;
    private ReservaationListAdapter reservaationListAdapter;
    private ArrayList<Reservation> reservationArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_reserve, container, false);

        listView = (ListView) rootView.findViewById(R.id.lv_reservation_list);
        reservationArrayList = new ArrayList<Reservation>();
        reservaationListAdapter = new ReservaationListAdapter(getContext(),reservationArrayList);
        listView.setAdapter(reservaationListAdapter);

        get_reservation();

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
                                if (document.getString("uid").equals(user.getUid())){
                                    Reservation reservation = new Reservation();
                                    reservation.readSnapShot(document);
                                    reservationArrayList.add(reservation);
                                    reservaationListAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}
