package com.example.projetfinetude;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    public MyPostHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imagedirection);
    }
}
