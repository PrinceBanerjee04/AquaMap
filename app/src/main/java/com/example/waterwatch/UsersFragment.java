package com.example.waterwatch;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    String name,image,post,title,des,like,comment,share,time,comments;
    RecyclerView recycleView;
    List<PostData> data;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference1,databaseReference2;
    String[] nameStore=null;
    String[] imageStore=null;
    ImageView account;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data=new ArrayList<PostData>();
        View view= inflater.inflate(R.layout.fragment_users, container, false);
        Toast.makeText(view.getContext(), "Users", Toast.LENGTH_SHORT).show();
        account=view.findViewById(R.id.account_button);
        recycleView=view.findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String userid=firebaseUser.getUid();
        DatabaseReference datadp=FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        datadp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dp=snapshot.child("image").getValue().toString();
                Glide.with(UsersFragment.this).load(dp).into(account);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference2=firebaseDatabase.getReference("Posts");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    name=""+dataSnapshot1.child("uname").getValue();
                    image=""+dataSnapshot1.child("udp").getValue();
                    title=""+dataSnapshot1.child("title").getValue();
                    des=""+dataSnapshot1.child("description").getValue();
                    time=""+dataSnapshot1.child("ptime").getValue();
                    like=""+dataSnapshot1.child("plike").getValue();
                    comment=""+dataSnapshot1.child("pcomments").getValue();
                    post=""+dataSnapshot1.child("uimage").getValue();
                    comments=""+dataSnapshot1.child("comment").getValue();
                    //    Toast.makeText(getContext(),comments,  Toast.LENGTH_SHORT).show();
                    PostData newData=new PostData(image,name,"12/22/2056",time,title,des,post,like,comment,"0");
                    data.add(newData);
                    //Toast.makeText(getContext(), data.get(0).toString(), Toast.LENGTH_SHORT).show();
                }
                Adapter ad=new Adapter(view.getContext(),data);
                recycleView.setAdapter(ad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(), UserProfile.class);
                startActivity(i);
            }
        });
        return view;
    }
}