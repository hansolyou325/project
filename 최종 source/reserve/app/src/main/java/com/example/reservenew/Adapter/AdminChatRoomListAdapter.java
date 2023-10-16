package com.example.reservenew.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.example.reservenew.Activity.AdminActivity;
import com.example.reservenew.Fragment.AdminChatFragment;
import com.example.reservenew.Model.ChatRoom;
import com.example.reservenew.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminChatRoomListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChatRoom> arrayList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdminChatRoomListAdapter(Context context, ArrayList<ChatRoom> arrayList){
        this.arrayList  = arrayList;
        this.context    = context;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        LayoutInflater layoutInflatet = LayoutInflater.from(context);
        if (converView == null){
            converView = layoutInflatet.inflate(R.layout.chat_room_list_item, null);
        }

        ChatRoom chatRoom = arrayList.get(i);

        TextView tv_title = (TextView) converView.findViewById(R.id.tv_chat_room_item_title);
        TextView tv_content = (TextView) converView.findViewById(R.id.tv_chat_room_item_content);
        LinearLayout ll_layout = (LinearLayout) converView.findViewById(R.id.ll_admin_chat_room_layout);

        tv_title.setText(chatRoom.getUser_name());
        tv_content.setText(chatRoom.getRecent_msg());

        ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_fragement(chatRoom.getUid());
            }
        });


        return converView;
    }

    private void move_fragement(String uid){
        FragmentTransaction transaction = ((AdminActivity) context).getSupportFragmentManager().beginTransaction();
        AdminChatFragment adminChatFragment = new AdminChatFragment(uid);
        transaction.replace(R.id.menu_frame_layout, adminChatFragment);
        transaction.commit();
    }

}
