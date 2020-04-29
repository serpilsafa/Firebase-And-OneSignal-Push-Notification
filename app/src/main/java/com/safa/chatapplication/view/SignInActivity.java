package com.safa.chatapplication.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.safa.chatapplication.R;

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText emailTV, passwordTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();

        emailTV = findViewById(R.id.emailTextView);
        passwordTV = findViewById(R.id.passwordTextView);

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
        }


    }



    public void onSignIn(View view) {

        mAuth.signInWithEmailAndPassword(emailTV.getText().toString(), passwordTV.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        startActivity(intent);

                    }
                });

    }

    public void onSignUp(View view) {
        mAuth.createUserWithEmailAndPassword(emailTV.getText().toString(), passwordTV.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                            startActivity(intent);
                        }else{
                            System.out.println("error: "+ task.getException().getMessage());
                            Toast.makeText(getApplicationContext(), "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
