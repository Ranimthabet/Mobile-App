
package com.example.projetfinetude;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
public class AddPostGroup extends AppCompatActivity {
    TextView addPost;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference postGroupRef;
    ImageView addPic,image;
    EditText post_text;
    Uri uri;
    String username,userimage,useruid,imageURI;
    String groupid,groupname,groupimage;
    private static  final int CODE_GALERIE_ACCESS = 9001;
    String postText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_group);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addPost = findViewById(R.id.sendImageToUpload);
        addPic = findViewById(R.id.addImageToUpload);
        image = findViewById(R.id.imageToPost);
        post_text = findViewById(R.id.posttitle);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Post");
        progressDialog.setCanceledOnTouchOutside(false);
        username = getIntent().getStringExtra("username");
        userimage = getIntent().getStringExtra("userimage");
        useruid = getIntent().getStringExtra("useruid");
        groupid = getIntent().getStringExtra("groupid");
        groupname = getIntent().getStringExtra("groupname");
        groupimage = getIntent().getStringExtra("groupuri");
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        postGroupRef = database.getReference().child("Postgroup").child(groupid);
        StorageReference storageReference = storage.getReference().child("PostGroupImages");


        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,CODE_GALERIE_ACCESS);
            }
        });
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postText=post_text.getText().toString();
                if(postText.isEmpty()|| postText.length()<5 ){
                    post_text.setError("Please check your discription");
                    post_text.requestFocus();
                    return;
                }
                if(uri ==null||image.getResources().equals(R.color.quantum_grey600)){
                    Toast.makeText(getApplicationContext(), "Please check your picture to post", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                DateFormat date = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss");
                String timeAgo = date.format(Calendar.getInstance().getTime());
                String key = postGroupRef.push().getKey();
                imageURI = uri.toString();
                storageReference.child(key + ".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String testUri = uri.toString() ;
                                Posts posts = new Posts(useruid,username, postText, timeAgo,testUri , userimage);
                                postGroupRef.child(key).setValue(posts).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "succeully uploaded...", Toast.LENGTH_SHORT).show();
                                       Intent intent= new Intent(getApplicationContext(),SingleGroup.class) ;
                                       intent.putExtra("groupid",groupid);

                                        startActivity(intent);

                                        image.setImageResource(R.color.quantum_grey300);
                                        post_text.setText("");
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                });
            }



        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_GALERIE_ACCESS && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            image.setImageURI(uri);
        }
    }
}
/*****************************************/