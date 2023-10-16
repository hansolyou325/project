package com.example.reservenew.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.reservenew.Fragment.ChatFragment;
import com.example.reservenew.Fragment.FaqFragment;
import com.example.reservenew.Fragment.HomeFragment;
import com.example.reservenew.Fragment.PaymentFragment;
import com.example.reservenew.Fragment.ProfileFragment;
import com.example.reservenew.Fragment.ReserveFragment;
import com.example.reservenew.Fragment.HotelListFragment;
import com.example.reservenew.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private ReserveFragment reserveFragment = new ReserveFragment();
    private ChatFragment    chatFragment    = new ChatFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    private FaqFragment faqFragment = new FaqFragment();

    // MainFragment
    private HomeFragment        homeFragment        = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, homeFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());



    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.item_home:
                    transaction.replace(R.id.menu_frame_layout, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.item_reserve:
                    transaction.replace(R.id.menu_frame_layout, reserveFragment).commitAllowingStateLoss();
                    break;
                case R.id.item_chat:
                    transaction.replace(R.id.menu_frame_layout, chatFragment).commitAllowingStateLoss();
                    break;
                case R.id.item_profile:
                    transaction.replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();
                    break;
                case R.id.item_faq:
                    transaction.replace(R.id.menu_frame_layout, faqFragment).commitAllowingStateLoss();
                    break;
            }

            return true;
        }
    }

}