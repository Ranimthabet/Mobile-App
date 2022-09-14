package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddAvis extends AppCompatActivity {

    CircleImageView profileimage ;
    TextView save_avis,username;
    EditText avis_text;
    String userUID ,destnination,Cusername,Cuseruri,avisuser;
    DatabaseReference userRef,avisRef;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_avis);

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Pease Wait ...");
        progressDialog.setTitle(" Uploading Avis");
        profileimage = findViewById(R.id.avis_userimage);
        save_avis = findViewById(R.id.avis_save);
        username = findViewById(R.id.avis_username);
        avis_text = findViewById(R.id.avis_text);
        userUID = getIntent().getStringExtra("useruid");
        destnination = getIntent().getStringExtra("Direction");
        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userUID);
        avisRef = FirebaseDatabase.getInstance().getReference().child("Avis").child(destnination);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               Cusername = snapshot.child("name").getValue().toString()+" "+snapshot.child("lastName").getValue().toString();
               Cuseruri = snapshot.child("imageuri").getValue().toString();
               Picasso.get().load(Cuseruri).into(profileimage);
               username.setText(Cusername);

           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        save_avis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 avisuser = avis_text.getText().toString();
                if (avisuser.isEmpty() || avisuser.length()<5){
                    avis_text.setError("Please Verify your avis");
                    avis_text.requestFocus();
                    return;
                }
                else {
                  //  progressDialog.show();
                    DateFormat date = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
                    String timeAgo = date.format(Calendar.getInstance().getTime());

                    String key = avisRef.push().getKey();
                    MyAvis myAvis = new MyAvis(userUID, Cuseruri, Cusername, timeAgo, avisuser);
                    avisRef.child(key).setValue(myAvis).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "succeully uploaded...", Toast.LENGTH_SHORT).show();
                     //       progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(),Avis.class);
                            intent.putExtra("userimageuri",Cuseruri);
                            intent.putExtra("username",Cusername);
                            startActivity(intent);
                            finish();
                        }
                    });


                }

            }
        });


    }
}