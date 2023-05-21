package com.example.reservenew.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.reservenew.Model.Reservation;
import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminReservaationListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Reservation> reservations;
    private ArrayList<String> users;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        Reservation reservation = reservations.get(i);
        tv_type.setText(reservation.getType());
        tv_room_num.setText(reservation.getRoom_number());
        tv_check_in_time.setText(reservation.getCheck_in_time());
        tv_check_out_time.setText(reservation.getCheck_out_time());
        tv_check_in_date.setText(reservation.getCheck_in_date());
        tv_check_out_date.setText(reservation.getCheck_out_date());
        tv_price.setText(reservation.getTotal_price());
        tv_user_name.setText(users.get(i));

        return converView;
    }

}
