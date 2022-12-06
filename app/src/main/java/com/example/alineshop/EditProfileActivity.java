package com.example.alineshop;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {
    private GlobalVariables glblVbls;

    private ImageView m_profilePic;
    private Button m_saveBtn;
    private EditText m_firstName, m_lastName, m_phoneNumber;

    private Uri m_filePath;
    private UserModel m_currUser;

    private FirebaseUser fb_user;
    private StorageReference fb_storage;
    private DatabaseReference fb_RTDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initialSetup();

        getAllUserData();

        m_profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        m_saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                updateUserInformation();
            }
        });
    }

    private void initialSetup() {
        m_profilePic = findViewById(R.id.profilePic);
        m_saveBtn = findViewById(R.id.button);
        m_firstName = findViewById(R.id.firstNameET);
        m_lastName = findViewById(R.id.userIdTV);
        m_phoneNumber = findViewById(R.id.userEmailTV);

        glblVbls = (GlobalVariables) getApplicationContext();
        fb_user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void getAllUserData() {
        if(fb_user != null) {
            fb_RTDatabase = FirebaseDatabase.getInstance().getReference().child(glblVbls.getGv_userInformationFBRTDB()).child(fb_user.getUid());
            fb_storage = FirebaseStorage.getInstance().getReference().child(glblVbls.getGv_profilePictureFBCS()).child(fb_user.getUid()+ glblVbls.getGv_profilePictureImageType()); //[(7)]

            if(fb_RTDatabase.get() != null)
                getUserInformationFromDatabase();
            else
                Toast.makeText(this, "RealTime Database fetch failed", Toast.LENGTH_SHORT).show();

            if (fb_storage != null) {
                getUserImageFromStorage();
            }
            else
                Toast.makeText(this, "Storage fetch failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "User sign-in failed", Toast.LENGTH_SHORT).show();
        }
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
                                Toast.makeText(EditProfileActivity.this, "Get picture failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(EditProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "Upload picture failed", Toast.LENGTH_SHORT).show();
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
                            m_currUser = dataSnapshot.getValue(UserModel.class);
                            if(m_currUser != null) {
                                if(!m_currUser.getName().isEmpty()){
                                    m_firstName.setText(m_currUser.getName().substring(0, m_currUser.getName().lastIndexOf(" ")));
                                    m_lastName.setText(m_currUser.getName().substring(m_currUser.getName().lastIndexOf(" ") + 1));
                                }

                                if(!m_currUser.getPhoneNumber().isEmpty())
                                    m_phoneNumber.setText(m_currUser.getPhoneNumber());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(EditProfileActivity.this, "Realtime database getter failed", Toast.LENGTH_SHORT).show();
                        }
                    };
                    fb_RTDatabase.addValueEventListener(postListener);
                }
                else
                    Toast.makeText(EditProfileActivity.this, "data does not exist", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Failed receiving data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SelectImage() {
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.setType(glblVbls.getGv_imageDataType());
        data = Intent.createChooser(data, "Choose image");
        activityResultLauncher.launch(data);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        m_filePath = data.getData();

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), m_filePath);
                        } catch (IOException e) {
                            m_firstName.setText(e.getLocalizedMessage());
                        }
                        m_profilePic.setImageBitmap(bitmap);
                    }
                }
            });

    private void uploadImage() {
        if (m_filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading image...");
            progressDialog.show();

            fb_storage.putFile(m_filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    m_phoneNumber.setText("upload successful");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    m_phoneNumber.setText("upload falied");
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }

    private void updateUserInformation() {
        String firstName = m_firstName.getText().toString().trim();
        String lastName = m_lastName.getText().toString().trim();
        String phoneNumber = m_phoneNumber.getText().toString().trim(); //.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        m_currUser = new UserModel(firstName + " " + lastName,fb_user.getEmail(), phoneNumber);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(firstName + " " + lastName)
                .build();

        fb_user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            fb_RTDatabase.setValue(m_currUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfileActivity.this, "Update Failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            startActivity(new Intent(EditProfileActivity.this, ViewProfileActivity.class));
                            finish();
                        }
                    }
                });
    }
}
//    private ImageView m_profilePicture;
//    private Button m_saveButton, m_addPicButton;
//    private EditText m_firstName, m_lastName, m_phoneNumber;
//    private Uri m_uri;
//    private FirebaseUser m_user;
//    private DatabaseReference m_RTDatabase;
//    private UserModel m_currUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.row_users);
//
//        m_profilePicture = findViewById(R.id.profilePic);
//        m_saveButton = findViewById(R.id.button);
//        m_addPicButton = findViewById(R.id.profilePicButton);
//        m_firstName = findViewById(R.id.firstNameET);
//        m_lastName = findViewById(R.id.userIdTV);
//        m_phoneNumber = findViewById(R.id.userEmailTV);
//
//        m_user = FirebaseAuth.getInstance().getCurrentUser();
//        if (m_user != null) {
//            m_RTDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(m_user.getUid());
//
//            if (!m_user.getDisplayName().isEmpty()) {
//                m_firstName.setText(m_user.getDisplayName().substring(0, m_user.getDisplayName().lastIndexOf(" ")));
//                m_lastName.setText(m_user.getDisplayName().substring(m_user.getDisplayName().lastIndexOf(" ") + 1));
//                if (m_RTDatabase != null) {
//                    m_RTDatabase.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                        @Override
//                        public void onSuccess(DataSnapshot dataSnapshot) {
//                            UserModel currUser = dataSnapshot.getValue(UserModel.class);
//                            m_phoneNumber.setText(currUser.getPhoneNumber());
//                        }
//                    });
//                }
//            }
//            if (m_user.getPhotoUrl() != null) {
//                //https://www.geeksforgeeks.org/android-how-to-upload-an-image-on-firebase-storage/
//                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("profile_pictures/").child(m_user.getUid() + ".jpg");
//                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        //Picasso.get().load(uri).into(m_profilePicture);
//                    }
//                });
//            } else
//                Toast.makeText(this, "Error loading profile pic", Toast.LENGTH_SHORT).show();
//
//            m_addPicButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    data.setType("image/*");
//                    data = Intent.createChooser(data, "Choose file");
//                    activityResultLauncher.launch(data);
//                }
//            });
//
//            m_saveButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String firstName = m_firstName.getText().toString().trim();
//                    String lastName = m_lastName.getText().toString().trim();
//                    String phoneNumber = m_phoneNumber.getText().toString().trim(); //.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
//                    m_currUser = new UserModel(firstName + " " + lastName, m_user.getEmail(), phoneNumber);
//
//                    if (m_uri == null) {
//                        m_uri = m_user.getPhotoUrl();
//                    }
//
//                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                            .setDisplayName(firstName + " " + lastName)
//                            .setPhotoUri(m_uri)
//                            .build();
//
//                    m_user.updateProfile(profileUpdates)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//
//                                        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
//                                                .child("profile_pictures")
//                                                .child(m_user.getUid() + ".jpg");
//                                        storageReference.putFile(m_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                            @Override
//                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                m_RTDatabase.setValue(m_currUser).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void unused) {
//                                                        Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }).addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        Toast.makeText(EditProfileActivity.this, "Update Failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                                startActivity(new Intent(EditProfileActivity.this, ViewProfileActivity.class));
//                                                finish();
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(EditProfileActivity.this, "upload photo fail: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    } else
//                                        Toast.makeText(EditProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                }
//            });
//        }
//    }
//
//    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//        @Override
//        public void onActivityResult(ActivityResult result) {
//            if(result.getResultCode() == Activity.RESULT_OK){
//                Intent data = result.getData();
//                Uri uri = data.getData();
//                m_profilePicture.setImageURI(uri);
//                m_uri = uri;
//            }
//        }
//    });
//}