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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleGroup extends AppCompatActivity {
    String groupid;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference userRef, usergroupRef,likeRef,groupRef,postGroupRef ;
    TextView group_name,group_info;
    CircleImageView group_image;
    RecyclerView recyclerView ;
    String group_creator,name,data,uri;
    ImageView group_member,group_addpost,group_setting,group_chat;
    String username,useruri;
    FirebaseRecyclerOptions<Posts>options;
    FirebaseRecyclerAdapter<Posts,MyViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_group);
        groupid = getIntent().getStringExtra("groupid");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        usergroupRef = FirebaseDatabase.getInstance().getReference().child("UserGroup").child(firebaseUser.getUid());

        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupid);
        postGroupRef = FirebaseDatabase.getInstance().getReference().child("Postgroup").child(groupid);

        group_name = findViewById(R.id.groupname);
        group_info = findViewById(R.id.groupdata);
        group_image = findViewById(R.id.groupimage);
        recyclerView = findViewById(R.id.groupmedia);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setFitsSystemWindows(true);
        group_member = findViewById(R.id.groupmemeber);
        group_addpost = findViewById(R.id.groupaddpost);
        group_setting = findViewById(R.id.groupconfig);
        group_chat = findViewById(R.id.groupchat);
        group_chat.setColorFilter(Color.GREEN);
        group_addpost.setColorFilter(Color.BLUE);
        group_setting.setColorFilter(Color.WHITE);
        group_member.setColorFilter(Color.RED);
        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
            username = snapshot.child("name").getValue().toString()+" "+snapshot.child("lastName").getValue().toString();
            useruri = snapshot.child("imageuri").getValue().toString();
           }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               name = snapshot.child("groupe_name").getValue().toString();
               data = snapshot.child("groupe_information").getValue().toString();
               uri = snapshot.child("groupe_image").getValue().toString();
               group_creator = snapshot.child("group_creator").getValue().toString();
               Picasso.get().load(uri).into(group_image);
               group_name.setText(name);
               group_info.setText(data);
               if(firebaseUser.getUid().equals(group_creator)){
                   Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                   group_setting.setVisibility(View.VISIBLE);
               }
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        group_addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usergroupRef.child(groupid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            Toast.makeText(SingleGroup.this, "You have to join this Group", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(getApplicationContext(),AddPostGroup.class);
                            intent.putExtra("groupid",groupid);
                            intent.putExtra("groupname",name);
                            intent.putExtra("groupuri",uri);
                            intent.putExtra("username",username);
                            intent.putExtra("userimage",useruri);
                            intent.putExtra("useruid",firebaseUser.getUid());
                            Toast.makeText(SingleGroup.this, ""+username, Toast.LENGTH_SHORT).show();
                            Toast.makeText(SingleGroup.this, ""+useruri, Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usergroupRef.child(groupid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            Toast.makeText(SingleGroup.this, "You have to join this Group", Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(SingleGroup.this, "A verifer", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        group_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usergroupRef.child(groupid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            Toast.makeText(SingleGroup.this, "You have to join this Group", Toast.LENGTH_SHORT).show();
                        }else {

                            Intent intent = new Intent(getApplicationContext(),MemberGroup.class);
                            intent.putExtra("groupid",groupid);
                            Toast.makeText(SingleGroup.this, ""+groupid, Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

LoadsMyPost();
    }

    private void LoadsMyPost() {
        options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postGroupRef,Posts.class).build();
        adapter = new FirebaseRecyclerAdapter<Posts, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Posts model) {
                String postKey = getRef(position).getKey();
                String userkey = model.getUserUId();
                Picasso.get().load(model.getImagePost()).into(holder.postsingle_image);
                holder.postsingle_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "image clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ImagePostActivity.class);
                        intent.putExtra("ImagePosturi",model.getImagePost());
                        startActivity(intent);
                    }
                });
                Picasso.get().load(model.getUserprofileimage()).into(holder.postsingle_profilimage);
                holder.postsingle_profilimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userkey = model.getUserUId();
                        String userimage = model.getUserprofileimage();
                        String username = model.getUsername();
                        if(!firebaseUser.getUid().equals(userkey)){
                            Intent i =new Intent(getApplicationContext(),FriendProfile.class);
                            i.putExtra("postuserkey",model.getUserUId());
                            i.putExtra("postusername",model.getUsername());
                            i.putExtra("postuserimage",model.getUserprofileimage());

                            Toast.makeText(getApplicationContext(), "Welcome to "+username+" Profile", Toast.LENGTH_SHORT).show();
                            startActivity(i);}
                        else{
                            Toast.makeText(getApplicationContext(), "You can pass to your profile form\n the NavigationView", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                holder.postsingle_timeAgo.setText(model.getTimeAgo());
                holder.postsingle_description.setText(model.getPostDiscription());
                holder.postsingle_username.setText(model.getUsername());
                holder.postesingle_like_logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeRef.child(postKey).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    likeRef.child(postKey).child(firebaseUser.getUid()).removeValue();
                                    holder.postesingle_like_logo.setImageResource(R.drawable.ic_love);
                                    notifyDataSetChanged();
                                }
                                else{
                                    likeRef.child(postKey).child(firebaseUser.getUid()).setValue("Likes");
                                    holder.postesingle_like_logo.setImageResource(R.drawable.ic_loveadd);
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
                holder.CountLike(postKey,firebaseUser.getUid(),likeRef);

                holder.postsingle_commentcounter.setVisibility(View.INVISIBLE);
                holder.postesingle_sendcomment.setVisibility(View.INVISIBLE);
                holder.postesingle_comment_logo.setVisibility(View.INVISIBLE);
                holder.postesingle_comment.setVisibility(View.INVISIBLE);
                if(userkey.equals(firebaseUser.getUid())){
                    holder.post_option.setVisibility(View.VISIBLE);
                }
                holder.post_option.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        holder.btn_postoption.setVisibility(View.VISIBLE);
                        holder.btn_share.setVisibility(View.INVISIBLE);
                        holder.btn_share.setText("Set Post");
                        holder.btn_postoption.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DeletePost(postKey);
                                holder.btn_postoption.setVisibility(View.GONE);
                                holder.btn_share.setVisibility(View.GONE);
                            }
                        });


                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_layout,parent,false);
                return new MyViewHolder(view);            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void DeletePost(String postKey) {
        postGroupRef.child(postKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SingleGroup.this, "Succusfully deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}