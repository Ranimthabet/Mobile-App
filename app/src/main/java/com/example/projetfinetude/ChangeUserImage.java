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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeUserImage extends AppCompatActivity {
    DatabaseReference userRef ;
CircleImageView setImage;
TextView saveImage ;
StorageReference storageReference;
String userUID ,imageURI;
String Cname,Clastname,Cemail,Cphone,Cbirthdate,Cpassword;
Uri imageuri;
    //ProgressDialog progressDialog;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_image);
        setImage = findViewById(R.id.setuserimage);
        saveImage = findViewById(R.id.btn_saveimage);

      /*  progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Changing User Pic");
        progressDialog.setMessage("please wait");*/
        userUID = getIntent().getStringExtra("useruid");
        storageReference = FirebaseStorage.getInstance().getReference().child("Upload").child(userUID);
        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userUID);
        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,9001);
            }
        });
        /**********************************************************************/
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                     Cname = snapshot.child("name").getValue().toString();
                     Clastname = snapshot.child("lastName").getValue().toString();
                     Cemail = snapshot.child("email").getValue().toString();
                     Cphone = snapshot.child("phone").getValue().toString();
                     Cbirthdate = snapshot.child("birthdate").getValue().toString();
                     Cpassword = snapshot.child("password").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

saveImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        //progressDialog.show();
        if(imageuri != null) {
            storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
               if(task.isSuccessful()) {
                   storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           imageURI=uri.toString();
                           Users users = new Users(userUID,Cname,Clastname,Cbirthdate,Cphone,Cemail,Cpassword,imageURI);
                           userRef.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   Toast.makeText(ChangeUserImage.this, "Opreation succeded", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(getApplicationContext(),Profile.class));


                               }
                           });
                       }
                   });
               }
                }
            });

        }
        else {
            Toast.makeText(ChangeUserImage.this, "choose pic to save ", Toast.LENGTH_SHORT).show();

        }
        /*if (uri != null){
            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageURI=uri.toString();
                                Users users = new Users(userUID,Cname,Clastname,Cbirthdate,Cphone,Cemail,Cpassword,imageURI);
                                setuserRef.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                          //  progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Succes Changing image", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), UserAcceuil.class));
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });
                    } }
            });
        }else {
            Toast.makeText(ChangeUserImage.this, "Failure saving data", Toast.LENGTH_SHORT).show();
        }*/

    }
});

        /********************************************************************/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 9001 && resultCode == RESULT_OK && data != null){
            imageuri = data.getData();
            setImage.setImageURI(imageuri);

        }

    }
}