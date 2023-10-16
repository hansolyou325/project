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

public class AddUserListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<User> users;

    public AddUserListAdapter(Context context, ArrayList<User> users){
        this.users       = users;
        this.context            = context;
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        LayoutInflater layoutInflatet = LayoutInflater.from(context);
        if (converView == null){
            converView = layoutInflatet.inflate(R.layout.add_user_list_item, null);
        }

        TextView tv_name    = converView.findViewById(R.id.tv_add_user_list_item_name);
        TextView tv_email   = converView.findViewById(R.id.tv_add_user_list_item_email);


        User user = users.get(i);
        tv_name.setText(user.getName());
        tv_email.setText(user.getEmail());


        return converView;
    }

}
