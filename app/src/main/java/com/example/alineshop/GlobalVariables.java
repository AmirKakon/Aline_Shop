package com.example.alineshop;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class GlobalVariables extends Application {
    private String gv_profilePictureFBCS = "profile_pictures";
    private String gv_userInformationFBRTDB = "Users";
    private String gv_profilePictureImageType = ".jpeg";
    private String gv_imageDataType = "image/jpeg";
    private String gv_welcomeGuestText = "Welcome Guest";

    public String getGv_profilePictureFBCS() {return gv_profilePictureFBCS;}

    public String getGv_userInformationFBRTDB() {return gv_userInformationFBRTDB;}

    public String getGv_profilePictureImageType() {return gv_profilePictureImageType;}

    public String getGv_imageDataType() {return gv_imageDataType;}

    public String getGv_welcomeGuestText() {return gv_welcomeGuestText;}
}
