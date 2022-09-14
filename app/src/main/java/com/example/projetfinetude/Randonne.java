package com.example.projetfinetude;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Randonne extends AppCompatActivity {
    EditText randonne_title,randonne_location,randonne_startDate,randonne_Discrription;
    Button btn_addrandonne;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database ;
    DatabaseReference randonneRef;
    String username,imageprofileuri;
    DatePickerDialog.OnDateSetListener setListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randonne);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        randonne_title = findViewById(R.id.randonne_title);
        randonne_location = findViewById(R.id.randonne_location);
        randonne_startDate = findViewById(R.id.randonne_startdate);
        randonne_Discrription = findViewById(R.id.randonne_discription);
        username = getIntent().getStringExtra("username");
        imageprofileuri = getIntent().getStringExtra("imageprofileuri");
        btn_addrandonne = findViewById(R.id.btn_addrandonne);
        firebaseAuth = FirebaseAuth.getInstance();
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        randonneRef = database.getReference().child("Randonnee");
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Pease Wait ...");
        progressDialog.setTitle(" Uploading Event");
        progressDialog.setCancelable(false);
        randonne_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Randonne.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, setListner, year, month, day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String randonnee_firstdate = dayOfMonth + "/" + month + "/" + year;
                randonne_startDate.setText(randonnee_firstdate);

            }
        };

        btn_addrandonne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = randonne_title.getText().toString();
                String Location = randonne_location.getText().toString();
                String Start = randonne_startDate.getText().toString();
                String Discription = randonne_Discrription.getText().toString();
                if (Title.isEmpty()) {
                    randonne_title.setError("Please enter Title for your Event");
                    randonne_title.requestFocus();
                    return;
                }
                if (Location.isEmpty()) {
                    randonne_location.setError("Please enter location of your Event");
                    randonne_location.requestFocus();
                    return;
                }

                if (Start.isEmpty()) {
                    randonne_startDate.setError("Please enter location of your Event");
                    randonne_startDate.requestFocus();
                    return;
                }
                if (Discription.isEmpty()) {
                    randonne_Discrription.setError("Please enter location of your Event");
                    randonne_Discrription.requestFocus();
                    return;
                }
                String key = randonneRef.push().getKey();
                DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
                String time = dateFormat.format(Calendar.getInstance().getTime());
                 MyRandonnee myRandonnee = new MyRandonnee(username, imageprofileuri,time, Title, Location, Start, Discription);
                randonneRef.child(key).setValue(myRandonnee).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "succeully uploaded...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        Intent intent= new Intent(getApplicationContext(),RandonneReception.class);
                        intent.putExtra("username",username);
                        intent.putExtra("imageprofileuri",imageprofileuri);
                        startActivity(intent);
                        finish();}
                });
            }
        });


    }
        }