package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetRandonne extends AppCompatActivity {
    CircleImageView imageprofile;
    TextView username,set_rando;
    EditText CTitlte,Cdate,Clocation,Cdiscription;
   String rname;
   // String rtitle,rdate,rlocation,rdiscription;
    ImageView btn_back;
    String randokey , userimagepicture;
    DatabaseReference randRef;
    DatePickerDialog.OnDateSetListener setListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_randonne);
       imageprofile = findViewById(R.id.setrandoimage);
        username = findViewById(R.id.setrandoname);
        CTitlte = findViewById(R.id.setrandotitle);
        Cdate = findViewById(R.id.setrandodate);
        Clocation = findViewById(R.id.setrandolocationn);
        Cdiscription = findViewById(R.id.setrandodiscription);
        set_rando = findViewById(R.id.setrando);
        btn_back = findViewById(R.id.backrando);
        Calendar calendar  = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        randokey = getIntent().getStringExtra("randokey");
        userimagepicture = getIntent().getStringExtra("userimageprofileRef");
        randRef = FirebaseDatabase.getInstance().getReference().child("Randonnee").child(randokey);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Tache end without setting", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), RandonneReception.class));
            }
        });
        randRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                     rname = snapshot.child("randonne_username").getValue().toString();
                    String rtitle = snapshot.child("randonne_title").getValue().toString();
                    String   rdate = snapshot.child("randonne_start").getValue().toString();
                    String   rlocation = snapshot.child("randonne_location").getValue().toString();
                    String   rdiscription = snapshot.child("randonne_discription").getValue().toString();
                    Picasso.get().load(userimagepicture).into(imageprofile);
                    username.setText(rname);
                    CTitlte.setText(rtitle);
                    Cdate.setText(rdate);
                    Clocation.setText(rlocation);
                    Cdiscription.setText(rdiscription);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Cdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SetRandonne.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, setListner, year, month, day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month= month+1;
                String event_firstdate = dayOfMonth + "/" + month + "/" + year;
                Cdate.setText(event_firstdate);

            }
        };
        set_rando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = CTitlte.getText().toString();
                String eventLocation = Clocation.getText().toString();
                String eventStart = Cdate.getText().toString();
                String eventDiscription = Cdiscription.getText().toString();
                if(eventTitle.isEmpty()){
                    CTitlte.setError("Please enter Title for your Event");
                    CTitlte.requestFocus();
                    return;
                }
                if(eventLocation.isEmpty()){
                    Clocation.setError("Please enter location of your Event");
                    Clocation.requestFocus();
                    return;
                }

                if(eventStart.isEmpty()){
                    Cdate.setError("Please enter date of your Event");
                    Cdate.requestFocus();
                    return;
                }
                if(eventDiscription.isEmpty()){
                    Cdiscription.setError("Please enter Discription of your Event");
                    Cdiscription.requestFocus();
                    return;
                }
                DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
                String time = dateFormat.format(Calendar.getInstance().getTime());
                MyRandonnee myRandonnee = new MyRandonnee(rname, userimagepicture,time, eventTitle, eventLocation, eventStart, eventDiscription);
                randRef.child(randokey).setValue(myRandonnee).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "succeully uploaded...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ReceptionAcceuil.class));
                    }
                });
            }
        });
    }
}