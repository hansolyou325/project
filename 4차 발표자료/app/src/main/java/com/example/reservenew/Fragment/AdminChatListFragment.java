package com.example.reservenew.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.reservenew.Adapter.AdminChatRoomListAdapter;
import com.example.reservenew.Model.ChatRoom;
import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminChatListFragment extends Fragment {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ListView lv_chat_list;
    private ArrayList<ChatRoom> chatRooms;
    private ArrayList<User> userArrayList;
    private AdminChatRoomListAdapter adminChatRoomListAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Message");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_admin_chat_list, container, false);

        lv_chat_list = (ListView) rootView.findViewById(R.id.lv_admin_chat_list_list);

        chatRooms = new ArrayList<ChatRoom>();
        adminChatRoomListAdapter = new AdminChatRoomListAdapter(getContext(),chatRooms);
        lv_chat_list.setAdapter(adminChatRoomListAdapter);

        get_user();
        return rootView;
    }

    /*private void get_chatroom(){
        // 아이디 받아옴 key값
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("[DATA]",snapshot.getKey());
                if(chatRooms.size() == 0){
                    Msg chatDTO = new Msg();
                    for(DataSnapshot innerSnapshot :snapshot.getChildren()){
                        chatDTO = snapshot.getValue(Msg.class);
                    }

                    ChatRoom chatRoom = new ChatRoom(chatDTO.getUser(),chatDTO.getMsg());
                    chatRooms.add(chatRoom);
                    adminChatListAdapter.notifyDataSetChanged();

                }else{
                    Msg chatDTO = new Msg();
                    for(DataSnapshot innerSnapshot :snapshot.getChildren()){
                        Msg tempMsg = snapshot.getValue(Msg.class);
                        if(!tempMsg.getUser().equals("admin")){
                            chatDTO = snapshot.getValue(Msg.class);
                        }
                    }
                    boolean flag = false;
                    for(ChatRoom chatRoom : chatRooms){
                        if(chatRoom.getUser_name().equals(chatDTO.getUser())){
                            flag = true;
                            chatRoom.setUser_name(chatDTO.getUser());
                            chatRoom.setUser_name(chatDTO.getMsg());
                            adminChatListAdapter.notifyDataSetChanged();
                        }
                    }

                    if(!flag){
                        ChatRoom chatRoom = new ChatRoom(chatDTO.getUser(),chatDTO.getMsg());
                        chatRooms.add(chatRoom);
                        adminChatListAdapter.notifyDataSetChanged();
                    }
                }
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
    }
*/
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
                                User login_user = new User();
                                login_user.readSnapShot(document);
                                ChatRoom chatRoom = new ChatRoom(login_user.getName(), login_user.getUid(),"");
                                if(!login_user.getGrade().equals("admin")){
                                    chatRooms.add(chatRoom);
                                    adminChatRoomListAdapter.notifyDataSetChanged();
                                }

                            }
                            //get_chatroom();
                        } else {

                        }
                    }
                });
    }

}
