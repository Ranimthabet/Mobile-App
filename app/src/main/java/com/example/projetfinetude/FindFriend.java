package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FindFriend extends AppCompatActivity {
private FirebaseRecyclerOptions<Users> userOption;
private FirebaseRecyclerAdapter<Users,FindFriendViewHolder> useradapter;
private Toolbar toolbar;
    private DatabaseReference userRef;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private RecyclerView finffriend_recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        toolbar = findViewById(R.id.findfiend_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.ic_addfriend);
        getSupportActionBar().setTitle("Find Friend");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
        finffriend_recyclerView =  findViewById(R.id.recycleFF);
        finffriend_recyclerView.setLayoutManager(new LinearLayoutManager(this));


        LoadUsers("");

    }

    private void LoadUsers(String s) {
        Query query = userRef.orderByChild("email").startAt(s).endAt(s+"\uf8ff");
        userOption = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query,Users.class).build();
        useradapter = new FirebaseRecyclerAdapter<Users, FindFriendViewHolder>(userOption) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull Users model) {
              if(!firebaseUser.getUid().equals(getRef(position).getKey().toString()) )
                    {
                        Picasso.get().load(model.getImageuri()).into(holder.findfriend_imageprofile);
                        holder.findfriend_username.setText(model.getName() + " " + model.getLastName());
                    }
                    else {
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
              }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),ViewFriend.class);
                            intent.putExtra("userKey",getRef(position).getKey().toString());
                            startActivity(intent);
                        }
                    });


            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_find_friend,parent,false);
                return new FindFriendViewHolder(view);
            }
        };
        useradapter.startListening();
        finffriend_recyclerView.setAdapter(useradapter);
    }
/*******************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem menuItem = menu.findItem(R.id.search_user);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LoadUsers(s);
                return false;
            }
        });


        return true;
    }
    /**********************************************/
}