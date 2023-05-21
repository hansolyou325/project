package com.example.reservenew.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reservenew.Activity.AdminActivity;
import com.example.reservenew.Adapter.AdminStateListAdapter;
import com.example.reservenew.Adapter.ChatListAdapter;
import com.example.reservenew.Model.Msg;
import com.example.reservenew.Model.Reservation;
import com.example.reservenew.Model.Room;
import com.example.reservenew.R;
import com.example.reservenew.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminRoomStateFragment extends Fragment {
    final String TAG = "AdminRoomStateFragment";

    private Dialog room_dialog;
    private Dialog type_dialog;
    private ViewGroup rootView;
    private LinearLayout ll_type;
    private LinearLayout ll_room_num;
    private TextView tv_type;
    private TextView tv_room_num;
    private Button btn_lock;
    private ListView lv_empty;
    private ListView lv_occupied;

    private FirebaseAuth firebaseAuth   = FirebaseAuth.getInstance();
    private FirebaseUser user           = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db        = FirebaseFirestore.getInstance();


    private FirebaseDatabase database   = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Lock");

    private ArrayList<String> standards     = new ArrayList<String>();
    private ArrayList<String> superiors     = new ArrayList<String>();
    private ArrayList<String> deluxes       = new ArrayList<String>();
    private ArrayList<String> executives    = new ArrayList<String>();
    private ArrayList<String> room_numList  = new ArrayList<String>();
    private ArrayList<Room> empty_roomList = new ArrayList<Room>();
    private ArrayList<Room> occupied_roomList = new ArrayList<Room>();

    private AdminStateListAdapter empty_adapter;
    private AdminStateListAdapter occupied_adapter;

    private boolean lock = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_admin_room_state, container, false);

        ll_type         = rootView.findViewById(R.id.ll_admin_room_state_room_type);
        ll_room_num     = rootView.findViewById(R.id.ll_admin_room_state_room_number);
        tv_type         = rootView.findViewById(R.id.tv_admin_room_state_room_type);
        tv_room_num     = rootView.findViewById(R.id.tv_admin_room_state_room_number);
        btn_lock        = rootView.findViewById(R.id.btn_admin_room_state_lock);
        lv_empty        = rootView.findViewById(R.id.lv_admin_room_state_empty);
        lv_occupied     = rootView.findViewById(R.id.lv_admin_room_state_occupied);

        empty_adapter = new AdminStateListAdapter(getContext(), empty_roomList);
        occupied_adapter = new AdminStateListAdapter(getContext(), occupied_roomList);

        lv_empty.setAdapter(empty_adapter);
        lv_occupied.setAdapter(occupied_adapter);


        room_dialog = new Dialog(rootView.getContext());
        type_dialog = new Dialog(rootView.getContext());

        type_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        room_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        type_dialog.setContentView(R.layout.simple_dialog);
        room_dialog.setContentView(R.layout.simple_dialog);

        ll_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoomTypeDialog();
            }
        });

        ll_room_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoomNumDialog();
            }
        });

        btn_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tv_type.getText().toString().equals("")){
                    if(!tv_room_num.getText().toString().equals("")){
                        if (lock){
                            lock = false;
                            myRef.child(tv_room_num.getText().toString()).child("lock").setValue("0");
                            btn_lock.setText("문열기");
                        }else{
                            lock = true;
                            myRef.child(tv_room_num.getText().toString()).child("lock").setValue("1");
                            btn_lock.setText("문닫기");
                        }

                    }else{
                        Util.showMsg(getContext(),"룸 번호를 선택해 주세요.");
                    }
                }else{
                    Util.showMsg(getContext(),"룸 타입을 선택해 주세요.");
                }
            }
        });



        get_room();

        return rootView;
    }

    private void showRoomTypeDialog(){
        ListView listView = type_dialog.findViewById(R.id.lv_simple_dialog_list);

        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        list.add("Standard");
        list.add("Superior");
        list.add("Deluxe");
        list.add("Executive");
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clicked_item = list.get(i);
                Log.d("[TYPE_DIALOG_CLICKED]", clicked_item);
                tv_type.setText(clicked_item);
                tv_room_num.setText("");
                type_dialog.dismiss();
            }
        });

        type_dialog.show();
    }

    private void showRoomNumDialog(){
        ListView listView = type_dialog.findViewById(R.id.lv_simple_dialog_list);

        room_numList.clear();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.simple_list_item_1, room_numList);
        listView.setAdapter(adapter);

        switch(tv_type.getText().toString()){
            case "Standard":
                for (String str : standards){
                    room_numList.add(str);
                }
                break;
            case "Superior":
                for (String str : superiors){
                    room_numList.add(str);
                }
                break;
            case "Deluxe":
                for (String str : deluxes){
                    room_numList.add(str);
                }
                break;
            case "Executive":
                for (String str : executives){
                    room_numList.add(str);
                }
                break;
        }
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clicked_item = room_numList.get(i);
                Log.d("[TYPE_DIALOG_CLICKED]", clicked_item);
                tv_room_num.setText(clicked_item);

                myRef.child(tv_room_num.getText().toString()).child("lock").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String val = snapshot.getValue(String.class);
                        if(val.equals("0")){
                            lock = false;
                            btn_lock.setText("문열기");
                        }else{
                            lock = true;
                            btn_lock.setText("문닫기");
                        }
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
                type_dialog.dismiss();
            }
        });
        type_dialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
            transaction.replace(R.id.menu_frame_layout, adminHomeFragment);
            transaction.commit();
        }
    };

    private void get_room(){
        // 콜렉션 선택
        db.collection("Room")
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
                                Room room = new Room();
                                room.readSnapShot(document);

                                if(room.getIs_available().equals("false")){
                                    empty_roomList.add(room);
                                    empty_adapter.notifyDataSetChanged();
                                }else{
                                    occupied_roomList.add(room);
                                    occupied_adapter.notifyDataSetChanged();
                                }

                                switch(room.getType()){
                                    case "Standard":
                                        standards.add(room.getRoom_number());
                                        break;
                                    case "Superior":
                                        superiors.add(room.getRoom_number());
                                        break;
                                    case "Deluxe":
                                        deluxes.add(room.getRoom_number());
                                        break;
                                    case "Executive":
                                        executives.add(room.getRoom_number());
                                        break;
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
