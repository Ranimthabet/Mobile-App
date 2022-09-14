package com.example.projetfinetude;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAvisViewHolder extends RecyclerView.ViewHolder {
    CircleImageView avis_userimage;
    ImageView option;
    TextView avis_username,avis_time,avis_text;
    Button btn_delete;
    public MyAvisViewHolder(@NonNull View itemView) {
        super(itemView);
        avis_userimage = itemView.findViewById(R.id.singavis_userimage);
        avis_username = itemView.findViewById(R.id.singleavis_username);
        avis_time = itemView.findViewById(R.id.singleavis_time);
        avis_text = itemView.findViewById(R.id.singleavis_text);
        option = itemView.findViewById(R.id.optioneavis);
        btn_delete = itemView.findViewById(R.id.deleteavis);

    }
}
