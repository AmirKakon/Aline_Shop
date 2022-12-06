package com.example.alineshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewProfileActivity extends AppCompatActivity {
    private GlobalVariables glblVbls;

    private ImageView m_profilePic;
    private TextView m_name, m_id, m_email, m_phoneNumber;
    private Button m_editBtn;

    private  FirebaseUser fb_user;
    private DatabaseReference fb_RTDatabase;
    private StorageReference fb_storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        initialSetup();

        getAllUserData();

        m_editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this, EditProfileActivity.class));
            }
        });
    }

    private void initialSetup() {
        m_profilePic = findViewById(R.id.profilePic);
        m_name = findViewById(R.id.nameTV);
        m_id = findViewById(R.id.userIdTV);
        m_email = findViewById(R.id.userEmailTV);
        m_phoneNumber = findViewById(R.id.userPhoneNumberTV);
        m_editBtn = findViewById(R.id.editbutton);

        glblVbls = (GlobalVariables) getApplicationContext();
        fb_user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void getAllUserData() {
        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching user data...");
        progressDialog.show();

        if(fb_user != null) {
            fb_RTDatabase = FirebaseDatabase.getInstance().getReference().child(glblVbls.getGv_userInformationFBRTDB()).child(fb_user.getUid());
            fb_storage = FirebaseStorage.getInstance().getReference().child(glblVbls.getGv_profilePictureFBCS()).child(fb_user.getUid()+ glblVbls.getGv_profilePictureImageType()); //[(7)]

            if(fb_RTDatabase.get() != null)
                getUserInformationFromDatabase();
            else
                Toast.makeText(this, "RealTime Database fetch failed", Toast.LENGTH_SHORT).show();

            if (fb_storage != null)
                getUserImageFromStorage();
            else
                Toast.makeText(this, "Storage fetch failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "User sign-in failed", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    private void getUserImageFromStorage() {

        fb_storage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    try {
                        File localFile = File.createTempFile("tempfile", glblVbls.getGv_profilePictureImageType());
                        fb_storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                m_profilePic.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewProfileActivity.this, "Get picture failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(ViewProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(ViewProfileActivity.this, "Upload picture failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserInformationFromDatabase(){
        fb_RTDatabase.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserModel currUser = dataSnapshot.getValue(UserModel.class);
                            m_id.setText(fb_user.getUid());
                            m_name.setText(currUser.getName());
                            m_email.setText(currUser.getEmail());
                            m_phoneNumber.setText(currUser.getPhoneNumber());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(ViewProfileActivity.this, "Realtime database getter failed", Toast.LENGTH_SHORT).show();
                        }
                    };
                    fb_RTDatabase.addValueEventListener(postListener);
                }
                else
                    Toast.makeText(ViewProfileActivity.this, "data does not exist", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewProfileActivity.this, "Failed fetching data: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}