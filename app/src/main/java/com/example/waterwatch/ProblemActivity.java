package com.example.waterwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProblemActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        navigation = findViewById(R.id.navigation);
        SubmittedFragment fragment = new SubmittedFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();
        navigation.setOnItemSelectedListener(selectedListener);

    }
    private final BottomNavigationView.OnItemSelectedListener selectedListener=new BottomNavigationView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();
            int submit=R.id.submitted_problems;
            int execute=R.id.executed_problems;
            if(id==submit){
                SubmittedFragment fragment1 = new SubmittedFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment1, "");
                fragmentTransaction.commit();
                return true;
            }
            else if(id==execute){
                ExecutedFragment fragment3 = new ExecutedFragment();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.content, fragment3, "");
                fragmentTransaction2.commit();
                return true;
            }
            Toast.makeText(ProblemActivity.this, "Some Error Occurred! Please Open The App Again.", Toast.LENGTH_SHORT).show();
            return false;
        }
    };
}