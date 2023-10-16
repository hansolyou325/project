package com.example.reservenew.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reservenew.Adapter.AdminChatListAdapter;
import com.example.reservenew.Adapter.ChatListAdapter;
import com.example.reservenew.Model.Msg;
import com.example.reservenew.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminChatFragment extends Fragment {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();

    private EditText et_msg;
    private Button btn_send;
    private ListView lv_chat_list;
    private ArrayList<Msg> msgs;
    private AdminChatListAdapter chatListAdapter;
    private String user_uid;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Message");

    public AdminChatFragment(String uid){
        this.user_uid = uid;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_chat, container, false);

        lv_chat_list = (ListView) rootView.findViewById(R.id.lv_chat_list);
        et_msg = (EditText) rootView.findViewById(R.id.et_chat_msg_box);
        btn_send = (Button) rootView.findViewById(R.id.btn_chat_send);

        msgs = new ArrayList<Msg>();
        chatListAdapter = new AdminChatListAdapter(getContext(),msgs);
        lv_chat_list.setAdapter(chatListAdapter);

        myRef.child(user_uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                addMessage(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_Msg();
                et_msg.setText("");
            }
        });
        return rootView;
    }

    private void addMessage(DataSnapshot dataSnapshot) {
        Msg chatDTO = dataSnapshot.getValue(Msg.class);
        msgs.add(chatDTO);
        chatListAdapter.notifyDataSetChanged();
    }

    private void send_Msg(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = mFormat.format(date);

        Msg msg= new Msg("admin",et_msg.getText().toString(),getTime);
        myRef.child(user_uid).push().setValue(msg);
    }
}
