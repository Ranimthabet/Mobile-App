package com.example.projetfinetude;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatReception extends AppCompatActivity {
FirebaseRecyclerOptions<Friends>options;
FirebaseRecyclerAdapter<Friends,MyFriendViewHolder>adapter;
    DatabaseReference messageRef ,userRef;
    RecyclerView recyclerView ;
    String username,userimage;
    CircleImageView myimage;
    TextView myname;
    FirebaseAuth firebaseAuth ;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_reception);
       myimage = findViewById(R.id.mypic);
        myname = findViewById(R.id.myname);
        recyclerView = findViewById(R.id.mychatRecyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
      recyclerView.setFitsSystemWindows(true);
        username=getIntent().getStringExtra("username");
        userimage=getIntent().getStringExtra("imageprofileuri");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        messageRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(firebaseUser.getUid());
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
        Picasso.get().load(userimage).into(myimage);
        myname.setText(username);
        LoadMessage();
    }

    private void LoadMessage() {
    options = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(messageRef,Friends.class).build();
    adapter = new FirebaseRecyclerAdapter<Friends, MyFriendViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull MyFriendViewHolder holder, int position, @NonNull Friends model) {
            String refkey = getRef(position).getKey();
            messageRef.child(refkey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
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
                               holder.itemView.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       Intent intent= new Intent(getApplicationContext(),ChatActivity.class);
                                       intent.putExtra("reciveruid",getRef(position).getKey().toString());
                                       intent.putExtra("recivername",cname);
                                       intent.putExtra("reciverimage",cuseruri);
                                       startActivity(intent);
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
    adapter.startListening();
    recyclerView.setAdapter(adapter);

    }



}