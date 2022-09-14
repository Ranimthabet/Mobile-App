package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostSetting extends AppCompatActivity {
CircleImageView post_profileimage;
TextView post_username,post_datepost;
ImageView post_image,btn_return;
TextView setPost;
EditText post_discription;
DatabaseReference postRef ;
String userUID,postkey,userimageuri;
String Cuser,Ctime,Cdiscription,Cimagepost;
Uri imageuri;
String imageURI, Ndiscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_setting);
        post_profileimage = findViewById(R.id.setpost_profile_image);
        post_username = findViewById(R.id.setpost_username);
        post_datepost = findViewById(R.id.setpost_timeago);
        post_discription = findViewById(R.id.setpost_discription);
        setPost = findViewById(R.id.setpost);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set Post");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        post_image = findViewById(R.id.setpost_image);
        btn_return = findViewById(R.id.btn_return);
        postkey = getIntent().getStringExtra("Postkey");
        userimageuri = getIntent().getStringExtra("userimageprofileRef");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("PostImages");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postkey);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostSetting.this, "Tache end without setting", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ReceptionAcceuil.class));
            }
        });
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userUID = snapshot.child("userUId").getValue().toString();
                    Cuser = snapshot.child("username").getValue().toString();
                    Ctime = snapshot.child("timeAgo").getValue().toString();
                    Cdiscription = snapshot.child("postDiscription").getValue().toString();
                    Cimagepost = snapshot.child("imagePost").getValue().toString();
                    Picasso.get().load(userimageuri).into(post_profileimage);
                    Picasso.get().load(Cimagepost).into(post_image);
                    post_username.setText(Cuser);
                    post_datepost.setText(Ctime);
                    post_discription.setText(Cdiscription);
                    post_username.setError("uou can't change this item");
                    post_datepost.setError("uou can't change this item");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 9001);
            }
        });

        setPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              Ndiscription= post_discription.getText().toString();
                if(Ndiscription.isEmpty()|| (Ndiscription.length() < 5)){
                    post_discription.setError("Please Verify your Discription");
                    post_discription.requestFocus();
                    return;

                }
                if(imageuri!= null ) {
                    progressDialog.show();
                    DateFormat date = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
                    String timeAgo = date.format(Calendar.getInstance().getTime());
                    imageURI = imageuri.toString();
                    storageReference.child(postkey + ".jpg").putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child(postkey + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String testuri = uri.toString();
                                    Posts posts = new Posts(userUID, Cuser, Ndiscription, timeAgo, testuri, userimageuri);
                                    postRef.child(postkey).setValue(posts).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(PostSetting.this, "Now have  Set this Post \n in this time  " + timeAgo, Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), ReceptionAcceuil.class));
                                        }
                                    });
                                }
                            });
                        }
                    });
                    /******************************************************************************/
                }else {
                    Toast.makeText(PostSetting.this, "Please Chooos enew Picture", Toast.LENGTH_SHORT).show();
                }
                }

                /************************************************************************************/
            });

    }












    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 9001 && resultCode == RESULT_OK && data != null){
            imageuri = data.getData();
            post_image.setImageURI(imageuri);
        }
    }
}