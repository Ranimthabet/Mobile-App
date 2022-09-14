package com.example.projetfinetude;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
public class ReceptionAcceuil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView addPostimage, sendPostImage;
    EditText post;
    String imageURI;
    String Userheader_profileimage, Userheader_username;
    private static final int REQUEST_ACCESS_GALERIE_PERMISSION = 9001;
    Uri postImageUri;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    CircleImageView header_profileimage;
    TextView header_username;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference muserRef, postRef,likeRef,comentRef,myRef;
    FirebaseRecyclerAdapter<Posts, MyViewHolder> adapter;
    FirebaseRecyclerOptions<Posts> options;
    FirebaseRecyclerOptions<Comment> CommentOptions;
    FirebaseRecyclerAdapter<Comment,CommentViewHolder>CommentAdapter;
    SwipeRefreshLayout swipeRefreshLayout ;
    @Override
    protected void onStart() {
        super.onStart();
        if (mUser != null) {
            muserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Userheader_profileimage = snapshot.child("imageuri").getValue().toString();
                    Userheader_username = snapshot.child("name").getValue().toString() + " " + snapshot.child("lastName").getValue().toString();
                    Picasso.get().load(Userheader_profileimage).into(header_profileimage);
                    header_username.setText(Userheader_username);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception_acceuil);
        addPostimage = findViewById(R.id.imagepost);
        sendPostImage = findViewById(R.id.ic_postok);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Acceuil");
        setSupportActionBar(toolbar);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Post");
        progressDialog.setCanceledOnTouchOutside(false);
        getSupportActionBar().setTitle("Acceuil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        post = findViewById(R.id.posttext);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        header_profileimage = view.findViewById(R.id.header_imageprofile);
        header_username = view.findViewById(R.id.header_username);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        muserRef = FirebaseDatabase.getInstance().getReference().child("User");
        postRef = database.getReference().child("Posts");
        likeRef = database.getReference().child("Likes");
        comentRef = database.getReference().child("Comments");
        myRef = database.getReference().child("Comments");
        StorageReference storageReference = storage.getReference().child("PostImages");

        navigationView.setNavigationItemSelectedListener(this);
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadsPost("");
                swipeRefreshLayout.setColorSchemeColors(Color.GREEN);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
       // linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFitsSystemWindows(true);

        header_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openProfile = new Intent(getApplicationContext(),Profile.class);
                openProfile.putExtra("username",Userheader_username);
                openProfile.putExtra("imageprofileuri",Userheader_profileimage);
                startActivity(openProfile);

            }
        });


        addPostimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_ACCESS_GALERIE_PERMISSION);
            }
        });
        sendPostImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String postdiscription = post.getText().toString();
                if (postdiscription.isEmpty() || postImageUri == null) {
                    post.setError("please check your fields");
                    post.requestFocus();
                } else {
                    progressDialog.show();
                    DateFormat date = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
                    String timeAgo = date.format(Calendar.getInstance().getTime());
                    String key = postRef.push().getKey();
                    if (postImageUri != null) {
                        imageURI = postImageUri.toString();
                        storageReference.child(key + ".jpg").putFile(postImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String testUri = uri.toString() ;
                                        Posts posts = new Posts(mUser.getUid(),Userheader_username, postdiscription, timeAgo,testUri , Userheader_profileimage);
                                        postRef.child(key).setValue(posts).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ReceptionAcceuil.this, "succeully uploaded...", Toast.LENGTH_SHORT).show();
                                               addPostimage.setImageResource(R.drawable.ic_addpic);
                                               post.setText("");
                                                progressDialog.dismiss();

                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        LoadsPost("");

    }

    private void LoadsPost(String s) {
 options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postRef,Posts.class).build();
 adapter= new FirebaseRecyclerAdapter<Posts, MyViewHolder>(options) {
     @Override
     protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Posts model) {
         String postKey = getRef(position).getKey();
         String userkey = model.getUserUId();
         /*******************************************DeletePost*****************************************************/
         if(userkey.equals(mUser.getUid())){
             holder.post_option.setVisibility(View.VISIBLE);
         }
         holder.post_option.setOnClickListener(new View.OnClickListener() {
             @Override

             public void onClick(View v) {
                holder.btn_postoption.setVisibility(View.VISIBLE);
                holder.btn_share.setVisibility(View.VISIBLE);
                holder.btn_share.setText("Set Post");
                holder.btn_postoption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    DeletePost(postKey,Userheader_profileimage);
                    holder.btn_postoption.setVisibility(View.GONE);
                    holder.btn_share.setVisibility(View.GONE);
                    }
                });

                holder.btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetPost(postKey,Userheader_profileimage);
                        holder.btn_share.setVisibility(View.GONE);
                        holder.btn_postoption.setVisibility(View.GONE);
                    }
                });
             }
         });
         /***************************************************************************************************/

         holder.postsingle_username.setText(" "+model.getUsername());
       //  String timeAgo = CalculateTimeAgo(model.getTimeAgo());
         holder.postsingle_timeAgo.setText(" "+model.getTimeAgo());
         holder.postsingle_description.setText(" "+" "+model.getPostDiscription());
         holder.CountLike(postKey,mUser.getUid(),likeRef);
         holder.CountComment(postKey,mUser.getUid(),comentRef);
         holder.postesingle_like_logo.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                    likeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                          if(snapshot.exists()){
                              likeRef.child(postKey).child(mUser.getUid()).removeValue();
                              holder.postesingle_like_logo.setImageResource(R.drawable.ic_love);
                              notifyDataSetChanged();
                          }
                          else{
                              likeRef.child(postKey).child(mUser.getUid()).setValue("Likes");
                              holder.postesingle_like_logo.setImageResource(R.drawable.ic_loveadd);
                              holder.postesingle_like_logo.setColorFilter(Color.RED);
                              notifyDataSetChanged();
                          }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ReceptionAcceuil.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
             }
         });
         holder.postsingle_image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(ReceptionAcceuil.this, "image clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),ImagePostActivity.class);
                intent.putExtra("ImagePosturi",model.getImagePost());
                startActivity(intent);
             }
         });
         holder.postesingle_sendcomment.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String commentText = holder.postesingle_comment.getText().toString();
                 if(commentText.isEmpty()){
                     holder.postesingle_comment.setError("Vous douvez Commenter ");
                     holder.postesingle_comment.requestFocus();
                    return;
                 }else{
                     AddComments(holder,postKey,comentRef,mUser.getUid(),commentText);
                 }
             }
         });
         LoadsComment(postKey);
         Picasso.get().load(model.getImagePost()).into(holder.postsingle_image);
         Picasso.get().load(model.getUserprofileimage()).into(holder.postsingle_profilimage);
         /****************************************************************************************/
        holder.postsingle_profilimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userkey = model.getUserUId();
                String userimage = model.getUserprofileimage();
                String username = model.getUsername();
                if(!mUser.getUid().equals(userkey)){
                Intent i =new Intent(getApplicationContext(),FriendProfile.class);
              i.putExtra("postuserkey",model.getUserUId());
               i.putExtra("postusername",model.getUsername());
                i.putExtra("postuserimage",model.getUserprofileimage());

                Toast.makeText(ReceptionAcceuil.this, "Welcome to "+username+" Profile", Toast.LENGTH_SHORT).show();
                startActivity(i);}
                else{
                    Toast.makeText(ReceptionAcceuil.this, "You can pass to your profile form\n the NavigationView", Toast.LENGTH_SHORT).show();
                }
            }
        });
         /***********************************************************************************/
     }
     @NonNull
     @Override
     public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_layout,parent,false);
         return new MyViewHolder(view);
     }
 };
 adapter.startListening();
 recyclerView.setAdapter(adapter);
    }
/*********************************************************************************************/
    private void SetPost(String postKey, String testUri) {
        postRef.child(postKey).addValueEventListener(new ValueEventListener() {
        String refURi;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               refURi = snapshot.child("userprofileimage").getValue().toString();
               if(refURi.equals(testUri)){
                   Toast.makeText(ReceptionAcceuil.this, "You can manage this post", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),PostSetting.class);
                intent.putExtra("Postkey",postKey);
                intent.putExtra("userimageprofileRef",testUri);
                   startActivity(intent);


               }
               else{
                   Toast.makeText(ReceptionAcceuil.this, "You can't set this Post", Toast.LENGTH_SHORT).show();

               }
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    /**************************************************/
    private void DeletePost(String postKey ,String testUri) {

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
        };

    /***************************************************/

    private void AddComments(MyViewHolder holder, String postKey, DatabaseReference comentRef, String uid, String commentText) {
        HashMap hashMap = new HashMap();
        hashMap.put("userUID",mUser.getUid());
        hashMap.put("userName",Userheader_username);
        hashMap.put("profilImageUri",Userheader_profileimage);
        hashMap.put("Comment",commentText);
        comentRef.child(postKey).child(uid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(ReceptionAcceuil.this, "Comment Added", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    holder.postesingle_comment.setText(" ");

                }
                else {
                    Toast.makeText(ReceptionAcceuil.this, " "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
/******************************************************************/
    private void LoadsComment(String postKey) {
        MyViewHolder.comment_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CommentOptions = new FirebaseRecyclerOptions.Builder<Comment>().setQuery(comentRef.child(postKey),Comment.class).build();
        CommentAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(CommentOptions) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {
                Picasso.get().load(model.getProfilImageUri()).into(holder.comment_imageprofile);
                holder.comment_username.setText(" " + model.getUserName());
                holder.comment_text.setText(" " + model.getComment());

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


    private String CalculateTimeAgo(String timeAgo) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
        try {
            long time = sdf.parse(timeAgo).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";

        }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ACCESS_GALERIE_PERMISSION && data != null && resultCode == RESULT_OK){
            postImageUri = data.getData();
            addPostimage.setImageURI(postImageUri);

        }

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId()){
            case R.id.home :
                Toast.makeText(getApplicationContext(),"Home clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile :
                    Intent openProfile= new Intent(getApplicationContext(),Profile.class);
                openProfile.putExtra("username",Userheader_username);
                openProfile.putExtra("imageprofileuri",Userheader_profileimage);
                startActivity(openProfile);
                break;
            case R.id.geomap :
                Toast.makeText(getApplicationContext(),"geomap clicked",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MyLocationMap.class));
                break;
            case R.id.friend :
                startActivity(new Intent(getApplicationContext(),MyFriendActivity.class));
                break;
            case R.id.add_friend :
                startActivity(new Intent(getApplicationContext(),FindFriend.class));
                break;
            case R.id.chat :

                Intent chatintent= new Intent(getApplicationContext(),ChatReception.class);
                chatintent.putExtra("username",Userheader_username);
                chatintent.putExtra("imageprofileuri",Userheader_profileimage);

                startActivity(chatintent);
                break;
            case R.id.events :
                Intent intent= new Intent(getApplicationContext(),EventReception.class);
                intent.putExtra("username",Userheader_username);
                intent.putExtra("imageprofileuri",Userheader_profileimage);
                startActivity(intent);


                break;
            case R.id.group :
            Intent grp = new Intent(getApplicationContext(),GroupReception.class);
            startActivity(grp);
                break;
            case R.id.myfavoris:
                Intent myintent= new Intent(getApplicationContext(),Favoris.class);
                myintent.putExtra("username",Userheader_username);
                myintent.putExtra("imageprofileuri",Userheader_profileimage);
                startActivity(myintent);
                break;
            case R.id.randonnée :
                Intent randonneeintent= new Intent(getApplicationContext(),RandonneReception.class);
                randonneeintent.putExtra("username",Userheader_username);
                randonneeintent.putExtra("imageprofileuri",Userheader_profileimage);
                startActivity(randonneeintent);
                break;
            case R.id.feedback :
                    Intent feedback = new Intent(getApplicationContext(),FeedbackAcceuil.class);
                feedback.putExtra("username",Userheader_username);
                feedback.putExtra("imageprofileuri",Userheader_profileimage);
                startActivity(feedback);
                break;
            case R.id.logout :
                mAuth.signOut();
                Toast.makeText(getApplicationContext(),"Vous avez déconnectez",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                break;
        }
        return true;
    }

}