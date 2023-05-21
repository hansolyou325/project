package com.example.reservenew.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reservenew.Activity.AdminActivity;
import com.example.reservenew.Activity.MainActivity;
import com.example.reservenew.R;

public class AdminHomeFragment extends Fragment {
    LinearLayout ll_room_state;
    LinearLayout ll_reserve;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_admin_home, container, false);

        ll_room_state   = (LinearLayout) rootView.findViewById(R.id.ll_admin_home_room_state);
        ll_reserve      = (LinearLayout) rootView.findViewById(R.id.ll_admin_home_room_reserve);

        ll_room_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AdminRoomStateFragment adminRoomStateFragment = new AdminRoomStateFragment();
                transaction.replace(R.id.menu_frame_layout, adminRoomStateFragment);
                transaction.commit();
            }
        });

        ll_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = ((AdminActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                AdminReserveFragment adminReserveFragment = new AdminReserveFragment();
                transaction.replace(R.id.menu_frame_layout, adminReserveFragment);
                transaction.commit();
            }
        });
        return rootView;
    }


}
