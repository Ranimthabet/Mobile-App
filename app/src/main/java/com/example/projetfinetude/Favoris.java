package com.example.projetfinetude;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;
public class Favoris extends AppCompatActivity {
    CircleImageView profile_image;
    TextView Username,event,randonne;
    View viewevent,viewrandonne;
    RecyclerView recyclerViewEvent,recyclerViewRandonnee;
String Refname,Refimageuri;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser ;
DatabaseReference favorisRef,eventRef,myrandonneeRef,randRef;
FirebaseRecyclerOptions<MyEvent>options;
FirebaseRecyclerAdapter<MyEvent,MyEventViewHolder>adapter;
FirebaseRecyclerOptions<MyRandonnee>randOption;
FirebaseRecyclerAdapter<MyRandonnee,MyRandonneeViewHolder>randAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        profile_image = findViewById(R.id.favoris_imageprofile);
        Username = findViewById(R.id.favoris_username);
        event = findViewById(R.id.favoris_event);
        randonne = findViewById(R.id.favoris_randonnee);
        viewevent = findViewById(R.id.favoris_view1);
        viewrandonne = findViewById(R.id.favoris_view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        recyclerViewRandonnee = findViewById(R.id.recyclerview_randoonee);
      /*  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);*/
        recyclerViewRandonnee.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewRandonnee.setFitsSystemWindows(true);
        recyclerViewEvent = findViewById(R.id.recyclerview_event);
        recyclerViewEvent.setLayoutManager(new LinearLayoutManager(getApplicationContext() ));
        recyclerViewEvent.setFitsSystemWindows(true);
        Refname =getIntent().getStringExtra("username");
        Refimageuri =getIntent().getStringExtra("imageprofileuri");

        favorisRef = FirebaseDatabase.getInstance().getReference().child("Favoris").child("MyEvents");
        myrandonneeRef = FirebaseDatabase.getInstance().getReference().child("Favoris").child("MyRandonnes");
        eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
        randRef = FirebaseDatabase.getInstance().getReference().child("Randonnee");


        Picasso.get().load(Refimageuri).into(profile_image);
        Username.setText(Refname);
        randonne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewevent.setVisibility(View.INVISIBLE);
                viewrandonne.setVisibility(View.VISIBLE);
                event.setTextColor(Color.BLACK);
                randonne.setTextColor(Color.BLUE);
                recyclerViewEvent.setVisibility(View.INVISIBLE);
                recyclerViewRandonnee.setVisibility(View.VISIBLE);
                LoadsMyRandonnee();
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewevent.setVisibility(View.VISIBLE);
                viewrandonne.setVisibility(View.INVISIBLE);
                event.setTextColor(Color.BLUE);
                randonne.setTextColor(Color.BLACK);
                recyclerViewEvent.setVisibility(View.VISIBLE);
                recyclerViewRandonnee.setVisibility(View.INVISIBLE);
                LoadsMyEvents();

            }
        });
    }
/****************************************************************/
    private void LoadsMyRandonnee() {
        randOption = new FirebaseRecyclerOptions.Builder<MyRandonnee>().setQuery(myrandonneeRef,MyRandonnee.class).build();
        randAdapter = new FirebaseRecyclerAdapter<MyRandonnee, MyRandonneeViewHolder>(randOption) {
            @Override
            protected void onBindViewHolder(@NonNull MyRandonneeViewHolder holder, int position, @NonNull MyRandonnee model) {
                String key = getRef(position).getKey();
                randRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String name = snapshot.child("randonne_username").getValue().toString();
                            String time = snapshot.child("randonne_start").getValue().toString();
                            String location = snapshot.child("randonne_location").getValue().toString();
                            String desc = snapshot.child("randonne_discription").getValue().toString();
                            String uri = snapshot.child("randonne_userimage").getValue().toString();
                            String title = snapshot.child("randonne_title").getValue().toString();
                            String timeevent = snapshot.child("time").getValue().toString();

                            holder.myevent_username.setText(name);
                            holder.myevent_discription.setText(desc);
                            holder.myevent_local.setText(location);
                            holder.eventOption.setVisibility(View.INVISIBLE);
                            holder.myevent_start.setText(time);
                            holder.myevent_title.setText(title);
                            holder.time.setText(timeevent);
                            Picasso.get().load(uri).into(holder.myecent_imageprofile);
                            holder.favoris.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myrandonneeRef.child(key).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                Toast.makeText(getApplicationContext(), "Remove from Favoris", Toast.LENGTH_SHORT).show();
                                                myrandonneeRef.child(key).child(firebaseUser.getUid()).removeValue();
                                                holder.favoris.setImageResource(R.drawable.ic_staremty);
                                                notifyDataSetChanged();

                                            }else {
                                                Toast.makeText(getApplicationContext(), "Randonnee add to Favoris", Toast.LENGTH_SHORT).show();
                                                myrandonneeRef.child(key).child(firebaseUser.getUid()).setValue("Favoris");
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
holder.CountFavoris(key,firebaseUser.getUid(),myrandonneeRef);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @NonNull
            @Override
            public MyRandonneeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_randonnee,parent,false);

                return new MyRandonneeViewHolder(view);
            }
        };
        randAdapter.startListening();
        recyclerViewRandonnee.setAdapter(randAdapter);
    }
/*********************************************************************/
    private void LoadsMyEvents() {
        options = new FirebaseRecyclerOptions.Builder<MyEvent>().setQuery(favorisRef,MyEvent.class).build();
        adapter = new FirebaseRecyclerAdapter<MyEvent, MyEventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyEventViewHolder holder, int position, @NonNull MyEvent model) {
                String key = getRef(position).getKey();
                eventRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.exists()){
                       String name = snapshot.child("event_username").getValue().toString();
                       String time = snapshot.child("event_start").getValue().toString();
                       String location = snapshot.child("event_location").getValue().toString();
                       String desc = snapshot.child("event_discription").getValue().toString();
                       String uri = snapshot.child("event_userimage").getValue().toString();
                       String title = snapshot.child("event_title").getValue().toString();
                       String timeevent = snapshot.child("time").getValue().toString();

                       holder.myevent_username.setText(name);
                       holder.myevent_discription.setText(desc);
                       holder.myevent_local.setText(location);
                       holder.myevent_start.setText(time);
                       holder.myevent_title.setText(title);
                       holder.eventtime.setText(timeevent);
                       holder.eventOption.setVisibility(View.INVISIBLE);
                       Picasso.get().load(uri).into(holder.myecent_imageprofile);
                       holder.CountFavoris(key,firebaseUser.getUid(),favorisRef);
                       holder.favoris.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               favorisRef.child(key).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {

                                       if(snapshot.exists()){
                                           Toast.makeText(getApplicationContext(), "Remove from Favoris", Toast.LENGTH_SHORT).show();
                                           favorisRef.child(key).child(firebaseUser.getUid()).removeValue();
                                           holder.favoris.setImageResource(R.drawable.ic_staremty);
                                           notifyDataSetChanged();
                                       }
                                       else{
                                           Toast.makeText(getApplicationContext(), "Event add to Favoris", Toast.LENGTH_SHORT).show();
                                           favorisRef.child(key).child(firebaseUser.getUid()).setValue("Favoris");
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

                   }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            @NonNull
            @Override
            public MyEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_view,parent,false);
                return new MyEventViewHolder(view) ;
            }
        };
        adapter.startListening();
        recyclerViewEvent.setAdapter(adapter);
    }
}