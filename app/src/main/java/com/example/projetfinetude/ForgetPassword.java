package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
EditText inputemail;
Button btn_ok ;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        inputemail = findViewById(R.id.passwordforget);
        btn_ok = findViewById(R.id.btnvalider);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputemail.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(ForgetPassword.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(ForgetPassword.this, "Please chek your Email", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),Connexion.class));
                       }
                       else {
                           Toast.makeText(ForgetPassword.this, "Email not sent", Toast.LENGTH_SHORT).show();

                       }

                        }
                    });

                }
            }
        });

    }
}