package com.example.alineshop;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NextActivity extends AppCompatActivity {

    private EditText editName, editColor, editSize;
    private Button add, save;
    private ImageView imageView;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    ActivityResultLauncher<String> mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        editName = findViewById(R.id.editName);
        editColor = findViewById(R.id.editColor);
        editSize = findViewById(R.id.editSize);
        add = findViewById(R.id.add);
        save = findViewById(R.id.save);
        imageView = findViewById(R.id.imageView);

        save.setEnabled(false); //disables button

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        StorageReference mountainsRef = storageReference.child("IMG_20220619_003631.jpg");
        StorageReference mountainImagesRef = storageReference.child("images/IMG_20220619_003631.jpg");

        editName.setText(mountainsRef.getPath());

        mPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageUri = result;
                imageView.setImageURI(result);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSize.setText("yes");
                Toast.makeText(NextActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
//                mPhoto.launch("image/*");
//                editColor.setText(imageUri.getPath());
//                Uri file = Uri.fromFile(new File(imageUri.getPath()));
//                StorageReference riversRef = storageReference.child(file.getLastPathSegment());
//                editSize.setText(file.getLastPathSegment());
//                UploadTask uploadTask = riversRef.putFile(file);
//
//                // Register observers to listen for when the download is done or if it fails
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Toast.makeText(NextActivity.this, "Failed upload image", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                        Toast.makeText(NextActivity.this, "Success upload image", Toast.LENGTH_SHORT).show();
//                    }
//                });



//                String name = editName.getText().toString();
//                String color = editColor.getText().toString();
//                String size = editSize.getText().toString();
//                if(name.isEmpty() || false){
//                    Toast.makeText(NextActivity.this, "No name entered", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    //addDress(name, color,size);
//                    mPhoto.launch("image/*");
//                    editName.setText(FirebaseStorage.getInstance().getReference().toString());
//                    editSize.setText(imageUri.toString());
//                    uploadImage();
//                }
            }
        });
    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        editColor.setText(imageUri.getPath());

        Uri file = Uri.fromFile(new File(imageUri.getPath()));

        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //imageView.setImageURI(null);
                Toast.makeText(NextActivity.this, "Success upload image", Toast.LENGTH_SHORT).show();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(NextActivity.this, "Failed upload Image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDress(String name, String color, String size){
        //.push() creates a unique Id, create more than 1 branch at a time using hashmap
        HashMap<String,Object> map = new HashMap<>();
        map.put("name", name);
        map.put("color", color);
        map.put("size", size);
        FirebaseDatabase.getInstance().getReference().child("Dresses").push().updateChildren(map);

        Toast.makeText(NextActivity.this, "Added new Item", Toast.LENGTH_SHORT).show();

        editName.getText().clear();
        editColor.getText().clear();
        editSize.getText().clear();
    }

}