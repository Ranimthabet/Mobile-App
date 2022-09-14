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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddGroup extends AppCompatActivity {
    CircleImageView group_image;
    EditText group_name,group_information;
    TextView btn_addgroup;
    Uri uri ;
    DatabaseReference userRef,groupRef,memberGroupRef,usergroupRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
String cname,cemail,curi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        group_image = findViewById(R.id.addgrp_image);
        group_name = findViewById(R.id.addgrp_name);
        group_information = findViewById(R.id.addgrp_data);
        btn_addgroup = findViewById(R.id.btn_addgrp);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser =firebaseAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        usergroupRef = FirebaseDatabase.getInstance().getReference().child("UserGroup");

        memberGroupRef = FirebaseDatabase.getInstance().getReference().child("MemberGroup");
        userRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            cname = snapshot.child("name").getValue().toString()+" "+snapshot.child("lastName").getValue().toString();
            cemail = snapshot.child("email").getValue().toString();
                curi = snapshot.child("imageuri").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        group_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,44);
            }
        });
        btn_addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = group_name.getText().toString();
                String data = group_information.getText().toString();
                if(name.isEmpty() || name.length()<3){
                    group_name.requestFocus();
                    group_name.setError("Please verify your name");
                    return;

                }
                if(data.isEmpty() || data.length()<3){
                    group_information.requestFocus();
                    group_information.setError("Please verify your informations");
                    return;
                }
                if(uri == null){
                    Toast.makeText(AddGroup.this, "Please choose pic ", Toast.LENGTH_SHORT).show();

                    return;
                }
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("GroupImages");
                String key = groupRef.push().getKey();
                 String   imageURI = uri.toString();
                    storageReference.child(key + ".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String testUri = uri.toString() ;
                                    MyGroup myGroup = new MyGroup(key,testUri, name, data ,firebaseUser.getUid());
                                    groupRef.child(key).setValue(myGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Group have been created ...", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),GroupReception.class));
                                            MyMemeber myMemeber = new MyMemeber(cname,cemail,curi);
                                            memberGroupRef.child(key).child(firebaseUser.getUid()).setValue(myMemeber);
                                            usergroupRef.child(firebaseUser.getUid()).child(key).setValue(myGroup);
                                        }
                                    });
                                }
                            });
                        }
                    });


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 44 && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            group_image.setImageURI(uri);
        }
    }
}