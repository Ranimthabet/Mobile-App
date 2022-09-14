package com.example.projetfinetude;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    CircleImageView comment_imageprofile;
    TextView comment_username,comment_text;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        comment_imageprofile=itemView.findViewById(R.id.singlecomment_profilimage);
        comment_username=itemView.findViewById(R.id.singlecomment_username);
        comment_text=itemView.findViewById(R.id.singlecomment_comment);

    }
}
