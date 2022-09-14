package com.example.projetfinetude;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AllRegion extends AppCompatActivity {
RecyclerView recyclerView ;
FirebaseRecyclerOptions<ListRegion>options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_region);
        recyclerView = findViewById(R.id.recycler_reg);
    }
}