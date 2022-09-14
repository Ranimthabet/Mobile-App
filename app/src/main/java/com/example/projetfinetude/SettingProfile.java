package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SettingProfile extends AppCompatActivity {
EditText fname,flastname,fphone,fbirthdate,femail;
ImageView name_logo,last_logo,phone_logo,date_logo,mail_logo;
String userUID,userimageuri;
TextView btn_save;
    String Cpassword;
    DatabaseReference userRef;
    ProgressDialog progressDialog;
    DatePickerDialog.OnDateSetListener setListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);
        name_logo = findViewById(R.id.logo_name);
        last_logo = findViewById(R.id.logo_lastname);
        date_logo = findViewById(R.id.logo_Birthdate);
        mail_logo = findViewById(R.id.logo_email);
        phone_logo = findViewById(R.id.logo_phone);
        fname = findViewById(R.id.name_field);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Data");
        progressDialog.setMessage("Please wait ..... ");

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        btn_save = findViewById(R.id.btn_save);
        flastname = findViewById(R.id.lastname_field);
        fphone = findViewById(R.id.phone_field);
        fbirthdate = findViewById(R.id.birthdate_field);
        femail = findViewById(R.id.email_field);
        userUID = getIntent().getStringExtra("useruid");
        userimageuri = getIntent().getStringExtra("userimage");
        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userUID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               String Cname = snapshot.child("name").getValue().toString();
               String Clastname = snapshot.child("lastName").getValue().toString();
               String Cemail = snapshot.child("email").getValue().toString();
               String Cphone = snapshot.child("phone").getValue().toString();
               String Cbirthdate = snapshot.child("birthdate").getValue().toString();
              Cpassword= snapshot.child("password").getValue().toString();
               fname.setText(Cname);
               flastname.setText(Clastname);
               femail.setText(Cemail);
               fbirthdate.setText(Cbirthdate);
               fphone.setText(Cphone);
           }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_logo.setColorFilter(Color.GRAY);
            }
        });
        flastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last_logo.setColorFilter(Color.GRAY);
            }
        });
        femail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail_logo.setColorFilter(Color.GRAY);
            }
        });
        fbirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_logo.setColorFilter(Color.GRAY);
            }
        });
        fphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_logo.setColorFilter(Color.GRAY);
            }
        });
        fbirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SettingProfile.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, setListner, year, month, day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month= month+1;
                String date = dayOfMonth + "/" + month + "/" + year;
                fbirthdate.setText(date);

            }
        };

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nname = fname.getText().toString();
                String Nlast = flastname.getText().toString();
                String Nbirthdate = fbirthdate.getText().toString();
                String Nemail = femail.getText().toString();
                String Nphone = fphone.getText().toString();


                if(Nname.isEmpty()){
                    fname.setError("missing name");
                    fname.requestFocus();
                    return;
                }
                if(Nlast.isEmpty()){
                    flastname.setError("missing name");
                    flastname.requestFocus();
                    return;
                }
                if(Nemail.isEmpty()){
                    femail.setError("missing name");
                    femail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Nemail).matches())
                    if(Nbirthdate.isEmpty()){
                        fbirthdate.setError("missing name");
                        fbirthdate.requestFocus();
                        return;
                    }
                if(Nphone.isEmpty()){
                    fphone.setError("missing name");
                    fphone.requestFocus();
                    return;
                }
                progressDialog.show();

                Users users = new Users(userUID,Nname,Nlast,Nbirthdate,Nphone,Nemail,Cpassword,userimageuri);
                userRef.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(SettingProfile.this, "your data has been updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SettingProfile.this, ReceptionAcceuil.class));
                        }
                        else {
                            Toast.makeText(SettingProfile.this, "Error in updating data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });



    }
}