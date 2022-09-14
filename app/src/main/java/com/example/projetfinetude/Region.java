package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class Region extends AppCompatActivity {
TextView region;
ImageView avis,infoRegion,note,addPic,btn_switch;
String test,userUID;
DatabaseReference databaseReference ;
RecyclerView recyclerView ;
List<MyRegion> myRegionList;
MyRegionViewHolder myRegionViewHolder;
    MyRegion myRegion;
    private final static int GET_PERMISSION_GALERIE =9001;
@Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        test =getIntent().getStringExtra("direction");
        userUID=getIntent().getStringExtra("useruid");
        region = findViewById(R.id.regionname);
        note = findViewById(R.id.regionnote);
        addPic = findViewById(R.id.regionadd);
        avis = findViewById(R.id.regionavis);
        btn_switch = findViewById(R.id.btn_switch);



    addPic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(userUID.equals( "Visiteur")){
                startActivity(new Intent(getApplicationContext(),AddUserRequest.class));

            }

               else {
                Intent intent = new Intent(getApplicationContext(), AddDestinationImage.class);
                intent.putExtra("Direction",test);
                intent.putExtra("useruid",userUID);
                startActivity(intent);
            }

        }
    });
    btn_switch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(userUID.equals( "Visiteur")){
                startActivity(new Intent(getApplicationContext(),AddUserRequest.class));

            }

            else {
                Intent intent = new Intent(getApplicationContext(), UserImageUploaded.class);
                intent.putExtra("Direction",test);
                intent.putExtra("useruid",userUID);
                startActivity(intent);
            }

        }
    });
    note.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(userUID.equals( "Visiteur")){
                startActivity(new Intent(getApplicationContext(),AddUserRequest.class));

            }
            else {
                Intent intent = new Intent(getApplicationContext(), Noter.class);
                Toast.makeText(Region.this, "Please make your note[0-10]", Toast.LENGTH_SHORT).show();
                intent.putExtra("Direction",test);
                intent.putExtra("useruid",userUID);
                startActivity(intent);
            }
        }
    });
        avis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Avis.class);
                intent.putExtra("Direction",test);
                intent.putExtra("useruid",userUID);

                startActivity(intent);
            }
        });

        region.setText(test);
        infoRegion = findViewById(R.id.inforegion);
        infoRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegionInformation.class);
                intent.putExtra("Direction",test);
                startActivity(intent);
            }
        });


    myRegionList =new ArrayList<>();
        recyclerView = findViewById(R.id.regionRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("Directions").child(test);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(!ds.equals(Proprities.class)){
                        myRegion= ds.getValue(MyRegion.class);
                    myRegionList.add(myRegion);


                }
                }
                myRegionViewHolder = new MyRegionViewHolder(myRegionList);
                recyclerView.setAdapter(myRegionViewHolder);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}