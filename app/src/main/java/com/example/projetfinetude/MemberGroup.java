package com.example.projetfinetude;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;
public class MemberGroup extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView add_member;
    String groupid;
    DatabaseReference userRef,usermemberRef;
  private   FirebaseRecyclerOptions<MyMemeber>moptions;
   private FirebaseRecyclerAdapter<MyMemeber,MyFriendViewHolder>madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_group);
        add_member = findViewById(R.id.addmember);
        groupid = getIntent().getStringExtra("groupid");
        recyclerView = findViewById(R.id.member_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
        usermemberRef = FirebaseDatabase.getInstance().getReference().child("MemberGroup").child(groupid);
        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListMyfriend.class);
                intent.putExtra("groupid", groupid);
                startActivity(intent);
            }
        });
LoadsMember();
    }

    private void LoadsMember() {
        moptions = new FirebaseRecyclerOptions.Builder<MyMemeber>().setQuery(usermemberRef,MyMemeber.class).build();
        madapter = new FirebaseRecyclerAdapter<MyMemeber, MyFriendViewHolder>(moptions) {
            @Override
            protected void onBindViewHolder(@NonNull MyFriendViewHolder holder, int position, @NonNull MyMemeber model) {
                String refkey = getRef(position).getKey();
                usermemberRef.child(refkey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            userRef.child(refkey).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String cname = snapshot.child("name").getValue().toString()+" "+snapshot.child("lastName").getValue().toString();
                                        String cemail = snapshot.child("email").getValue().toString();
                                        String cuseruri = snapshot.child("imageuri").getValue().toString();


                                        Picasso.get().load(cuseruri).into(holder.my_friend_imageprofile);
                                        holder.my_friend_username.setText(cname);
                                        holder.my_friend_useremail.setText(cemail);


                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public MyFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_myfriend_view,parent,false);
                return new MyFriendViewHolder(view);
            }
        };
        madapter.startListening();
        recyclerView.setAdapter(madapter);


}}

