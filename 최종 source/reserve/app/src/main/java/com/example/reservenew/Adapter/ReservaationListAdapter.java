package com.example.reservenew.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.reservenew.Model.Reservation;
import com.example.reservenew.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReservaationListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Reservation> reservations;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ReservaationListAdapter(Context context, ArrayList<Reservation> reservations){
        this.reservations       = reservations;
        this.context            = context;
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
            converView = layoutInflatet.inflate(R.layout.reserve_list_item, null);
        }

        TextView tv_type            = converView.findViewById(R.id.tv_reserve_list_item_type);
        TextView tv_check_in_date   = converView.findViewById(R.id.tv_reserve_list_item_check_in_date);
        TextView tv_check_out_date  = converView.findViewById(R.id.tv_reserve_list_item_check_out_date);
        TextView tv_price           = converView.findViewById(R.id.tv_reserve_list_item_price);

        Reservation reservation = reservations.get(i);
        tv_type.setText(reservation.getType());
        tv_check_in_date.setText(reservation.getCheck_in_date());
        tv_check_out_date.setText(reservation.getCheck_out_date());
        tv_price.setText(reservation.getTotal_price());

        return converView;
    }

}
