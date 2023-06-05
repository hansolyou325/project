package com.example.reservenew.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.reservenew.Fragment.AdminChatListFragment;
import com.example.reservenew.Fragment.AdminHomeFragment;
import com.example.reservenew.Fragment.ChatFragment;
import com.example.reservenew.Fragment.HomeFragment;
import com.example.reservenew.Fragment.ProfileFragment;
import com.example.reservenew.Fragment.ReserveFragment;
import com.example.reservenew.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private AdminChatListFragment adminChatListFragment    = new AdminChatListFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    // MainFragment
    private AdminHomeFragment adminHomeFragment        = new AdminHomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, adminHomeFragment).commitAllowingStateLoss();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        bottomNavigationView.getMenu().findItem(R.id.item_home).setChecked(true);
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.item_home:
                    transaction.replace(R.id.menu_frame_layout, adminHomeFragment).commitAllowingStateLoss();
                    break;
                case R.id.item_chat:
                    transaction.replace(R.id.menu_frame_layout, adminChatListFragment).commitAllowingStateLoss();
                    break;
                case R.id.item_profile:
                    transaction.replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();
                    break;

            }

            return true;
        }
    }
}