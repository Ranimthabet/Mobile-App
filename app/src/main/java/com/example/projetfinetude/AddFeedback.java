package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class AddFeedback extends AppCompatActivity {
CircleImageView userimage;
TextView username,add_feedback;
EditText feedback;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser ;
DatabaseReference feddbackRef ;
String Cusername,Cuserimage,myuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);
        userimage = findViewById(R.id.feedback_image);
        username = findViewById(R.id.feedback_username);
        feedback = findViewById(R.id.myfeedback);
        add_feedback = findViewById(R.id.add_feedback);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        myuid = firebaseUser.getUid();
        Cusername = getIntent().getStringExtra("username");
        Cuserimage = getIntent().getStringExtra("imageprofileuri");
        feddbackRef = FirebaseDatabase.getInstance().getReference().child("Feedback");
        username.setText(Cusername);
        Picasso.get().load(Cuserimage).into(userimage);
        add_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myfeed = feedback.getText().toString();
                if(myfeed.length()<10 || myfeed.isEmpty()){
                    feedback.requestFocus();
                    feedback.setError("Please Enter your Feedback");
                    return;
                }
                else {

                    DateFormat date = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
                    String timeAgo = date.format(Calendar.getInstance().getTime());
                    Myfeedback myfeedback = new Myfeedback(myuid,Cusername,Cuserimage,timeAgo,myfeed);
                    String key = feddbackRef.push().getKey();
                    feddbackRef.child(key).setValue(myfeedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddFeedback.this, " \n WE have recive your feedback", Toast.LENGTH_SHORT).show();
                            Toast.makeText(AddFeedback.this, "Thank's for your help", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),FeedbackAcceuil.class);
                            intent.putExtra("username",Cusername);
                            intent.putExtra("imageprofileuri",Cuserimage);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}