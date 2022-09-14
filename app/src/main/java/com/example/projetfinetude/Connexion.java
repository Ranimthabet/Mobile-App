package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Connexion extends AppCompatActivity {
    TextView cnx_aboutus,cnx_visitus,forgetPassword;
    EditText cnx_email,cnx_password;
    Button cnx_btncnx;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cnx_email = findViewById(R.id.cnx_email);
        cnx_password = findViewById(R.id.cnx_password);
        cnx_btncnx = findViewById(R.id.cnx_btncnx);
        cnx_aboutus= findViewById(R.id.cnx_aboutus);
        cnx_visitus= findViewById(R.id.cnx_visitus);
        firebaseAuth = FirebaseAuth.getInstance();
        forgetPassword = findViewById(R.id.forgetpassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Connecting");
        progressDialog.setMessage("Pease Wait ...");
        progressDialog.setCancelable(false);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgetPassword.class));
            }
        });
        cnx_visitus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connexion.this,VisiteurAcceuil.class));
            }
        });
        cnx_btncnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = cnx_email.getText().toString();
                String password = cnx_password.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    cnx_email.setError("Enter Valid Email Address");
                    cnx_email.requestFocus();
                    return;

                }
                if(email.isEmpty()){
                    cnx_email.setError("Entrez votre adress e_mail");
                    cnx_email.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(password.isEmpty()){
                    cnx_password.setError("Entrez votre mot de passe");
                    cnx_password.requestFocus();
                    progressDialog.dismiss();

                    return;
                }
                if(password.length()<8){
                    cnx_password.setError("Entrez une valide mot de passe");
                    cnx_password.requestFocus();
                    progressDialog.dismiss();
                    return;

                }
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(Connexion.this, "Bienvenue ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Connexion.this, UserAcceuil.class));
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(Connexion.this, "Error : "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}