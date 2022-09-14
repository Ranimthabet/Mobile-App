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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Avis extends AppCompatActivity {
ImageView addAvis;
TextView drirection,moyenne;
RecyclerView recyclerView ;
String testDestination,userUID,userimage,username;
FirebaseRecyclerOptions<MyAvis>options;
FirebaseRecyclerAdapter<MyAvis,MyAvisViewHolder>adapter;
DatabaseReference avisRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avis);
        testDestination = getIntent().getStringExtra("Direction");
        userUID = getIntent().getStringExtra("useruid");
        userimage = getIntent().getStringExtra("userimageuri");
        username = getIntent().getStringExtra("username");
        avisRef = FirebaseDatabase.getInstance().getReference().child("Avis").child(testDestination);
        addAvis = findViewById(R.id.addAvis);
        drirection = findViewById(R.id.region_avis);
        moyenne = findViewById(R.id.region_moyen);
        drirection.setText(testDestination);
        recyclerView = findViewById(R.id.avis_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFitsSystemWindows(true);
        addAvis.setColorFilter(Color.BLUE);

        addAvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userUID.equals( "Visiteur")){
                    startActivity(new Intent(getApplicationContext(),AddUserRequest.class));

                }else {
                    Intent intent = new Intent(getApplicationContext(), AddAvis.class);
                    intent.putExtra("useruid", userUID);
                    intent.putExtra("Direction", testDestination);
                    startActivity(intent);
                }}
        });
        LoadAvis();

    }

    private void LoadAvis() {
        options = new FirebaseRecyclerOptions.Builder<MyAvis>().setQuery(avisRef,MyAvis.class).build();
        adapter = new FirebaseRecyclerAdapter<MyAvis, MyAvisViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyAvisViewHolder holder, int position, @NonNull MyAvis model) {
                String avisKey = getRef(position).getKey();
                String userkey = model.getAvis_useruid();
                holder.avis_username.setText(model.getAvis_username());
                holder.avis_time.setText(model.getAvis_time());
                holder.avis_text.setText(model.getAvis_text());
                Picasso.get().load(model.getAvis_userimage()).into(holder.avis_userimage);
                if(userUID.equals(userkey)) {
                    holder.option.setVisibility(View.VISIBLE);
                    holder.option.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.btn_delete.setVisibility(View.VISIBLE);
                            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DeleteAvis(avisKey, testDestination, userUID);
                                }
                            });

                        }
                    });
                }


            }

            @NonNull
            @Override
            public MyAvisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_avis_layout,parent,false);

                return new MyAvisViewHolder(view);
            }
        };
        adapter.startListening();
recyclerView.setAdapter(adapter);
    }

    private void DeleteAvis(String avisKey, String testDestination, String userUID) {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Avis").child(testDestination);
        myRef.child(avisKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               String testUID  = snapshot.child("avis_useruid").getValue().toString();
               if(testUID.equals(userUID)){
                   myRef.child(avisKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Toast.makeText(Avis.this, "Opreation succeeded", Toast.LENGTH_SHORT).show();

                       }
                   });
               }
               else{
                   Toast.makeText(Avis.this, "you can't delete this one", Toast.LENGTH_SHORT).show();
               }
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}