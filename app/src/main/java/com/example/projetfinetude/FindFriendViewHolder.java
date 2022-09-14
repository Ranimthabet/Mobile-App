package com.example.projetfinetude;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendViewHolder extends RecyclerView.ViewHolder {
    CircleImageView findfriend_imageprofile;
    TextView findfriend_username;
    public FindFriendViewHolder(@NonNull View itemView) {
        super(itemView);
        findfriend_imageprofile = itemView.findViewById(R.id.findfriend_imageprofile);
        findfriend_username = itemView.findViewById(R.id.findfriend_username);

    }
}
