package com.example.reservenew.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reservenew.Model.Reservation;
import com.example.reservenew.Model.Room;
import com.example.reservenew.Model.User;
import com.example.reservenew.R;
import com.example.reservenew.Utils.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeFragment extends Fragment {
    final String TAG = "HomeFragment";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Lock");

    private TextView tv_type;
    private TextView tv_room_num;
    private TextView tv_name;
    private TextView tv_check_in_date;
    private TextView tv_check_out_date;
    private TextView tv_check_in_time;
    private TextView tv_check_out_time;

    private TextView tv_head_count;
    private TextView tv_capacity;
    private Button btn_add_person;
    private Button btn_lock;

    private LinearLayout ll_hotel_list;

    private LinearLayout ll_home_exist1;
    private LinearLayout ll_home_exist2;

    private TextView tv_none1;
    private TextView tv_none2;

    private User login_user = new User();
    private Reservation reservation = new Reservation();
    private Room room = new Room();


    private boolean is_locked = false;

    // update -- 예약 정보의 ID 값을 저장하는 변수
    private String reservation_id = null;

    // update -- 체크인/아웃 날짜를 저장하는 변수
    private Date check_in = null;
    private Date check_out = null;

    // update -- 현재 유저의 예약 정보
    private Reservation user_reservation = new Reservation();

    // update -- 시간 데이터 파싱을 위한 객체
    private SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // GPS
    // update -- 현재 위치 위도 경도를 가져오기 위한 client
    FusedLocationProviderClient client;

    // 위치 정보 권한 코드 
    // permissionLauncher 로 권한 확인
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private ActivityResultLauncher<String> permissionLauncher = null;

    // 위치 정보 저장을 위한 변수
    private Location room_location = new Location("Hotel Room Location");
    private Location user_location = new Location("Current User Location");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        ll_hotel_list = rootView.findViewById(R.id.lv_home_room_list);
        tv_type = (TextView) rootView.findViewById(R.id.tv_home_type);
        tv_room_num = (TextView) rootView.findViewById(R.id.tv_home_room_number);
        tv_name = (TextView) rootView.findViewById(R.id.tv_home_room_name);
        tv_check_in_time = (TextView) rootView.findViewById(R.id.tv_home_room_check_in_time);
        tv_check_out_time = (TextView) rootView.findViewById(R.id.tv_home_room_check_out_time);
        tv_check_in_date = (TextView) rootView.findViewById(R.id.tv_home_room_check_in_date);
        tv_check_out_date = (TextView) rootView.findViewById(R.id.tv_home_room_check_out_date);

        tv_head_count = (TextView) rootView.findViewById(R.id.tv_home_head_count);
        tv_capacity = (TextView) rootView.findViewById(R.id.tv_home_capacity);
        btn_add_person = (Button) rootView.findViewById(R.id.btn_home_add_person);
        btn_lock = (Button) rootView.findViewById(R.id.btn_home_lock);

        ll_home_exist1 = (LinearLayout) rootView.findViewById(R.id.ll_home_exist1);
        ll_home_exist2 = (LinearLayout) rootView.findViewById(R.id.ll_home_exist2);
        tv_none1 = (TextView) rootView.findViewById(R.id.tv_home_none1);
        tv_none2 = (TextView) rootView.findViewById(R.id.tv_home_none2);

        // GPS
        // update -- location client 초기화
        client = LocationServices
                .getFusedLocationProviderClient(
                        getActivity());

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // When permission is granted
                        // Call method
                        getCurrentLocation();
                    } else {
                        // When permission is denied
                        // Display toast
                        Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
    );



        ll_hotel_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                HotelListFragment roomListFragment = new HotelListFragment();
                transaction.replace(R.id.menu_frame_layout, roomListFragment);
                transaction.commit();
            }
        });


        btn_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 체크인, 아웃 시간 가져오기 -- DB 값 변경 결과 반영
                set_check_in_out_date();

                // 현재 시간
                Date now = new Date();

                if (check_in.after(now)) {
                    // 체크인 시간이 현재 시간보다 이후인 경우, 즉 현재 시간이 체크인 이전인 경우
                    // 체크인 시간 이전인 경우
                    Util.showMsg(getContext(), "체크인 시간 이후에 제어 가능합니다.");
                } else if (check_out.before(now)) {
                    // 체크아웃 시간이 현재 시간 이전인 경우, 즉 현재 시간이 체크아웃 시간의 이후인 경우
                    // 체크아웃 시간이 지난 경우
                    Util.showMsg(getContext(), "체크아웃 시간이 지나 \n 예약이 취소되었습니다.");

                    // 예약 정보를 삭제하고 방의 head_count 와 is_available 값을 초기화하여 비예약 상태로 만들기
                    db.collection("Reservation").document(reservation_id)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    db.collection("Room").document(room.getId())
                                            .update("head_count", "0")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, room.getId() + " 's headcount is successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error updating document", e);
                                                }
                                            });

                                    db.collection("Room").document(room.getId())
                                            .update("is_available", "true")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, room.getId() + " 's available is successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error updating document", e);
                                                }
                                            });

                                    // 사용자 메인 화면으로 트랜잭션 이동
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    HomeFragment homeFragment = new HomeFragment();
                                    transaction.replace(R.id.menu_frame_layout, homeFragment);
                                    transaction.commit();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });

                } else {
                    // 체크인 시간 이후, 체크아웃 시간 이전인 경우
                    // 도어락 제어가 가능한 시간
                    // 위치 확인 코드 --> 위치가 일치하는 경우에만 도어락 제어!

                    // 권한 확인
                    if (checkLocationPermission()) {
                        // 권한이 승인된 경우
                        // Call method
                        getCurrentLocation();

                        if (user_location.distanceTo(room_location) <= 10){
                            // 거리가 10m 이하인 경우

                            Log.d(TAG, "위도: "+ String.valueOf(user_location.getLatitude()) + "경도: " + String.valueOf(user_location.getLongitude()));

                            // 문을 제어하는 코드
                            if (is_locked) {
                                myRef.child(room.getRoom_number()).child("lock").setValue("1");
                                is_locked = false;
                                btn_lock.setText("문 닫기");
                            } else {
                                myRef.child(room.getRoom_number()).child("lock").setValue("0");
                                is_locked = true;
                                btn_lock.setText("문 열기");
                            }
                        }
                        else{
                            Util.showMsg(getContext(), "객실 가까이로 이동해주세요.");
                        }
                    } else {
                        // 권한이 승인되지 않은 경우
                        // Request permissions
                        requestLocationPermission();
                    }

                }
            }
        });

        btn_add_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // 넘겨주는 예약 정보를 room 에 해당하는 예약 정보와 일치시켜야 함
                UserAddFragment addUserFragment = new UserAddFragment(user_reservation);
                transaction.replace(R.id.menu_frame_layout, addUserFragment);
                transaction.commit();
            }
        });

        get_user();

        return rootView;
    }

    private void get_user() {
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
                                // 만약 로그인한 아이디 와 현재 순회중인 document의 email값이 같다면,
                                if (document.getString("id").equals(user.getUid())) {
                                    login_user.readSnapShot(document);
                                    tv_name.setText(login_user.getName());
                                    get_reservation();
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void get_reservation() {
        // 콜렉션 선택
        db.collection("Reservation")
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
                                reservation.readSnapShot(document);
                                boolean flag = false;
                                if (!reservation.getAdd_user().equals("")) {
                                    String str = reservation.getAdd_user();
                                    String[] arr = str.split(",");
                                    for (String uid : arr) {
                                        if (uid.equals(user.getUid())) {
                                            flag = true;
                                        }
                                    }
                                }
                                if (document.getString("uid").equals(user.getUid()) || flag) {
                                    btn_lock.setVisibility(View.VISIBLE);
                                    ll_home_exist1.setVisibility(View.VISIBLE);
                                    ll_home_exist2.setVisibility(View.VISIBLE);
                                    tv_none1.setVisibility(View.GONE);
                                    tv_none2.setVisibility(View.GONE);

                                    // 숙소 예약이 있으면 숙소 조회 버튼 visible => gone
                                    ll_hotel_list.setVisibility(View.GONE);

                                    tv_type.setText(reservation.getType());
                                    tv_room_num.setText(reservation.getRoom_number());
                                    tv_check_in_time.setText(reservation.getCheck_in_time());
                                    tv_check_out_time.setText(reservation.getCheck_out_time());
                                    tv_check_in_date.setText(reservation.getCheck_in_date());
                                    tv_check_out_date.setText(reservation.getCheck_out_date());
                                    get_room(reservation.getRoom_id());

                                    // update -- 일치하는 예약정보를 user_reservation 에 할당
                                    user_reservation.readSnapShot(document);
                                    get_reservation_id(user_reservation);

                                    String check_in_date = document.getString("check_in_date");
                                    String check_in_time = document.getString("check_in_time");

                                    String check_out_date = document.getString("check_out_date");
                                    String check_out_time = document.getString("check_out_time");

                                    // 체크인 시간, 날짜 파싱해서 check_in 에 Date 객체로 입력
                                    try {
                                        check_in = timeformat.parse(check_in_date + " " + check_in_time);
                                    } catch (ParseException e) {
                                        Log.v("Error parsing time", check_in_date + " " + check_in_time);
                                    }

                                    // 체크아웃 시간, 날짜 파싱해서 check_out 에 Date 객체로 입력
                                    try {
                                        check_out = timeformat.parse(check_out_date + " " + check_out_time);
                                    } catch (ParseException e) {
                                        Log.v("Error parsing time", check_in_date + " " + check_in_time);
                                    }

                                    // GPS
                                    // update -- 방 번호에 해당하는 위치 정보를 입력하기
                                    db.collection("Location").document(reservation.getRoom_number())
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();

                                                        // 문서에서 위도 경도 값 읽어와서 double 로 변환
                                                        double latitude = Double.parseDouble(document.getString("latitude"));
                                                        double longitude = Double.parseDouble(document.getString("longitude"));

                                                        // 위도 경도 값을 room_location 의 Location 객체에 세팅
                                                        room_location.setLatitude(latitude);
                                                        room_location.setLongitude(longitude);

                                                    } else {
                                                        Log.d(TAG, "get failed with ", task.getException());
                                                    }
                                                }
                                            });


                                    // 사전 도어락 UI 세팅
                                    myRef.child(reservation.getRoom_number()).child("lock").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String value = snapshot.getValue(String.class);
                                            Log.d("[REALTIME]", value);
                                            if (value.equals("0")) {
                                                is_locked = true;
                                            } else {
                                                is_locked = false;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    // update -- 예약 정보 doc 의 ID 를 reservation_id 에 가져옴
    private void get_reservation_id(Reservation user_reservation) {
        db.collection("Reservation")
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
                                Reservation reservationDoc = new Reservation();
                                reservationDoc.readSnapShot(document);
                                if (user_reservation.getRoom_id().equals(reservationDoc.getRoom_id())) {
                                    reservation_id = document.getId();
                                    Log.v("reservationID", reservation_id);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    // update -- 체크인, 아웃 일자 가져와서 할당하기
    private void set_check_in_out_date() {
        db.collection("Reservation").document(reservation_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            // update -- 가져온 예약 정보에서 체크인 날짜, 시간을 얻어옴
                            String check_in_date = document.getString("check_in_date");
                            String check_in_time = document.getString("check_in_time");

                            String check_out_date = document.getString("check_out_date");
                            String check_out_time = document.getString("check_out_time");

                            // 체크인 시간, 날짜 파싱해서 check_in 에 Date 객체로 입력
                            try {
                                check_in = timeformat.parse(check_in_date + " " + check_in_time);
                            } catch (ParseException e) {
                                Log.v("Error parsing time", check_in_date + " " + check_in_time);
                            }

                            // 체크아웃 시간, 날짜 파싱해서 check_out 에 Date 객체로 입력
                            try {
                                check_out = timeformat.parse(check_out_date + " " + check_out_time);
                            } catch (ParseException e) {
                                Log.v("Error parsing time", check_in_date + " " + check_in_time);
                            }

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }


    private void get_room(String room_id) {
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
                                if (document.getString("id").equals(room_id)) {
                                    room.readSnapShot(document);
                                    tv_head_count.setText(room.getHeaad_count());
                                    tv_capacity.setText(room.getCapacity());
                                    if (room.getHeaad_count().equals(room.getCapacity())) {
                                        btn_add_person.setEnabled(false);
                                    }
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    // GPS
    // update --

    // 위치 정보 권한 확인 후 결과 반환
    private boolean checkLocationPermission() {
        int permissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    // 권한 요청
    private void requestLocationPermission() {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    // 현재 위치 정보를 가져옴
    private void getCurrentLocation() {
        // locationManage 초기화
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // 위치 서비스 사용 가능한지 확인
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // 위치 서비스 사용가능할 때
            // 권한이 허용되었는지 확인
            if (checkLocationPermission()) {
                // 마지막으로 알려진 위치 가져오기
                client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        // 위치 초기화
                        Location location = task.getResult();
                        // 조건 확인
                        if (location != null) {
                            // location 값이 null 이 아닐 때
                            // 위도와 경도 세팅

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // location 의 위치 정보를 user_location 에 세팅
                            user_location.setLatitude(latitude);
                            user_location.setLongitude(longitude);

                        } else {
                            // location 값이 null 일 때
                            // 위치 정보 요청 초기화
                            LocationRequest locationRequest = new LocationRequest()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setFastestInterval(1000)
                                    .setNumUpdates(1);

                            // location callback 초기화
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    // location1 으로 위치정보 초기화
                                    Location location1 = locationResult.getLastLocation();
                                    // 위도 경도 가져오기
                                    double latitude = location1.getLatitude();
                                    double longitude = location1.getLongitude();

                                    // location 의 위치 정보를 user_location 에 세팅
                                    user_location.setLatitude(latitude);
                                    user_location.setLongitude(longitude);
                                }
                            };

                            // 위치 정보 업데이트 요청
                            client.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    }
                });
            } else {
                // 권한 승인이 안 된 경우
                // 권한 요청
                requestLocationPermission();
            }
        } else {
            // 위치 서비스 사용 불가능한 경우
            // gps 설정 열기
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }


}
