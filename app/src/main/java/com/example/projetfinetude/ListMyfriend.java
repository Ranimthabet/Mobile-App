package com.example.projetfinetude;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
public class ListMyfriend extends AppCompatActivity {
    private FirebaseRecyclerOptions<Friends> userOption;
    private FirebaseRecyclerAdapter<Friends,MyFriendViewHolder> useradapter;
    FirebaseAuth fAuth;
    FirebaseUser mUser;
    DatabaseReference friendRef,userRef,groupRef,groupmemberRef ,usergroupRef;
    Toolbar toolbar ;
    RecyclerView recyclerView ;
    String groupid ,name,data,group_creator,uri ,userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_myfriend);
        toolbar = findViewById(R.id.myfriendtoolbar);
        groupid = getIntent().getStringExtra("groupid");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Friend");
        recyclerView = findViewById(R.id.myfriendRecyler);
        fAuth = FirebaseAuth.getInstance();
        mUser = fAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friend").child(mUser.getUid());
        groupmemberRef = FirebaseDatabase.getInstance().getReference().child("MemberGroup").child(groupid);
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupid);

        usergroupRef = FirebaseDatabase.getInstance().getReference().child("UserGroup");
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name = snapshot.child("groupe_name").getValue().toString();
                    data = snapshot.child("groupe_information").getValue().toString();
                    uri = snapshot.child("groupe_image").getValue().toString();
                    group_creator = snapshot.child("group_creator").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                /*********************************************/

                /***************************************************************************/
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
                                                MyMemeber myMemeber = new MyMemeber(cname,cemail,cuseruri);
                                                groupmemberRef.child(refkey).setValue(myMemeber);
                                            MyGroup myGroup = new MyGroup(groupid,uri,name,data,group_creator);
                                            usergroupRef.child(refkey).child(groupid).setValue(myGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ListMyfriend.this, "Member added", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(),MemberGroup.class);
                                                    intent.putExtra("groupid",groupid);
                                                    startActivity(intent);
                                                }
                                            });
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
/******************************************/