package com.example.reservenew.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.reservenew.Model.Reservation;
import com.example.reservenew.Model.Room;
import com.example.reservenew.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminStateListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Room> rooms;

    public AdminStateListAdapter(Context context, ArrayList<Room> rooms){
        this.rooms       = rooms;
        this.context            = context;
    }
    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int i) {
        return rooms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        LayoutInflater layoutInflatet = LayoutInflater.from(context);
        if (converView == null){
            converView = layoutInflatet.inflate(R.layout.admin_room_state_list_item, null);
        }

        TextView tv_type            = converView.findViewById(R.id.tv_admin_room_state_item_type);
        TextView tv_num             = converView.findViewById(R.id.tv_admin_room_state_item_room_num);


        Room room = rooms.get(i);
        tv_type.setText(room.getType());
        tv_num.setText(room.getRoom_number());


        return converView;
    }

}
