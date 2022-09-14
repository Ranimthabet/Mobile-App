package com.example.projetfinetude;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
public class MyFriendActivity extends AppCompatActivity {
    private FirebaseRecyclerOptions<Friends> userOption;
    private FirebaseRecyclerAdapter<Friends,MyFriendViewHolder> useradapter;
FirebaseAuth fAuth;
FirebaseUser mUser;
DatabaseReference friendRef,userRef ;
Toolbar toolbar ;
RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        toolbar = findViewById(R.id.myfriendtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Friend");
        recyclerView = findViewById(R.id.myfriendRecyler);
        fAuth = FirebaseAuth.getInstance();
        mUser = fAuth.getCurrentUser();
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friend").child(mUser.getUid());
        userRef = FirebaseDatabase.getInstance().getReference().child("User");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LoadFriends( "");


    }

    private void LoadFriends(String s) {
        Query query = friendRef.orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        userOption = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query,Friends.class).build();
        useradapter = new FirebaseRecyclerAdapter<Friends, MyFriendViewHolder>(userOption) {
            @Override
            protected void onBindViewHolder(@NonNull MyFriendViewHolder holder, int position, @NonNull Friends model) {
               String refkey = getRef(position).getKey();
               friendRef.child(refkey).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()) {
                           userRef.child(refkey).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   if(snapshot.exists()){
                                       String cname = snapshot.child("name").getValue().toString()+" "+snapshot.child("lastName").getValue().toString();
                                       String cemail = snapshot.child("email").getValue().toString();
                                       String cuseruri = snapshot.child("imageuri").getValue().toString();
                                       Picasso.get().load(cuseruri).into(holder.my_friend_imageprofile);
                                       holder.my_friend_username.setText(cname);
                                       holder.my_friend_useremail.setText(cemail);
                                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent= new Intent(getApplicationContext(),ChatActivity.class);
                                               intent.putExtra("reciveruid",getRef(position).getKey().toString());
                                               intent.putExtra("recivername",cname);
                                               intent.putExtra("reciverimage",cuseruri);
                                               startActivity(intent);
                                           }
                                       });

                                   }

                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });




                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

                }

            @NonNull
            @Override
            public MyFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_myfriend_view,parent,false);
                return new MyFriendViewHolder(view);
            }
        };
    useradapter.startListening();
    recyclerView.setAdapter(useradapter);
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
                LoadFriends(s);
                return false;
            }
        });


        return true;
    }
    /**********************************************/

}