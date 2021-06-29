package com.example.booksharing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class Profile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    private StorageReference ProductImagesRef;
    ImageView imageView;
    EditText editText;

    TextView textView;
    String message,downloadImageUrl;
    Uri uriProfileImage;
    ProgressBar progressBar;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView=findViewById(R.id.textViewVerified);
        mAuth=FirebaseAuth.getInstance();
        editText=findViewById(R.id.editTextprofile);
        imageView=findViewById(R.id.imageViewprofile);
        progressBar=findViewById(R.id.progressbarprofile);
        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Profile Image");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();

            }
        });
        loadUserInformation();

        findViewById(R.id.buttonsave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserinformation();
                switch (v.getId())
                {
                    case R.id.imageViewprofile:
                        uploadImageToFirebaseStorage();
                        break;

                    case R.id.editTextprofile:
                        startActivity(new Intent(Profile.this,FieldBranch.class));
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }


    private void loadUserInformation() {
        final FirebaseUser user=mAuth.getCurrentUser();
        if(user !=null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(imageView);
            }
            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());
            }
            if(user.isEmailVerified())
            {
                textView.setText("Email Verified");
            }else
            {
                textView.setText("Email Not Verified(Click to Verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Profile.this,"Verification Email Sent",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }

        }
    }

    private void saveUserinformation() {
        String displayName=editText.getText().toString();
        if (displayName.isEmpty())
        {
            editText.setError("Name Require..");
            editText.requestFocus();
            return;
        }
        FirebaseUser user=mAuth.getCurrentUser();


        if (user != null && downloadImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(Uri.parse(downloadImageUrl)).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CHOOSE_IMAGE && resultCode==RESULT_OK && data != null &&data.getData()!=null)
        {
            uriProfileImage= data.getData();
            try {

                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageView.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference filepath=ProductImagesRef.child(uriProfileImage.getLastPathSegment()+".jpg");
        final UploadTask uploadTask=filepath.putFile(uriProfileImage);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                message=e.toString();
                Toast.makeText(Profile.this,"Error:..",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile.this,"Successfull Upload..",Toast.LENGTH_SHORT).show();
                Task<Uri>UriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        else if (task.isSuccessful())
                        {
                            Intent intent=new Intent(Profile.this,FieldBranch.class);
                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        downloadImageUrl= filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                });
            }
        });
    }

    private void SavePrifileInfotoDatabase() {
        HashMap<String,Object> profileMap=new HashMap<>();
        profileMap.put("proname",editText);
    }

    private void showImageChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select Profile image"),CHOOSE_IMAGE);
    }
}
