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
import android.widget.ImageView;
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

public class RandonneReception extends AppCompatActivity {
    RecyclerView recyclerView ;
    ImageView addRandonne;
    String username,imageprofile;
    FirebaseRecyclerOptions<MyRandonnee>options;
    FirebaseRecyclerAdapter<MyRandonnee,MyRandonneeViewHolder>adapter;
    DatabaseReference randonneRef,myrandonneRef;
       String postKey ;
       FirebaseAuth firebaseAuth;
       FirebaseUser firebaseUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randonne_reception);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        username = getIntent().getStringExtra("username");
        imageprofile = getIntent().getStringExtra("imageprofileuri");
        randonneRef = FirebaseDatabase.getInstance().getReference().child("Randonnee");
        myrandonneRef = FirebaseDatabase.getInstance().getReference().child("Favoris").child("MyRandonnes");
        addRandonne = findViewById(R.id.addRandonne);
        addRandonne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

Intent intent = new Intent(getApplicationContext(),Randonne.class);
intent.putExtra("username",username);
intent.putExtra("imageprofileuri",imageprofile);
startActivity(intent);

            }
        });

        recyclerView = findViewById(R.id.recyclerRandonne);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFitsSystemWindows(true);
        LoadsRandonnee();
    }

    private void LoadsRandonnee() {
        options = new FirebaseRecyclerOptions.Builder<MyRandonnee>().setQuery(randonneRef,MyRandonnee.class).build();
        adapter = new FirebaseRecyclerAdapter<MyRandonnee, MyRandonneeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyRandonneeViewHolder holder, int position, @NonNull MyRandonnee model) {
              postKey= getRef(position).getKey();

                holder.myevent_title.setText(" "+model.randonne_title);
                holder.myevent_discription.setText(" "+" "+model.randonne_discription);
                holder.myevent_local.setText(" "+model.randonne_location);
                holder.myevent_start.setText(" "+model.randonne_start);
                holder.myevent_username.setText(" "+model.randonne_username);
                if(imageprofile.equals(model.getRandonne_userimage())){
                    holder.eventOption.setVisibility(View.VISIBLE);

                }
                holder.time.setText(model.getTime());
                holder.favoris.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postKey= getRef(position).getKey();
                        myrandonneRef.child(postKey).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(getApplicationContext(), "Remove from Favoris", Toast.LENGTH_SHORT).show();
                        myrandonneRef.child(postKey).child(firebaseUser.getUid()).removeValue();
                        holder.favoris.setImageResource(R.drawable.ic_staremty);
                        notifyDataSetChanged();

                    }else {
                        Toast.makeText(getApplicationContext(), "Randonnee add to Favoris", Toast.LENGTH_SHORT).show();
                        myrandonneRef.child(postKey).child(firebaseUser.getUid()).setValue("Favoris");
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
                /***************************************************************************/
                holder.CountFavoris(postKey,firebaseUser.getUid(),myrandonneRef);
                /**************************************************************************/
                holder.eventOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.event_delete.setVisibility(View.VISIBLE);
                        holder.event_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DeleteRandonnee(postKey,imageprofile);

                            }
                        });
                        holder.event_set.setVisibility(View.VISIBLE);
                        holder.event_set.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setRandonnee(postKey,imageprofile);
                            }
                        });
                    }
                });
                Picasso.get().load(model.randonne_userimage).into(holder.myecent_imageprofile);
            }

            @NonNull
            @Override
            public MyRandonneeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_randonnee,parent,false);
                return new MyRandonneeViewHolder(view) ;
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
/************************************************************************************/
private void setRandonnee(String eventkey, String imageprofileuri) {
    randonneRef.child(eventkey).addValueEventListener(new ValueEventListener() {
        String refUri;
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                refUri = snapshot.child("randonne_userimage").getValue().toString();
                if(imageprofileuri.equals(refUri)){
                    Intent intent = new Intent(getApplicationContext(),SetRandonne.class);
                    intent.putExtra("randokey",postKey);
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
/****************************************************************************************************/
    private void DeleteRandonnee(String postKey, String imageprofile) {
        randonneRef.child(postKey).addValueEventListener(new ValueEventListener() {
            String refURi;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    refURi = snapshot.child("randonne_userimage").getValue().toString();
                    if(imageprofile.equals(refURi)){
                        randonneRef.child(postKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "You have delete this randonnee", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "OOOUPs \nYou can't delete this Randonnee" , Toast.LENGTH_SHORT).show();

                    } }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});



    }


}