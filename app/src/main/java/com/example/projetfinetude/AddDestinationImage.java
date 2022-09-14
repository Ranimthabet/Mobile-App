package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDestinationImage extends AppCompatActivity {
ImageView imagetoadd,chooser;
TextView uploadpic;
Uri imageuri ;
String imageURI;

    DatabaseReference userRef,destPostRef;
    String destination,userUid,username,userimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destination_image);
        imagetoadd = findViewById(R.id.AddimageToDest);
        chooser = findViewById(R.id.chooseimage);
        uploadpic = findViewById(R.id.sendImageTodest);
        destination = getIntent().getStringExtra("Direction");
        userUid = getIntent().getStringExtra("useruid");
        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userUid);
        destPostRef = FirebaseDatabase.getInstance().getReference().child("Destination_post").child(destination);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Destination_Post_Image").child(destination);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username = snapshot.child("name").getValue().toString()+" "+snapshot.child("lastName").getValue().toString();
                    userimage = snapshot.child("imageuri").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,9001);
            }
        });
        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageuri == null){
                    Toast.makeText(AddDestinationImage.this, " Please choose pic", Toast.LENGTH_SHORT).show();
                }
                else {
                    /**********************************************************************************/
                   DateFormat date = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
                    String timeAgo = date.format(Calendar.getInstance().getTime());
                    String key = destPostRef.push().getKey();
                      imageURI = imageuri.toString();
                    storageReference.child(key + ".jpg").putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String testUri = uri.toString() ;
                                    DestinationPost destinationPost = new DestinationPost(userUid,username, userimage,timeAgo,testUri  );
                                    destPostRef.child(key).setValue(destinationPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "succeully uploaded...", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),Region.class));

                                        }
                                    });
                                }
                            });
                        }
                    });


                    /***************************************************************************************/
                    Toast.makeText(AddDestinationImage.this, "you can add pic"+username, Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddDestinationImage.this, "you can add pic"+userimage, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 9001 && resultCode == RESULT_OK && data != null){
            imageuri = data.getData();
            imagetoadd.setImageURI(imageuri);
        }
    }
}