package com.example.projetfinetude;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Noter extends AppCompatActivity {
EditText note;
TextView btn_note;
String userUid,destination ;
Integer noteuser;
DatabaseReference noteRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noter);
        note = findViewById(R.id.note_note);
        btn_note = findViewById(R.id.save_note);
        destination = getIntent().getStringExtra("Direction");
        userUid = getIntent().getStringExtra("useruid");
        noteRef = FirebaseDatabase.getInstance().getReference().child("Note_Destination").child(destination);

        btn_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteuser= Integer.parseInt( note.getText().toString());

                if(noteuser<1 || noteuser>10 ){
                    Toast.makeText(Noter.this, "Verify your note", Toast.LENGTH_SHORT).show();
                }
                else{
                    String key = noteRef.push().getKey();
                    MyNote myNote = new MyNote(userUid,destination,noteuser);
                        noteRef.child(key).setValue(myNote).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Noter.this, "we have recice your rainting \n and we thank you", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Region.class));
                            }

                        });

                }


            }
        });



    }
}