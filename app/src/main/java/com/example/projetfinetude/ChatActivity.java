package com.example.projetfinetude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
RecyclerView recyclerView;
EditText inputmessage;
ImageView sendMsg,addPic;
CircleImageView userimage;
TextView username,status;
String groupid,reciverUid,recivername,reciveruri;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
DatabaseReference msgRef;
FirebaseRecyclerOptions<MyChat>options;
FirebaseRecyclerAdapter<MyChat,MyChatViewHolder>adapter;
String Url = "https://fcm.googleapis.com/fcm/send";

RequestQueue requestQueue;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        inputmessage = findViewById(R.id.chat_message);
sendMsg  = findViewById(R.id.chat_send);
addPic = findViewById(R.id.chat_addpic);

requestQueue = Volley.newRequestQueue(this);
recyclerView = findViewById(R.id.chat_recyclerView);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setFitsSystemWindows(true);
userimage = findViewById(R.id.chat_userimage);
    username = findViewById(R.id.chat_username);
    status = findViewById(R.id.chat_userstatut);
    groupid = getIntent().getStringExtra("groupid");
reciverUid = getIntent().getStringExtra("reciveruid");
    recivername = getIntent().getStringExtra("recivername");
    reciveruri = getIntent().getStringExtra("reciverimage");
    firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser=firebaseAuth.getCurrentUser();
    msgRef = FirebaseDatabase.getInstance().getReference().child("Messages");
    Picasso.get().load(reciveruri).into(userimage);
    inputmessage.setBackgroundColor(Color.WHITE);
    username.setText(recivername);
sendMsg.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SendMessage();
    }
});
LoadsMessages();
}
/*************************************************************************/
    private void LoadsMessages() {
    options = new FirebaseRecyclerOptions.Builder<MyChat>().setQuery(msgRef.child(firebaseUser.getUid()).child(reciverUid),MyChat.class).build();
    adapter = new FirebaseRecyclerAdapter<MyChat, MyChatViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull MyChatViewHolder holder, int position, @NonNull MyChat model) {
            if(model.getUserUId().equals(firebaseUser.getUid())){
                holder.firstuserimage.setVisibility(View.GONE);
                holder.firstmessage.setVisibility(View.GONE);
                holder.secondmessage.setVisibility(View.VISIBLE);
                holder.secondmessage.setText(model.getMessage());
            }
            else
            {
                holder.firstuserimage.setVisibility(View.VISIBLE);
                holder.firstmessage.setVisibility(View.VISIBLE);
                holder.secondmessage.setVisibility(View.GONE);
                holder.firstmessage.setText(model.getMessage());
                Picasso.get().load(reciveruri).into(holder.firstuserimage);
            }
        }

        @NonNull
        @Override
        public MyChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_view,parent,false);

            return new  MyChatViewHolder(view);
        }
    };
    adapter.startListening();
    recyclerView.setAdapter(adapter);
    }
/********************************************************************/
    private void SendMessage() {
    String text = inputmessage.getText().toString();
    if(text.isEmpty()){
        Toast.makeText(this, "Please writhe something", Toast.LENGTH_SHORT).show();
    }else
    {

        HashMap hashMap = new HashMap();
        hashMap.put("message",text);
        hashMap.put("status","unseen");
        hashMap.put("userUId",firebaseUser.getUid());
        msgRef.child(reciverUid).child(firebaseUser.getUid()).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
           if(task.isSuccessful()){
               msgRef.child(firebaseUser.getUid()).child(reciverUid).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                   @Override
                   public void onComplete(@NonNull Task task) {
                  if(task.isSuccessful()){
                     // sendNotification(text);
                      inputmessage.setText(null);
                      Toast.makeText(ChatActivity.this, "msg sent", Toast.LENGTH_SHORT).show();
                  }
                   }
               });
           }
            }
        });
    }
    }
/*******************************************************************************/
 /*   private void sendNotification(String text)  {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("to","/topics/"+reciverUid);
            JSONObject jsonObject1= new JSONObject();
            jsonObject1.put("titlle","Message from user");
            jsonObject1.put("body",text);
            jsonObject.put("notification",jsonObject1);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Url,jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put("content_type","application/json");
                    map.put("autorization","key=AAAAQeUdyBY:APA91bEiLd2Oeqc7R2WlpyTMyrnM6XDoF5l-Elf2muoSgz9DZxFPT-7fVj3Dhh90-vRH_Bm8fH3sx75Z1KYsm2uBnWc01mxZ23vBciqI5X7V4KooShVzGAaybwJZ0ABGFdDI-33mT9-N");
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/
/********************************************************************/
}