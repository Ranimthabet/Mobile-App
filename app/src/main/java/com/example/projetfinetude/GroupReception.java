package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class GroupReception extends AppCompatActivity {
ImageView searchgroup,addgroup;
RecyclerView recyclerView;
DatabaseReference groupRef,myGroupsRef,usergroupRef ;
FirebaseRecyclerOptions<MyGroup>options;
FirebaseRecyclerAdapter<MyGroup,FindFriendViewHolder>adapter;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_reception);
        searchgroup = findViewById(R.id.serachgrp);
        addgroup = findViewById(R.id.addgrp);
        recyclerView = findViewById(R.id.group_recycler);
        recyclerView.setFitsSystemWindows(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        usergroupRef = FirebaseDatabase.getInstance().getReference().child("UserGroup").child(firebaseUser.getUid());
        myGroupsRef = FirebaseDatabase.getInstance().getReference().child("MemberGroup");
        searchgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SearchGroup.class));
            }
        });
        addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddGroup.class));
            }
        });
        LoadsMyGroups();
    }

   private void LoadsMyGroups() {
        options = new FirebaseRecyclerOptions.Builder<MyGroup>().setQuery(usergroupRef,MyGroup.class).build();
        adapter = new FirebaseRecyclerAdapter<MyGroup, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull MyGroup model) {
                String key =getRef(position).getKey();
                holder.findfriend_username.setText(model.getGroupe_name());
                Picasso.get().load(model.getGroupe_image()).into(holder.findfriend_imageprofile);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),SingleGroup.class);
                        intent.putExtra("groupid",key);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_find_friend,parent,false);
                return new FindFriendViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}