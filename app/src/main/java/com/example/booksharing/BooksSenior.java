package com.example.booksharing;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BooksSenior extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextBookname, mEditTextAuthorname, mEditTextDetails, mEditTextNO;
    private RadioGroup mRadioButton;


    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private StorageReference mStorageRef, mfilename;
    private DatabaseReference mDatabadeRef;
    private StorageTask mUploadTask;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    private Uri mImageUri;
    String CurrentUserID, name;
    String uploadId1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_senior);

        mButtonUpload = findViewById(R.id.button);
        //mTextViewShowUploads=findViewById(R.id.showsubmit);
        mEditTextBookname = findViewById(R.id.editText5);
        mEditTextAuthorname = findViewById(R.id.editText7);
        mEditTextDetails = findViewById(R.id.editText11);
        mEditTextNO = findViewById(R.id.editText3);
        mRadioButton = findViewById(R.id.radioGroup);
        mImageView = findViewById(R.id.imageView3);
        mProgressBar = findViewById(R.id.progressbar1);
        mStorageRef = FirebaseStorage.getInstance().getReference("Books");

        CurrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mAuth = FirebaseAuth.getInstance();
        mfirebasedatabase = FirebaseDatabase.getInstance();
        mRef = mfirebasedatabase.getReference("profileinfo");
        FirebaseUser user = mAuth.getCurrentUser();

        mDatabadeRef = FirebaseDatabase.getInstance().getReference().child("books");


//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds:dataSnapshot.getChildren())
//
//                {
//                    Userinformation uinfo=new Userinformation();
//                    String fname=ds.child(CurrentUserID).getValue(Userinformation.class).getFirstname();
//
//                    String lname=ds.child(CurrentUserID).getValue(Userinformation.class).getLastname();
//
//                    name=fname+" "+lname;
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        mRef.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Firstname = dataSnapshot.child("firstname").getValue(String.class);
                String Lastname = dataSnapshot.child("lastname").getValue(String.class);

                name = Firstname + " " + Lastname;
                Log.e("11111111111111111", name);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask!=null && mUploadTask.isInProgress())
                {
                    Toast.makeText(BooksSenior.this,"Upload in Progress",Toast.LENGTH_SHORT).show();
                }else {

                    uploadFile();
                }
            }
        });


        /*mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageActivity();
            }
        });*/
    }

    private void OpenFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageUri=data.getData();
            Picasso.get().load(mImageUri).into(mImageView);

            mfilename = mStorageRef.child("book"+mImageUri.getLastPathSegment());
        }
        {

        }

    }

    @Override
    public ContentResolver getContentResolver() {
        return super.getContentResolver();
    }

    private  String getFileExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }
    private  void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            mfilename.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mfilename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.e("11111111111111111", String.valueOf(uri));
                            Log.e("11111111111111111", mStorageRef.getPath().toString());
                            Log.e("11111111111111111", mStorageRef.getStorage().toString());
                            Toast.makeText(BooksSenior.this, "Upload successfull", Toast.LENGTH_SHORT).show();
                            Map<String, String> ob1 = new HashMap<>();
                            ob1.put("url", String.valueOf(uri));
                            ob1.put("Bookname", mEditTextBookname.getText().toString().trim());
                            ob1.put("AuthersName", mEditTextAuthorname.getText().toString().trim());
                            ob1.put("Datails", mEditTextDetails.getText().toString().trim());
                            ob1.put("MobileNO", mEditTextNO.getText().toString().trim());
                            ob1.put("Doner", name);
                            ob1.put("fileurl","");
                            String uploadId = mDatabadeRef.push().getKey();
                            mDatabadeRef.child(uploadId).setValue(ob1);
                            uploadId1=uploadId;
                            Log.e("uploadid", uploadId);
                            sendfile();



                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BooksSenior.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);
                    Toast.makeText(BooksSenior.this, "File Uploaded", Toast.LENGTH_LONG).show();

                }
            });
        }
        else
        {
            Toast.makeText(this," NO file Selected",Toast.LENGTH_LONG).show();
        }
    }

    private void sendfile() {
        Intent intent = new Intent(this,UploadBooks.class);
        intent.putExtra("msg",uploadId1);
        startActivity(intent);
//        Toast.makeText(BooksSenior.this, uploadId1, Toast.LENGTH_LONG).show();
//        Log.e("uploadid", uploadId1);
    }

   /* private void openImageActivity(){
        Intent intent=new Intent(this,donee_books.class);
        startActivity(intent);
    }*/

}