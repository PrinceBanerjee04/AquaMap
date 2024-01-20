package com.example.waterwatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
public class UserProfile extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView avatartv;
    TextView name, email;
    FloatingActionButton fab;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        // Initialising the text view and imageview
        avatartv = findViewById(R.id.profile_pic);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        fab = findViewById(R.id.fab);
        progressBar=findViewById(R.id.progressBar);
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String nam =""+(dataSnapshot1.child("name").getValue());
                    String emaill = "" + dataSnapshot1.child("email").getValue();
                    String image = "" + dataSnapshot1.child("image").getValue();
                    // setting data to our text view
                    name.setText("Name : "+nam);
                    email.setText("Email id : "+emaill);
                    // Toast.makeText(getContext(), image, Toast.LENGTH_SHORT).show();
                    try {
                        progressBar.setVisibility(View.INVISIBLE);
                        Glide.with(UserProfile.this).load(image).into(avatartv);

                    } catch (Exception e) {
                        Toast.makeText(UserProfile.this, "Error occurred! Try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // On click we will open EditProfile Activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), EditProfilePage.class));

            }
        });
    }
}


