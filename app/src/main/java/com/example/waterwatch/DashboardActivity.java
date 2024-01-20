package com.example.waterwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//        Toast.makeText(this, "If not updated then update profile first.", Toast.LENGTH_SHORT).show();
        navigation = findViewById(R.id.navigation);
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();
        navigation.setOnItemSelectedListener(selectedListener);

    }
    private final BottomNavigationView.OnItemSelectedListener selectedListener=new BottomNavigationView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();
            int home=R.id.home;
            int user=R.id.users;
            int add=R.id.add;
            if(id==home){
                HomeFragment fragment1 = new HomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment1, "");
                fragmentTransaction.commit();
                return true;
            }
            else if(id==user){
                UsersFragment fragment3 = new UsersFragment();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.content, fragment3, "");
                fragmentTransaction2.commit();
                return true;
            }
            else if(id==add){
                AddFragment fragment2 = new AddFragment();
                FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction4.replace(R.id.content, fragment2, "");
                fragmentTransaction4.commit();
                return true;
            }
            Toast.makeText(DashboardActivity.this, "Some Error Occurred! Please Open The App Again.", Toast.LENGTH_SHORT).show();
            return false;
        }
    };
}