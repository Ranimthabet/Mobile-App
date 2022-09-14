package com.example.projetfinetude;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRegionViewHolder extends RecyclerView.Adapter{
List<MyRegion> myRegionList;

    public MyRegionViewHolder(List<MyRegion> myRegionList) {
        this.myRegionList =myRegionList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image,parent,false);
        ViewRegionHolder viewRegionHolder = new ViewRegionHolder(view);
        return viewRegionHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewRegionHolder viewRegionHolder = (ViewRegionHolder)holder;
        MyRegion myRegion = myRegionList.get(position);
        Picasso.get().load(myRegion.getImageURI()).into(viewRegionHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return myRegionList.size();
    }


    public class ViewRegionHolder extends RecyclerView.ViewHolder{
ImageView imageView;
        public ViewRegionHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagedirection);

        }
    }
}
