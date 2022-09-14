package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EventReception extends AppCompatActivity {
   private Toolbar toolbar ;
   private RecyclerView recyclerView ;
   private TextView addEvent;
   DatabaseReference eventRef,favorisRef;
   FirebaseAuth firebaseAuth;
   FirebaseUser firebaseUser;
   String username,imageprofileuri,eventkey;
    FirebaseRecyclerAdapter<MyEvent, MyEventViewHolder> adapter;
    FirebaseRecyclerOptions<MyEvent> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_reception);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
        favorisRef = FirebaseDatabase.getInstance().getReference().child("Favoris").child("MyEvents");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("    Events");
        getSupportActionBar().setIcon(R.drawable.ic_events);
        recyclerView = findViewById(R.id.ReceptionEvent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFitsSystemWindows(true);
        addEvent = findViewById(R.id.addEvent);

        username =getIntent().getStringExtra("username");
        imageprofileuri =getIntent().getStringExtra("imageprofileuri");

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplicationContext(),Events.class);
                intent.putExtra("username",username);
                intent.putExtra("imageprofileuri",imageprofileuri);
                startActivity(intent);


            }
        });
        LoadEvents();
    }

    private void LoadEvents() {
        options = new FirebaseRecyclerOptions.Builder<MyEvent>().setQuery(eventRef,MyEvent.class).build();
        adapter= new FirebaseRecyclerAdapter<MyEvent, MyEventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyEventViewHolder holder, int position, @NonNull MyEvent model) {
                eventkey=getRef(position).getKey();
                String userRef = model.getEvent_userimage();
                Picasso.get().load(model.getEvent_userimage()).into(holder.myecent_imageprofile);
                holder.myevent_username.setText(model.getEvent_username());
                holder.myevent_title.setText(model.getEvent_title());
                holder.myevent_local.setText(model.getEvent_location());
                holder.myevent_start.setText(model.getEvent_start());
                holder.eventtime.setText(model.getTime());
/*****************************************************************************************************/
                holder.favoris.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eventkey=getRef(position).getKey();
                        favorisRef.child(eventkey).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){
                                    Toast.makeText(EventReception.this, "Remove from Favoris", Toast.LENGTH_SHORT).show();
                                    favorisRef.child(eventkey).child(firebaseUser.getUid()).removeValue();
                                    holder.favoris.setImageResource(R.drawable.ic_staremty);
                                    notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(EventReception.this, "Event add to Favoris", Toast.LENGTH_SHORT).show();
                                    favorisRef.child(eventkey).child(firebaseUser.getUid()).setValue("Favoris");
                                   holder.favoris.setImageResource(R.drawable.ic_startfill);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
     /**********************************************************************************************************************/
     holder.CountFavoris(eventkey,firebaseUser.getUid(),favorisRef);
     /***********************************************************************************************************/
                holder.myevent_discription.setText("    "+model.getEvent_discription());
                if(imageprofileuri.equals(userRef)){
                    holder.eventOption.setVisibility(View.VISIBLE);
                }
                holder.eventOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.event_delete.setVisibility(View.VISIBLE);
                        holder.event_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DeleteEvents(eventkey,imageprofileuri);

                            }
                        });
                        holder.event_set.setVisibility(View.VISIBLE);
                        holder.event_set.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setEvents(eventkey,imageprofileuri);
                            }
                        });
                    }
                });


            }
            @NonNull
            @Override
            public MyEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_view,parent,false);
                return new MyEventViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
/***************************************************************************/
    private void setEvents(String eventkey, String imageprofileuri) {
        eventRef.child(eventkey).addValueEventListener(new ValueEventListener() {
           String refUri;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    refUri = snapshot.child("event_userimage").getValue().toString();
                    if(imageprofileuri.equals(refUri)){
                        Toast.makeText(EventReception.this, "You can edit this event", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),SetEvents.class);
                        intent.putExtra("eventkey",eventkey);
                        intent.putExtra("userimageprofileRef",imageprofileuri);
                        startActivity(intent);

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "You can't set this Event", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
/******************************************************************/
    private void DeleteEvents(String eventkey, String imageprofileuri) {
        eventRef.child(eventkey).addValueEventListener(new ValueEventListener() {
            String refURi;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    refURi = snapshot.child("event_userimage").getValue().toString();
                    if(imageprofileuri.equals(refURi)){
                        eventRef.child(eventkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "You have delete this post", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "OOOUPs \nYou can't delete this Post" , Toast.LENGTH_SHORT).show();

                    } }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});

    }
    /*************************************************************************/
}