
/**************************************************************/
package com.example.projetfinetude;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.graphics.Color;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;

        import de.hdodenhof.circleimageview.CircleImageView;


public class RefClass extends AppCompatActivity {
    private CircleImageView profile_imageprofile;
    private TextView profile_userName,profile_phone,profile_email,profile_birthdate;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference UserRef,postRef,likeRef,comentRef;
    RecyclerView recyclerView ;
    FirebaseRecyclerAdapter<Posts, MyPostHolder> adapter;
    FirebaseRecyclerOptions<Posts> options;

    String email,phone,birthdate,userkey,userUri,username;

    ImageView logo_agenda,logo_tlfn,logo_adress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_class);
        profile_imageprofile = findViewById(R.id.porfile_imageporfile);
        profile_userName = findViewById(R.id.profile_username);
        profile_phone = findViewById(R.id.profile_userphone);
        logo_agenda=findViewById(R.id.ic_prfltlfn);
        logo_agenda.setColorFilter(Color.BLUE);
        logo_tlfn=findViewById(R.id.ic_prflagenda);
        logo_tlfn.setColorFilter(Color.BLUE);
        logo_adress=findViewById(R.id.ic_prflemail);
        logo_adress.setColorFilter(Color.BLUE);
        userkey = getIntent().getStringExtra("postuserkey");
        userUri = getIntent().getStringExtra("postuserimage");
        username = getIntent().getStringExtra("postusername");
        profile_email = findViewById(R.id.profile_email);
        profile_birthdate = findViewById(R.id.profile_birthdate);
        recyclerView = findViewById(R.id.profilerecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setFitsSystemWindows(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference().child("User").child(userkey);
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Picasso.get().load(userUri).into(profile_imageprofile);
                    profile_userName.setText(username);
                    birthdate = snapshot.child("birthdate").getValue().toString();
                    phone = snapshot.child("phone").getValue().toString();
                    email = snapshot.child("email").getValue().toString();
                    profile_email.setText(email);
                    profile_phone.setText(phone);
                    profile_birthdate.setText(birthdate);

                }else {
                    Toast.makeText(RefClass.this, "Verifie votre db", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        LoadsProfilPost();
    }

    public void LoadsProfilPost(){
        options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postRef,Posts.class).build();
        adapter = new FirebaseRecyclerAdapter<Posts, MyPostHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyPostHolder holder, int position, @NonNull Posts model) {
                String keyRef = getRef(position).getKey();
                if(model.getUserUId().equals(userkey)){
                    Picasso.get().load(model.getImagePost()).into(holder.imageView);
                }
                else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
            }

            @NonNull
            @Override
            public MyPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image,parent,false);
                return new MyPostHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);



    }
}