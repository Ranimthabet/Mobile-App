package com.example.projetfinetude;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyChatViewHolder extends RecyclerView.ViewHolder {
    CircleImageView firstuserimage;
    TextView firstmessage,secondmessage;
    public MyChatViewHolder(@NonNull View itemView) {
        super(itemView);
        firstuserimage = itemView.findViewById(R.id.firstuserprofile);
        firstmessage = itemView.findViewById(R.id.firstusermessage);
        secondmessage = itemView.findViewById(R.id.secondusermessage);

    }
}
