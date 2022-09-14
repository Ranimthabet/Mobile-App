package com.example.projetfinetude;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyRandonneeViewHolder extends RecyclerView.ViewHolder {
    CircleImageView myecent_imageprofile;
    ImageView eventOption,favoris;
    TextView myevent_discription,time,event_delete,event_set;
    TextView  myevent_username,myevent_title,myevent_local,myevent_start,compteur;
    public MyRandonneeViewHolder(@NonNull View itemView) {
        super(itemView);
        myecent_imageprofile = itemView.findViewById(R.id.post_profile_image);
        myevent_username = itemView.findViewById(R.id.event_username);
        myevent_title = itemView.findViewById(R.id.titleEvent);
        myevent_local = itemView.findViewById(R.id.location);
        myevent_start = itemView.findViewById(R.id.eventstart);
        myevent_discription = itemView.findViewById(R.id.discriptionEvent);
        eventOption = itemView.findViewById(R.id.eventoption);
        time = itemView.findViewById(R.id.timeEvent);
        event_delete = itemView.findViewById(R.id.delete);
        event_set = itemView.findViewById(R.id.set);
        favoris = itemView.findViewById(R.id.favoris);
        compteur = itemView.findViewById(R.id.favoriscount);


    }

    public void CountFavoris(String postKey, String uid, DatabaseReference myrandonneRef) {
        myrandonneRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalfavoris = (int) snapshot.getChildrenCount();
                    compteur.setText(totalfavoris+"");
                }
                else{
                    compteur.setText("0");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myrandonneRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(uid).exists()){
                    favoris.setImageResource(R.drawable.ic_startfill);
favoris.setColorFilter(Color.YELLOW);

                }
                else{
                    favoris.setImageResource(R.drawable.ic_staremty);
                    favoris.setColorFilter(Color.BLACK);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
