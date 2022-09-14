package com.example.projetfinetude;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDestPostHolder extends RecyclerView.ViewHolder {
    CircleImageView userimage;
    TextView time,username;
    ImageView picture,option,delete;
    public MyDestPostHolder(@NonNull View itemView) {
        super(itemView);
        userimage = itemView.findViewById(R.id.dest_userimage);
        username = itemView.findViewById(R.id.dest_username);
        time = itemView.findViewById(R.id.dest_time);
        picture = itemView.findViewById(R.id.destpic);
        option = itemView.findViewById(R.id.destoption);
        delete = itemView.findViewById(R.id.destdelete);

    }
}
