package com.example.booksharing;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.HashMap;
import java.util.Map;

public class ReferenceSenior extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST=1;
    private Button mButtonUpload;
    private EditText mEditTextSubjectname,mEditTextDetails,mEditTextLink;
    private RadioGroup mRadioButton;

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private StorageReference mStorageRef,mFilename;
    private DatabaseReference mDatabadeRef;
    private StorageTask mUploadTask;

    private Uri mImageUri;
    String CurrentUserID,name;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_senior);
        mButtonUpload=findViewById(R.id.button11);

        mEditTextSubjectname=findViewById(R.id.editText5);
        mEditTextDetails=findViewById(R.id.editText7);
        mEditTextLink=findViewById(R.id.editText1);
        mRadioButton=findViewById(R.id.radioGroup);
        mImageView=findViewById(R.id.imageView);
        mProgressBar=findViewById(R.id.progressbar1);
        mStorageRef= FirebaseStorage.getInstance().getReference("Refernces");
        CurrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mAuth= FirebaseAuth.getInstance();
        mfirebasedatabase= FirebaseDatabase.getInstance();
        mRef = mfirebasedatabase.getReference("profileinfo");
        FirebaseUser user=mAuth.getCurrentUser();
        mDatabadeRef= FirebaseDatabase.getInstance().getReference().child("References");

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

                name=Firstname+" "+Lastname;



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
                    Toast.makeText(ReferenceSenior.this,"Upload in Progress",Toast.LENGTH_SHORT).show();
                }else {

                    uploadFile();
                }
            }
        });
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

            mFilename = mStorageRef.child("references" + mImageUri.getLastPathSegment());
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
//    private void uploadFile(){
//        if(mImageUri!=null){
//            StorageReference fileReference=mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
//            mUploadTask=fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Handler handler=new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProgressBar.setProgress(0);
//                        }
//                    },5000);
//                    Toast.makeText(ReferenceSenior.this,"Upload successfull",Toast.LENGTH_SHORT).show();
//                    Map<String,String> ob1=new HashMap<>();
//                    ob1.put("url",mStorageRef.getDownloadUrl().toString());
//                    ob1.put("Subject Name",mEditTextSubjectname.getText().toString().trim());
//                    ob1.put("Datails",mEditTextDetails.getText().toString().trim());
//                    ob1.put("Link",mEditTextLink.getText().toString().trim());
//                    ob1.put("Doner",name);
//                    String uploadId=mDatabadeRef.push().getKey();
//                    mDatabadeRef.child(uploadId).setValue(ob1);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ReferenceSenior.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                    mProgressBar.setProgress((int) progress);
//                }
//            });
//        }else
//        {
//            Toast.makeText(this," NO file Selected",Toast.LENGTH_SHORT).show();
//        }
//    }

    private  void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            mFilename.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mFilename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.e("11111111111111111", String.valueOf(uri));
                            Log.e("11111111111111111", mStorageRef.getPath().toString());
                            Log.e("11111111111111111", mStorageRef.getStorage().toString());
                            Toast.makeText(ReferenceSenior.this, "Upload successfull", Toast.LENGTH_SHORT).show();
                            Map<String, String> ob1 = new HashMap<>();
                            ob1.put("url", String.valueOf(uri));
                            ob1.put("Subject",mEditTextSubjectname.getText().toString().trim());
                            ob1.put("Datails",mEditTextDetails.getText().toString().trim());
                            ob1.put("Link",mEditTextLink.getText().toString().trim());
                            ob1.put("Doner",name);
                            String uploadId = mDatabadeRef.push().getKey();
                            mDatabadeRef.child(uploadId).setValue(ob1);


                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ReferenceSenior.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);
                    Toast.makeText(ReferenceSenior.this, "File Uploaded", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(this," NO file Selected",Toast.LENGTH_LONG).show();
        }
    }

}
