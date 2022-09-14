package com.example.projetfinetude;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Events extends AppCompatActivity {
    EditText event_title,event_location,event_startDate,event_Discrription;
    Button btn_addEvent;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database ;
    DatabaseReference eventRef;
    String Refusername,Refimageprofileuri;
    DatePickerDialog.OnDateSetListener setListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        event_title = findViewById(R.id.event_title);
        event_location = findViewById(R.id.event_location);
        event_startDate = findViewById(R.id.event_startdate);
        event_Discrription  = findViewById(R.id.event_discription);
        Refusername =getIntent().getStringExtra("username");
        Refimageprofileuri =getIntent().getStringExtra("imageprofileuri");
        btn_addEvent  = findViewById(R.id.btn_add);
        firebaseAuth = FirebaseAuth.getInstance();
        Calendar calendar  = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        firebaseUser =firebaseAuth.getCurrentUser();
        database =FirebaseDatabase.getInstance();
        eventRef = database.getReference().child("Events");
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Pease Wait ...");
        progressDialog.setTitle(" Uploading Event");
        progressDialog.setCancelable(false);
        event_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Events.this,
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
                event_startDate.setText(event_firstdate);

            }
        };
        btn_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventTitle = event_title.getText().toString();
                String eventLocation = event_location.getText().toString();
                String eventStart = event_startDate.getText().toString();
                String eventDiscription = event_Discrription.getText().toString();
                if(eventTitle.isEmpty()){
                    event_title.setError("Please enter Title for your Event");
                    event_title.requestFocus();
                    return;
                }
                if(eventLocation.isEmpty()){
                    event_location.setError("Please enter location of your Event");
                    event_location.requestFocus();
                    return;
                }

                if(eventStart.isEmpty()){
                    event_startDate.setError("Please enter location of your Event");
                    event_startDate.requestFocus();
                    return;
                }
                if(eventDiscription.isEmpty()){
                    event_Discrription.setError("Please enter location of your Event");
                    event_Discrription.requestFocus();
                    return;
                }
                String key = eventRef.push().getKey();
                DateFormat dateFormat = new  SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
                String time = dateFormat.format(Calendar.getInstance().getTime());

                MyEvent myEvent = new MyEvent(Refusername, Refimageprofileuri, time,eventTitle, eventLocation, eventStart, eventDiscription);
                eventRef.child(key).setValue(myEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "succeully uploaded...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent= new Intent(getApplicationContext(),EventReception.class);
                        intent.putExtra("username",Refusername);
                        intent.putExtra("imageprofileuri",Refimageprofileuri);
                        startActivity(intent);
                    }
                });
            }
        });
    }

}