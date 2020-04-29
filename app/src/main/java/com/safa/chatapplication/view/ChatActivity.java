package com.safa.chatapplication.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeTransform;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.safa.chatapplication.R;
import com.safa.chatapplication.adapter.ChatRecycleViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private EditText chatMessageET;
    private ChatRecycleViewAdapter chatRecycleViewAdapter;
    private ArrayList<String> chatMessageList = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.chat_recyclerView);
        chatMessageET = findViewById(R.id.chat_editText);

        chatRecycleViewAdapter = new ChatRecycleViewAdapter(chatMessageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(chatRecycleViewAdapter);
        chatRecycleViewAdapter.notifyDataSetChanged();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        getData();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.options_menu_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.options_menu_signout:
                mAuth.signOut();
                intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onSendMessage(View view) {
        String messageToSend = chatMessageET.getText().toString();

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        FirebaseUser user = mAuth.getCurrentUser();
        String userEmail = user.getEmail().toString();

        myRef.child("Chats").child(uuidString).child("useremail").setValue(userEmail);
        myRef.child("Chats").child(uuidString).child("usermessage").setValue(messageToSend);
        myRef.child("Chats").child(uuidString).child("usermessagetime").setValue(ServerValue.TIMESTAMP);

        getData();

    }

    private void getData(){
        DatabaseReference newMyRef = database.getReference("Chats");
        Query query = newMyRef.orderByChild("usermessagetime");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessageList.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    String userMessage = hashMap.get("usermessage");
                    String userEmail = hashMap.get("useremail");

                    chatMessageList.add(userEmail + ": "+userMessage);

                    chatRecycleViewAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
