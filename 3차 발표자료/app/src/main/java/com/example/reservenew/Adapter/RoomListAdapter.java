package com.example.reservenew.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.reservenew.Activity.MainActivity;
import com.example.reservenew.Fragment.PaymentFragment;
import com.example.reservenew.Model.Hotel;
import com.example.reservenew.Model.Room;
import com.example.reservenew.R;
import com.example.reservenew.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RoomListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Hotel> hotels;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://reservationmody.appspot.com");

    public RoomListAdapter(Context context, ArrayList<Hotel> hotels){
        this.hotels      = hotels;
        this.context    = context;
    }
    @Override
    public int getCount() {
        return hotels.size();
    }

    @Override
    public Object getItem(int i) {
        return hotels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        LayoutInflater layoutInflatet = LayoutInflater.from(context);
        if (converView == null){
            converView = layoutInflatet.inflate(R.layout.room_list_item, null);
        }

        Button btn_reserve      = converView.findViewById(R.id.btn_room_list_item_reserve);
        TextView tv_type        = converView.findViewById(R.id.tv_room_list_item_type);
        TextView tv_bed         = converView.findViewById(R.id.tv_room_list_item_bed);
        TextView tv_capacity    = converView.findViewById(R.id.tv_room_list_item_capacity);
        TextView tv_price       = converView.findViewById(R.id.tv_room_list_item_price);
        ImageView iv_image      = converView.findViewById(R.id.iv_room_list_item_image);


        Hotel hotel = hotels.get(i);

        tv_type.setText(hotel.getType());
        tv_bed.setText(hotel.getBed());
        tv_capacity.setText(hotel.getCapacity());
        tv_price.setText(hotel.getPrice_per_night());

        StorageReference storageRef = storage.getReference();
        storageRef.child(hotel.getImage_url()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시

                Glide.with(context)
                        .load(uri)
                        .into(iv_image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show();
            }
        });

        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blank_room_search(hotel.getId());
            }
        });




        return converView;
    }

    private void blank_room_search(String hotel_id){
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
                            boolean flag = false;
                            String room_id = "";
                            // 스냅샷을 순회
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Room room = new Room();
                                room.readSnapShot(document);
                                if(room.getHotel_id().equals(hotel_id)){
                                    if(room.getIs_available().equals("true")){
                                        flag = true;
                                        room_id = room.getId();
                                    }
                                }
                            }

                            if(flag){
                                Util.showMsg(context,"이용가능한 객실이 있습니다.\n");
                                FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                                PaymentFragment paymentFragment = new PaymentFragment(hotel_id,room_id);
                                transaction.replace(R.id.menu_frame_layout, paymentFragment);
                                transaction.commit();
                            }else{
                                Util.showMsg(context,"이용가능한 객실이 없습니다.");
                            }
                        } else {
                        }
                    }
                });
    }
}
