package com.example.waterwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterwatch.DashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    TextView login;
    private GpsTracker gpsTracker;
    private LocationRequest locationRequest;
    EditText name,email,password;
    Button register;
    ProgressBar progressBar;
    double store[];
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Intent i=getIntent();
        name=findViewById(R.id.register_name);
        email=findViewById(R.id.register_email);
        password=findViewById(R.id.register_password);
        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store=getLocation(v);
                String latitude=store[0]+"";
                String longitude=store[1]+"";
                String emailT = email.getText().toString().trim();
                String uname = name.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(emailT).matches()) {
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                } else if (pass.length() < 6) {
                    password.setError("Length Must be greater than 6 character");
                    password.setFocusable(true);
                } else {
                    registerUser(emailT, pass, uname,latitude, longitude);
                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class));
                finish();
            }
        });

    }
    public double[] getLocation(View view){
        gpsTracker = new GpsTracker(view.getContext());
        double[] store =new double[2];
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            store[0]=latitude;
            store[1]=longitude;
            Toast.makeText(view.getContext(), latitude+","+longitude, Toast.LENGTH_SHORT).show();
        }else{
            gpsTracker.showSettingsAlert();
        }
        return store;
    }
    private void registerUser(String email, final String pass, final String uname, final String latitude, final String longitude) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    String email = user.getEmail();
                    String uid = user.getUid();
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("name", uname);
                    hashMap.put("image", "");
                    hashMap.put("latitude",latitude);
                    hashMap.put("longitude",longitude);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uid).setValue(hashMap);
                    Toast.makeText(Signup.this, "Registered User " + user.getEmail(), Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(Signup.this, UploadProfilePic.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    Toast.makeText(Signup.this, "Please update your profile first..", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Signup.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Signup.this, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}