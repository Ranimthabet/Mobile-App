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

public class SetEvents extends AppCompatActivity {
CircleImageView imageprofile;
TextView username,set_event;
EditText CTitlte,Cdate,Clocation,Cdiscription;
String name,title,date,location,discription;
ImageView btn_back;
String eventkey , userimagepicture;
DatabaseReference eventRef;
    DatePickerDialog.OnDateSetListener setListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_events);
        imageprofile = findViewById(R.id.setEvent_profile_image);
        username = findViewById(R.id.setevent_username);
        CTitlte = findViewById(R.id.settitleEvent);
        Cdate = findViewById(R.id.seteventstart);
        Clocation = findViewById(R.id.setlocation);
        Cdiscription = findViewById(R.id.setdiscriptionEvent);
        set_event = findViewById(R.id.setevent);
        btn_back = findViewById(R.id.seteventbtn_return);
        Calendar calendar  = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        eventkey = getIntent().getStringExtra("eventkey");
        userimagepicture = getIntent().getStringExtra("userimageprofileRef");
        eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Tache end without setting", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), EventReception.class));
            }
        });
        eventRef.child(eventkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name = snapshot.child("event_username").getValue().toString();
                    title = snapshot.child("event_title").getValue().toString();
                    date = snapshot.child("event_start").getValue().toString();
                    location = snapshot.child("event_location").getValue().toString();
                    discription = snapshot.child("event_discription").getValue().toString();
                    Picasso.get().load(userimagepicture).into(imageprofile);
                    username.setText(name);
                    CTitlte.setText(title);
                    Cdate.setText(date);
                    Clocation.setText(location);
                    Cdiscription.setText(discription);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Cdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SetEvents.this,
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
        set_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SetEvents.this, "suuuuuuuuuuuuuuuuuuuucss", Toast.LENGTH_SHORT).show();
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

                DateFormat date = new SimpleDateFormat();
                String timeAgo = date.format(Calendar.getInstance().getTime());
                MyEvent myEvent = new MyEvent(name, userimagepicture,timeAgo, eventTitle, eventLocation, eventStart, eventDiscription);
                eventRef.child(eventkey).setValue(myEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "succeully uploaded...", Toast.LENGTH_SHORT).show();
                     //   progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), ReceptionAcceuil.class));
                    }
                });

            }
        });
    }
}