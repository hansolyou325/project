package com.example.reservenew.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reservenew.Model.Msg;
import com.example.reservenew.Model.Reservation;
import com.example.reservenew.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Msg> msgs;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ChatListAdapter(Context context, ArrayList<Msg> msgs){
        this.msgs       = msgs;
        this.context    = context;
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int i) {
        return msgs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        LayoutInflater layoutInflatet = LayoutInflater.from(context);
        if (converView == null){
            converView = layoutInflatet.inflate(R.layout.chat_msg_item, null);
        }
        LinearLayout ll_send_layout = converView.findViewById(R.id.ll_chat_msg_send);
        LinearLayout ll_recv_layout = converView.findViewById(R.id.ll_chat_msg_recv);
        TextView tv_send = converView.findViewById(R.id.tv_chat_msg_send);
        TextView tv_recv = converView.findViewById(R.id.tv_chat_msg_recv);

        Msg msg = msgs.get(i);
        if(!msg.getUser().equals("admin")){
            ll_recv_layout.setVisibility(View.GONE);
            ll_send_layout.setVisibility(View.VISIBLE);
            tv_send.setText(msg.getMsg());
        }else{
            ll_recv_layout.setVisibility(View.VISIBLE);
            ll_send_layout.setVisibility(View.GONE);
            tv_recv.setText(msg.getMsg());
        }

        return converView;
    }

}
