package com.safa.chatapplication.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.safa.chatapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private EditText ageET, phoneEt;
    private ImageView imageView;
    private ProgressBar progressBar;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Uri selectedImageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ageET = findViewById(R.id.userAgeET);
        phoneEt = findViewById(R.id.userPhoneET);
        imageView = findViewById(R.id.userImageView);
        progressBar = findViewById(R.id.progressBar);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        getData();
    }

    private void getData(){
       DatabaseReference databaseReferenceProfile = database.getReference("Profile");
       databaseReferenceProfile.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               for (DataSnapshot ds: dataSnapshot.getChildren()) {
                   HashMap<String, String > hashMap = ( HashMap<String, String >) ds.getValue();

                   assert hashMap != null;
                   String useremail = hashMap.get("useremail");
                   assert useremail != null;
                   if (useremail.matches(user.getEmail().toString())){
                       String imageURl = hashMap.get("userImage");
                       final String userAge = hashMap.get("userage");
                       final String userPhone = hashMap.get("userphone");

                       Picasso.get().load(imageURl).into(imageView, new Callback() {
                           @Override
                           public void onSuccess() {
                               progressBar.setVisibility(View.GONE);
                               ageET.setText(userAge);
                               phoneEt.setText(userPhone);
                           }

                           @Override
                           public void onError(Exception e) {

                           }
                       });


                   }

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }

    public void onSelectImage(View view) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    public void onUpload(View view) {

        final String uuidImage = UUID.randomUUID().toString();
        String imageName = "images/" + uuidImage + ".jpg";

        StorageReference storageReferenceForUpload = storageReference.child(imageName);
        storageReferenceForUpload.putFile(selectedImageUrl).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference reference = FirebaseStorage.getInstance().getReference("images/" + uuidImage + ".jpg");

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String uuidProfile = UUID.randomUUID().toString();
                        String downloadURL = uri.toString();

                        String userAge = ageET.getText().toString();
                        String userPhone = phoneEt.getText().toString();

                        databaseReference.child("Profile").child(uuidProfile).child("userImage").setValue(downloadURL);
                        databaseReference.child("Profile").child(uuidProfile).child("useremail").setValue(user.getEmail().toString());
                        databaseReference.child("Profile").child(uuidProfile).child("userage").setValue(userAge);
                        databaseReference.child("Profile").child(uuidProfile).child("userphone").setValue(userPhone);

                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null ){

            selectedImageUrl = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUrl);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
