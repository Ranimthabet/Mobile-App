package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private CircleImageView profile_imageprofile;
    private TextView profile_userName,profile_phone,profile_email,profile_birthdate;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private  DatabaseReference UserRef,postRef,likeRef,comentRef;
RecyclerView recyclerView ;
    FirebaseRecyclerAdapter<Posts, MyViewHolder> adapter;
    FirebaseRecyclerOptions<Posts> options;
    FirebaseRecyclerOptions<Comment> CommentOptions;
    FirebaseRecyclerAdapter<Comment,CommentViewHolder>CommentAdapter;
String imagetest;
ImageView logo_agenda,logo_tlfn,logo_adress ,logo_setting,logo_addpic;
String profileimageuri,prfiluserName ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        profile_imageprofile = findViewById(R.id.porfile_imageporfile);
        profile_userName = findViewById(R.id.profile_username);
        profile_phone = findViewById(R.id.profile_userphone);
        logo_agenda=findViewById(R.id.ic_prfltlfn);
        logo_agenda.setColorFilter(Color.BLUE);
        logo_tlfn=findViewById(R.id.ic_prflagenda);
        logo_setting=findViewById(R.id.prfl_setting);
        logo_addpic=findViewById(R.id.prfl_addpost);
        logo_tlfn.setColorFilter(Color.BLUE);
        logo_adress=findViewById(R.id.ic_prflemail);
        logo_adress.setColorFilter(Color.BLUE);
        imagetest = getIntent().getStringExtra("imageprofileuri");
        profile_email = findViewById(R.id.profile_email);
        profile_birthdate = findViewById(R.id.profile_birthdate);
        recyclerView = findViewById(R.id.profilerecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setFitsSystemWindows(true);
        logo_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SettingProfile.class);
                intent.putExtra("username",prfiluserName);
                intent.putExtra("userimage",profileimageuri);
                intent.putExtra("useruid",firebaseUser.getUid());

                startActivity(intent);

            }
        });
        logo_addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UploadImage.class);
                intent.putExtra("username",prfiluserName);
                intent.putExtra("userimage",profileimageuri);
                intent.putExtra("useruid",firebaseUser.getUid());
                startActivity(intent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        comentRef = FirebaseDatabase.getInstance().getReference().child("Comments");

profile_imageprofile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(Profile.this, "Change User profile picture", Toast.LENGTH_SHORT).show();
        Toast.makeText(Profile.this, ""+firebaseUser.getUid(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),ChangeUserImage.class);
        intent.putExtra("useruid",firebaseUser.getUid());
        startActivity(intent);
    }
});
        UserRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                     profileimageuri = snapshot.child("imageuri").getValue().toString();
                     prfiluserName = snapshot.child("name").getValue().toString() + " " + snapshot.child("lastName").getValue().toString();
                    String porfileuserPhone = snapshot.child("phone").getValue().toString();
                    String profileuseremail = snapshot.child("email").getValue().toString();
                    String profileuserbirthdate = snapshot.child("birthdate").getValue().toString();
                    Picasso.get().load(profileimageuri).into(profile_imageprofile);
                    profile_userName.setText(prfiluserName);
                    profile_birthdate.setText(profileuserbirthdate);
                    profile_phone.setText(porfileuserPhone);
                    profile_email.setText(profileuseremail);

                } else {
                    Toast.makeText(Profile.this, "Not Data found !!!!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

                      options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postRef, Posts.class).build();
                        adapter = new FirebaseRecyclerAdapter<Posts, MyViewHolder>(options) {

                            @NonNull
                            @Override
                            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_layout, parent, false);
                                return new MyViewHolder(view);
                            }

                            @Override
                            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Posts model) {
                                String postKey = getRef(position).getKey();
                                if(model.userprofileimage.equals(imagetest)){

                                Picasso.get().load(model.getUserprofileimage()).into(holder.postsingle_profilimage);
                                Picasso.get().load(model.getImagePost()).into(holder.postsingle_image);
                                holder.postsingle_image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(),ImagePostActivity.class);
                                        intent.putExtra("ImagePosturi",model.getImagePost());
                                        startActivity(intent);
                                    }
                                });
                                holder.postsingle_image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(),ImagePostActivity.class);
                                        intent.putExtra("ImagePosturi",model.getImagePost());
                                        startActivity(intent);
                                    }
                                });
                                holder.postsingle_username.setText(model.getUsername());
                                holder.postsingle_description.setText(model.getPostDiscription());
                                holder.postsingle_timeAgo.setText(model.getTimeAgo());
                                holder.CountLike(postKey, firebaseUser.getUid(), likeRef);
                                holder.CountComment(postKey, firebaseUser.getUid(), comentRef);
                                holder.post_option.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holder.btn_postoption.setVisibility(View.VISIBLE);
                                        holder.btn_postoption.setBackgroundColor(Color.WHITE);
                                        holder.btn_share.setVisibility(View.VISIBLE);
                                        holder.btn_share.setText("Set");
                                        holder.btn_share.setBackgroundColor(Color.WHITE);
                                        holder.btn_share.setTextColor(Color.BLUE);
                                        holder.btn_postoption.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                DeletePost(postKey, imagetest);
                                                holder.btn_postoption.setVisibility(View.GONE);
                                                holder.btn_share.setVisibility(View.GONE);
                                            }
                                        });
                                        holder.btn_share.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                                SetPost(postKey,imagetest);
                                                Toast.makeText(getApplicationContext(), "you can set this post", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });}
                                else {
                                    holder.itemView.setVisibility(View.GONE);
                                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                                }
                                LoadsComment(postKey);
                            }
                        };
                        adapter.startListening();
                        recyclerView.setAdapter(adapter);

                    }
/***********************************************************************************/
    private void SetPost(String postKey, String imagetest) {
        postRef.child(postKey).addValueEventListener(new ValueEventListener() {
            String refURi;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    refURi = snapshot.child("userprofileimage").getValue().toString();
                    if(refURi.equals(imagetest)){
                        Toast.makeText(getApplicationContext(), "You can manage this post", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),PostSetting.class);
                        intent.putExtra("Postkey",postKey);
                        intent.putExtra("userimageprofileRef",imagetest);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "You can't set this Post", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*****************************************************/
    private void LoadsComment(String postKey) {
        MyViewHolder.comment_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CommentOptions = new FirebaseRecyclerOptions.Builder<Comment>().setQuery(comentRef.child(postKey),Comment.class).build();
        CommentAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(CommentOptions) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {
                Picasso.get().load(model.getProfilImageUri()).into(holder.comment_imageprofile);
                holder.comment_username.setText(model.getUserName());
                holder.comment_text.setText(model.getComment());
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment_view,parent,false);
                return new CommentViewHolder(view) ;
            }
        };
        CommentAdapter.startListening();
        MyViewHolder.comment_recyclerView.setAdapter(CommentAdapter);

    }
    /********************************************/
    private void DeletePost(String postKey ,String testUri) {
        ;
        postRef.child(postKey).addValueEventListener(new ValueEventListener() {
            String refURi;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    refURi = snapshot.child("userprofileimage").getValue().toString();
                    if(testUri.equals(refURi)){
                        postRef.child(postKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "you have delete this post", Toast.LENGTH_SHORT).show();
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
    };

    /********************************************/
    }

