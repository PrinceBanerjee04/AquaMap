package com.example.waterwatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapFragment extends Fragment {
    private GoogleMap mMap;
    private GpsTracker gpsTracker;
    private LocationRequest locationRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        try {
            if (ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String uid=firebaseUser.getUid();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Posts");
        //   double[] store = getLocation(view);
        // Async map
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // When map is loaded
                mMap = googleMap;
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            String name=dataSnapshot.child("uname").getValue().toString();
                            String title=dataSnapshot.child("title").getValue().toString();
                            String likecnt=dataSnapshot.child("plike").getValue().toString();
                            String time=dataSnapshot.child("ptime").getValue().toString();
                            String latitude=dataSnapshot.child("latitude").getValue().toString();
                            String longitude=dataSnapshot.child("longitude").getValue().toString();
                            String region="Bardhamman";
                            createMarker(mMap,name,title,likecnt,time,latitude,longitude,region);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // Declare a LocationRequest object as a class variable
            }
        });
        // Return view
        return view;
    }
    public void createMarker(GoogleMap googleMap,String name, String title,String likes,String ptime,String lat,String lon,String reg){
        // Add a marker to the map
        double store[]={Double.parseDouble(lat),Double.parseDouble(lon)};
        long dateInMillis=Long.parseLong(ptime);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy | hh:mm:ss");
        String dateString = formatter.format(new Date(dateInMillis));
        LatLng markerLatLng = new LatLng(store[0],store[1]); // Replace with your desired coordinates
        MarkerOptions markerOptions = new MarkerOptions()
                .position(markerLatLng)
                .title("Name:"+name+" | Region:"+reg+"\n"+"Posted On:"+dateString)
                .snippet("Title:"+title+"\nLatitude:"+lat+" | Longitude:"+lon+"\nLikes:"+likes);

        Marker marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 15));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // When clicked on map
                // Initialize marker options
                MarkerOptions markerOptions=new MarkerOptions();
                // Set position of marker
                markerOptions.position(latLng);
                // Set title of marker
                markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                // Remove all marker
                googleMap.clear();
                // Animating to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                // Add marker on map
                googleMap.addMarker(markerOptions);
            }
        });
    }
    public double[] getLocation(View view){
        gpsTracker = new GpsTracker(getContext());
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

}