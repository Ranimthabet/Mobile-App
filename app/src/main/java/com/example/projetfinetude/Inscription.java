package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Inscription extends AppCompatActivity {

    private static final int REQUEST_ACCESS_GALERIE_PERMISSION = 9001;
    EditText ins_name, ins_lastname, ins_phone, ins_birthdate, ins_email, ins_password, ins_confirmPassword;
    TextView ins_visitus, ins_aboutus;
    Button ins_btninscrire;
    CircleImageView profil_image;
    DatePickerDialog.OnDateSetListener setListner;
    Uri imageUri;
    String imageURI;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ins_name = findViewById(R.id.ins_name);
        ins_lastname = findViewById(R.id.ins_lastname);
        ins_phone = findViewById(R.id.ins_phone);
        ins_birthdate = findViewById(R.id.ins_birthdate);
        ins_email = findViewById(R.id.ins_email);
        ins_password = findViewById(R.id.ins_password);
        ins_confirmPassword = findViewById(R.id.ins_confpassword);
        ins_aboutus = findViewById(R.id.ins_aboutus);
        ins_visitus = findViewById(R.id.ins_visitus);
        ins_btninscrire = findViewById(R.id.ins_btninscrire);
        profil_image = findViewById(R.id.profile_image);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        /******************************************/
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Pease Wait ...");
        progressDialog.setTitle(" Authentification");
        progressDialog.setCancelable(false);
        /******************************************/

        ins_visitus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inscription.this,VisiteurAcceuil.class));
            }
        });
        ins_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Inscription.this,
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
                ins_birthdate.setText(date);

            }
        };

        profil_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQUEST_ACCESS_GALERIE_PERMISSION);
            }
        });
        ins_btninscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = ins_name.getText().toString();
                String lastname = ins_lastname.getText().toString();
                String phone = ins_phone.getText().toString();
                String birthDate = ins_birthdate.getText().toString();
                String email = ins_email.getText().toString();
                String password = ins_password.getText().toString();
                String confirmPassword =ins_confirmPassword.getText().toString();
                if(name.isEmpty()){
                    ins_name.setError("Entrez votre nom");
                    ins_name.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(lastname.isEmpty()){
                    ins_lastname.setError("Entrez votre prénom");
                    ins_lastname.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(phone.isEmpty()){
                    ins_phone.setError("Entrez votre numéro de téléphone");
                    ins_phone.requestFocus();
                    progressDialog.dismiss();

                    return;
                }
                if(birthDate.isEmpty()){
                    ins_birthdate.setError("Entrez votre birthdate");
                    ins_birthdate.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(email.isEmpty()){
                    ins_email.setError("Entrez votre email");
                    ins_email.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    ins_email.setError("Enter Valid Email Address");
                    ins_email.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(password.isEmpty()){
                    ins_password.setError("Entrez une mot de passe");
                    ins_password.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(password.length()<8){
                    ins_password.setError("Entrez une mot de passe valide");
                    ins_password.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(confirmPassword.isEmpty()){
                    ins_confirmPassword.setError("Confirmez votre mot de passe");
                    ins_confirmPassword.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(!confirmPassword.equals(password)){
                    ins_confirmPassword.setError("Confirmez votre mot de passe");
                    ins_confirmPassword.requestFocus();
                    progressDialog.dismiss();

                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference reference= database.getReference().child("User").child(firebaseAuth.getUid());
                            StorageReference storageReference=storage.getReference().child("Upload").child(firebaseAuth.getUid());
                            if (imageUri != null){
                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful()){
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageURI=uri.toString();
                                                    Users users = new Users(firebaseAuth.getUid(),name,lastname,birthDate,phone,email,password,imageURI);
                                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                progressDialog.dismiss();
                                                                Toast.makeText(Inscription.this, "An other user has been created", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(Inscription.this, UserAcceuil.class));
                                                            }
                                                            else {
                                                                Toast.makeText(Inscription.this, "Error in Inscription", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }
                                            });
                                        } }
                                });
                            }



                            else{
                                imageURI="https://firebasestorage.googleapis.com/v0/b/projetfinetude-60247.appspot.com/o/profile.png?alt=media&token=de3809ef-84cb-4dd7-a879-e6146608906c";
                                Users users = new Users(firebaseAuth.getUid(),name,lastname,birthDate,phone,email,password,imageURI);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Inscription.this, "An other user has been created without proil pic", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Inscription.this, UserAcceuil.class));
                                        }
                                        else {
                                            Toast.makeText(Inscription.this, "Error in Inscription", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(Inscription.this, "Something go rong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ACCESS_GALERIE_PERMISSION && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            profil_image.setImageURI(imageUri);

        }

    }
}