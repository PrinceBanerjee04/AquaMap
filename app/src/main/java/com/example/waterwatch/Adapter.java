package com.example.waterwatch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyHolder> {

    List<PostData> data;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String myuid;
    FirebaseAuth firebaseAuth;
//    int likecnt=0;
    boolean mprocesslike = false;
    private DatabaseReference liekeref, postref;
    Context context;
    public Adapter(Context context, List<PostData> data) {
        this.context=context;
        this.data=data;
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        postref = FirebaseDatabase.getInstance().getReference().child("Posts");
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter,parent,false);
        return new MyHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        final String postid = data.get(position).getTime();
        holder.username.setText(data.get(position).getUsername());
        long dateInMillis=Long.parseLong(data.get(position).getTime());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy | hh:mm:ss");
        String dateString = formatter.format(new Date(dateInMillis));
        holder.date.setText(dateString);
        holder.posttitle.setText(data.get(position).getTitleP());
        holder.postdescription.setText(data.get(position).getDesP());
        String profileUrl = data.get(position).getProfile();
        Glide.with(holder.itemView.getContext()).load(profileUrl).into(holder.profilepic);
        String postUrl = data.get(position).getPicP();
        Glide.with(holder.itemView.getContext()).load(postUrl).into(holder.postpic);
        String pid=data.get(position).getTime();

        holder.shareb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, "Share by...");
                context.startActivity(shareIntent);

            }
        });

        holder.likeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprocesslike=true;
                final String postid = data.get(position).getTime();

                liekeref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final int[] likeCnt = new int[1];
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).child("plike");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                likeCnt[0] = Integer.parseInt((String) snapshot.getValue());
                                Toast.makeText(context.getApplicationContext(), likeCnt[0]+"", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(context.getApplicationContext(), likeCnt[0]+"", Toast.LENGTH_SHORT).show();


                        if(mprocesslike) {
                            if (dataSnapshot.child(postid).hasChild(myuid)) {
                                liekeref.child(postid).child(myuid).removeValue();
                                holder.likeb.setImageResource(R.drawable.like_void);
                                likeCnt[0] -=1;
                                Toast.makeText(context.getApplicationContext(), likeCnt[0]+"", Toast.LENGTH_SHORT).show();

                            } else {
                                liekeref.child(postid).child(myuid).setValue("Liked");
                                holder.likeb.setImageResource(R.drawable.like_fill);
                                likeCnt[0] +=1;
                                Toast.makeText(context.getApplicationContext(), likeCnt[0]+"", Toast.LENGTH_SHORT).show();


                            }
                            updateLikes(likeCnt[0],postid);
                            mprocesslike=false;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void updateLikes(int likeCnt,String postId) {
        String likeCount=likeCnt+"";
        Task<Void> databaseLikeCnt=FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).child("plike").setValue(likeCount);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        ImageView profilepic,postpic;
        ImageView shareb;
        ImageView likeb;

        TextView username,date,posttitle,postdescription,likeCount,commentCount;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profilepic=itemView.findViewById(R.id.profile_picture);
            postpic=itemView.findViewById(R.id.postimage);
            likeb=itemView.findViewById(R.id.likeButton);
            shareb=itemView.findViewById(R.id.sharebutton);
            likeCount=itemView.findViewById(R.id.likeCount);
            username=itemView.findViewById(R.id.uname);
            date=itemView.findViewById(R.id.timestamp);
            posttitle=itemView.findViewById(R.id.posttitle);
            postdescription=itemView.findViewById(R.id.postdescription);
            recyclerView=itemView.findViewById(R.id.recycleView);



        }
    }
}
