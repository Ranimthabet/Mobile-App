package com.example.projetfinetude;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    CircleImageView postsingle_profilimage;
    EditText postesingle_comment;
    Button btn_postoption,btn_share ;
    ImageView postsingle_image,postesingle_like_logo,postesingle_comment_logo,postesingle_sendcomment,post_option;
   public static RecyclerView comment_recyclerView ;
    TextView postsingle_description,postsingle_username,postsingle_timeAgo,postsingle_likecounter,postsingle_commentcounter;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        postsingle_image = itemView.findViewById(R.id.post_image);
        postsingle_profilimage = itemView.findViewById(R.id.post_profile_image);
        postesingle_like_logo = itemView.findViewById(R.id.post_like);
        postsingle_likecounter = itemView.findViewById(R.id.post_likecounter);
        postesingle_comment_logo = itemView.findViewById(R.id.post_Comment);
        postsingle_commentcounter = itemView.findViewById(R.id.post_commentcounter);
        postsingle_username = itemView.findViewById(R.id.post_username);
        postsingle_timeAgo = itemView.findViewById(R.id.post_timeago);
        postsingle_description = itemView.findViewById(R.id.post_discription);
        postesingle_comment = itemView.findViewById(R.id.comment);
        postesingle_sendcomment = itemView.findViewById(R.id.sendComment);
        comment_recyclerView = itemView.findViewById(R.id.comment_recyler);
        post_option=itemView.findViewById(R.id.postoption);
        btn_postoption=itemView.findViewById(R.id.post_btnoptionpost);
        btn_share=itemView.findViewById(R.id.post_optionshare);

    }

    public void CountLike(String postKey, String uid, DatabaseReference likeRef) {
    likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
       if(snapshot.exists()){
         int   totalKike = (int) snapshot.getChildrenCount();
           postsingle_likecounter.setText(totalKike+"");
       }
       else{
           postsingle_likecounter.setText("0");

       }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.child(uid).exists()){
                postesingle_like_logo.setImageResource(R.drawable.ic_loveadd);
                postesingle_like_logo.setColorFilter(Color.RED);


            }
            else{
                postesingle_like_logo.setImageResource(R.drawable.ic_love);

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    public void CountComment(String postKey, String uid, DatabaseReference comentRef) {
        comentRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalcomment = (int) snapshot.getChildrenCount();
                    postsingle_commentcounter.setText(totalcomment+"");
                }
                else{
                    postsingle_commentcounter.setText("0");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       
    }
}
