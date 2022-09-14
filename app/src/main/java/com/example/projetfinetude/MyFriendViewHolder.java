package com.example.projetfinetude;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFriendViewHolder extends RecyclerView.ViewHolder {
CircleImageView my_friend_imageprofile;
TextView my_friend_username,my_friend_useremail;
    public MyFriendViewHolder(@NonNull View itemView) {
        super(itemView);
my_friend_imageprofile = itemView.findViewById(R.id.myfriend_imageprofile);
my_friend_useremail = itemView.findViewById(R.id.myfriend_useremail);
my_friend_username = itemView.findViewById(R.id.myfriend_username);


    }
}
