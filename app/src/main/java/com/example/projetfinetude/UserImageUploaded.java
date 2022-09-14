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

public class UserImageUploaded extends AppCompatActivity {
ImageView addpost,btn_switch;
TextView destination;
RecyclerView recyclerView ;
String userUID,mydestination;
DatabaseReference destPostRef ;
FirebaseRecyclerOptions<DestinationPost>options;
FirebaseRecyclerAdapter<DestinationPost,MyDestPostHolder>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image_uploaded);
        addpost = findViewById(R.id.dest_post_add);
        btn_switch = findViewById(R.id.btn_switch2);
        destination = findViewById(R.id.dest_post_name);
        recyclerView = findViewById(R.id.dest_post_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setFitsSystemWindows(true);

        mydestination = getIntent().getStringExtra("Direction");
        userUID = getIntent().getStringExtra("useruid");
        destPostRef = FirebaseDatabase.getInstance().getReference().child("Destination_post").child(mydestination);
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Region.class));
            }
        });
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userUID.equals( "Visiteur")){
                    startActivity(new Intent(getApplicationContext(),AddUserRequest.class));

                }

                else {
                    Intent intent = new Intent(getApplicationContext(), AddDestinationImage.class);
                    intent.putExtra("Direction",mydestination);
                    intent.putExtra("useruid",userUID);
                    startActivity(intent);
                }

            }

        });
        destination.setText(mydestination);
        LoadPost();
    }

    private void LoadPost( ) {
options = new FirebaseRecyclerOptions.Builder<DestinationPost>().setQuery(destPostRef,DestinationPost.class).build();
adapter = new FirebaseRecyclerAdapter<DestinationPost, MyDestPostHolder>(options) {
    @Override
    protected void onBindViewHolder(@NonNull MyDestPostHolder holder, int position, @NonNull DestinationPost model) {
        String key=getRef(position).getKey();
        String refuid = model.getUseruid();
        Picasso.get().load(model.getPosturi()).into(holder.picture);
        Picasso.get().load(model.getUserimage()).into(holder.userimage);
        holder.username.setText(model.getUsername());
        holder.time.setText(model.getTime());
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "image clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),ImagePostActivity.class);
                intent.putExtra("ImagePosturi",model.getPosturi());
                startActivity(intent);
            }
        });
       holder.userimage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(!userUID.equals(refuid)){
                   Intent i =new Intent(getApplicationContext(),FriendProfile.class);
                   i.putExtra("postuserkey",refuid);
                   i.putExtra("postusername",model.getUsername());
                   i.putExtra("postuserimage",model.getUserimage());

                   Toast.makeText(getApplicationContext(), "Welcome to "+model.getUsername()+" Profile", Toast.LENGTH_SHORT).show();
                   startActivity(i);}
               else{
                   Toast.makeText(getApplicationContext(), "You can pass to your profile form\n the NavigationView", Toast.LENGTH_SHORT).show();
               }           }
       });
        if(userUID.equals(refuid)){
            holder.option.setVisibility(View.VISIBLE);
            holder.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.delete.setVisibility(View.VISIBLE);
                    holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         destPostRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Toast.makeText(UserImageUploaded.this, "Success", Toast.LENGTH_SHORT).show();
                             }
                         });
                        }
                    });
                }
            });

        }

    }

    @NonNull
    @Override
    public MyDestPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_dest_post_layout,parent,false);

        return new MyDestPostHolder(view);
    }
};
adapter.startListening();;
recyclerView.setAdapter(adapter);
    }
}