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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FeedbackAcceuil extends AppCompatActivity {
    ImageView addFeedback;
    RecyclerView recyclerView ;
    String Cusername,Cuserimage;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser ;
    String myuid;
    FirebaseRecyclerOptions<Myfeedback>options;
    FirebaseRecyclerAdapter<Myfeedback,MyAvisViewHolder>adapter;
    DatabaseReference feedbackRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_acceuil);
        addFeedback = findViewById(R.id.addFeed);
        recyclerView = findViewById(R.id.recycler_feed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFitsSystemWindows(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        myuid = firebaseUser.getUid();
        feedbackRef = FirebaseDatabase.getInstance().getReference().child("Feedback");
        Cusername = getIntent().getStringExtra("username");
        Cuserimage = getIntent().getStringExtra("imageprofileuri");
        addFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedback = new Intent(getApplicationContext(),AddFeedback.class);
                feedback.putExtra("username",Cusername);
                feedback.putExtra("imageprofileuri",Cuserimage);
                Toast.makeText(FeedbackAcceuil.this, "hello"+myuid, Toast.LENGTH_SHORT).show();
                startActivity(feedback);            }
        });
LoadsFeedback();
    }

    private void LoadsFeedback() {
        options = new FirebaseRecyclerOptions.Builder<Myfeedback>().setQuery(feedbackRef,Myfeedback.class).build();
        adapter = new FirebaseRecyclerAdapter<Myfeedback, MyAvisViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyAvisViewHolder holder, int position, @NonNull Myfeedback model) {
                String feedbackkey = getRef(position).getKey();
                String userrefkey = model.getUid();
                holder.avis_username.setText(model.getUsername());
                holder.avis_time.setText(model.getTime());
                holder.avis_text.setText(model.getFeedback());
                Picasso.get().load(model.getUserimage()).into(holder.avis_userimage);
                if(userrefkey.equals(myuid)){
                    holder.option.setVisibility(View.VISIBLE);
                    holder.option.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.btn_delete.setVisibility(View.VISIBLE);
                            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DeleteFeedback(feedbackkey);
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
                return new MyAvisViewHolder(view);            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void DeleteFeedback(String feedbackkey) {
feedbackRef.child(feedbackkey).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
   if(snapshot.exists()){
       feedbackRef.child(feedbackkey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               Toast.makeText(FeedbackAcceuil.this, "You have delete your Feedback", Toast.LENGTH_SHORT).show();
           }
       });
   }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
    }

}