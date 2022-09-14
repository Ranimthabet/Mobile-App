package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriend extends AppCompatActivity {
DatabaseReference myRef,userRef,requestRef,friendRef ;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser ;
String profileimageuri,username,useremail;
CircleImageView profileImage ;
TextView Username , Useremail ;
Button btn_send,btn_decline;
String CurrentState = "nothing happen";
    String UserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
         UserID  = getIntent().getStringExtra("userKey");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(UserID);
        requestRef = FirebaseDatabase.getInstance().getReference().child("Request");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friend");
        profileImage = findViewById(R.id.viewfriend_imageprofile);
        Username = findViewById(R.id.viewfriend_username);
        Useremail = findViewById(R.id.viewfriend_useremail);
        btn_send = findViewById(R.id.sendrequest);
        btn_decline = findViewById(R.id.declinerequest);
        LoadUser();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(UserID);
            }
        });

        CheckUserExistance(UserID);
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unifriend(UserID);
            }
        });
  }

    private void Unifriend(String userID) {
    if(CurrentState.equals("friend")){
    friendRef.child(firebaseUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                friendRef.child(userID).child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ViewFriend.this, "You are Unfriend", Toast.LENGTH_SHORT).show();
                        CurrentState = "nothing happen";
                        btn_send.setText("Send Friend Request");
                        btn_decline.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
    });
}
    if(CurrentState.equals("he sent Pending")){
    HashMap hashMap = new HashMap();
    hashMap.put("Status","decline");
    requestRef.child(userID).child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
       if(task.isSuccessful()){
           Toast.makeText(ViewFriend.this, "You have decline Request", Toast.LENGTH_SHORT).show();
           CurrentState ="he sent decline";
           btn_send.setVisibility(View.GONE);
           btn_decline.setVisibility(View.GONE);

       }
        }
    });

}

    }

    private void CheckUserExistance(String userID) {

        friendRef.child(firebaseUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               CurrentState = "friend";
               btn_send.setText("Send Message");
               btn_decline.setText("Unfriend");
               btn_decline.setVisibility(View.VISIBLE);
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendRef.child(userID).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               CurrentState = "friend";
               btn_send.setText("Send Message");
               btn_decline.setText("Unfriend");
               btn_decline.setVisibility(View.VISIBLE);
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(firebaseUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               if(snapshot.child("Status").getValue().toString().equals("Pending")){
                   CurrentState = "I send Pending";
                   btn_send.setText("Cancel friend request");
                   btn_decline.setVisibility(View.GONE);
               }
               if(snapshot.child("Status").getValue().toString().equals("decline")){
                   CurrentState = "I send Pending";
                   btn_send.setText("I sent decline");
                   btn_decline.setVisibility(View.GONE);
               }
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(userID).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               if(snapshot.child("Status").getValue().toString().equals("Pending")){
                   CurrentState="he sent Pending";
                   btn_send.setText("Accept friend Request");
                   btn_decline.setText("Decline friend");
                   btn_decline.setVisibility(View.VISIBLE);
               }
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(CurrentState.equals("nothing happen")){
            CurrentState="nothing happen";
            btn_send.setText("Send friend request");
            btn_decline.setVisibility(View.GONE);
        }


    }

    private void sendRequest(String userID) {
        if(CurrentState.equals("nothing happen")){
            HashMap hashMap = new HashMap();
            hashMap.put("Status","Pending");
            requestRef.child(firebaseUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
               if(task.isSuccessful()){
                   Toast.makeText(ViewFriend.this, "You have sent friend request", Toast.LENGTH_SHORT).show();
                   btn_decline.setVisibility(View.GONE);
                   CurrentState = "I sent Pending";
                   btn_send.setText("Cancel Friend request");
               }
               else {
                   Toast.makeText(ViewFriend.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
               }
                }
            });
        }
        if(CurrentState.equals("I sent Pending")||CurrentState.equals("I sent decline")){
            requestRef.child(firebaseUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   Toast.makeText(ViewFriend.this, "You have Canceled friend request", Toast.LENGTH_SHORT).show();
                CurrentState = "nothing happen";
                btn_send.setText("Send Request");
                btn_decline.setVisibility(View.GONE);

               }
                else {
                   Toast.makeText(ViewFriend.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();

               }

                }
            });
        }
        if(CurrentState.equals("he sent Pending")){
            requestRef.child(userID).child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){

                   HashMap hashMap = new HashMap();
                   hashMap.put("Status","friend");
                   hashMap.put("username",username);
                   hashMap.put("profileImageUri",profileimageuri);
                   hashMap.put("email",useremail);

                   friendRef.child(firebaseUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                       @Override
                       public void onComplete(@NonNull Task task) {
                      if(task.isSuccessful()){

                          friendRef.child(userID).child(firebaseUser.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener() {
                              @Override
                              public void onComplete(@NonNull Task task) {

                                  Toast.makeText(ViewFriend.this, "You added friend", Toast.LENGTH_SHORT).show();
                                  CurrentState ="friend";
                                  btn_send.setText("Send Message");
                                  btn_decline.setText("Unfriend");
                                  btn_decline.setVisibility(View.VISIBLE);

                              }
                          });
                      }
                       }
                   });

               }
                }
            });
        }
        if(CurrentState.equals("friend")){
            Intent intent= new Intent(getApplicationContext(),ChatActivity.class);
            intent.putExtra("reciveruid",UserID);
            startActivity(intent);        }
    }

    private void LoadUser(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               profileimageuri= snapshot.child("imageuri").getValue().toString();
               username = snapshot.child("name").getValue().toString()+"  "+snapshot.child("lastName").getValue().toString();
               useremail = snapshot.child("email").getValue().toString();
               Picasso.get().load(profileimageuri).into(profileImage);
               Username.setText(username);
               Useremail.setText(useremail);



           }
           else {
               Toast.makeText(ViewFriend.this, "Data not found !!!", Toast.LENGTH_SHORT).show();
           }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriend.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}