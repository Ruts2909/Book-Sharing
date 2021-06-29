package com.example.booksharing;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class senior_profile_display extends AppCompatActivity {

    private static final String TAG ="senior_profile_display" ;
    private FirebaseDatabase mfirebasedatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistener;
    private DatabaseReference mRef;
    private String uid;
    ListView mlistview;
    TextView fname_t,lname_t,email_t,mobile_t,collegeid_t;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_profile_display);
        mAuth= FirebaseAuth.getInstance();
        mRef=FirebaseDatabase.getInstance().getReference("profileinfo");
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        mlistview=findViewById(R.id.ListView);



        mRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Firstname = dataSnapshot.child("firstname").getValue(String.class);
                String Lastname = dataSnapshot.child("lastname").getValue(String.class);
                String Email = dataSnapshot.child("email").getValue(String.class);
                String Mobilenumber = dataSnapshot.child("mobilenumber").getValue(String.class);
                String Collegeid = dataSnapshot.child("collegeid").getValue(String.class);

                    ArrayList<String> array=new ArrayList<>();
                    array.add(Firstname);
                    array.add(Lastname);
                    array.add(Email);
                    array.add(Mobilenumber);
                    array.add(Collegeid);
                    ArrayAdapter adapter=new ArrayAdapter(senior_profile_display.this,android.R.layout.simple_list_item_1,array);
                    mlistview.setAdapter(adapter);


                //showdata(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*private void showdata(DataSnapshot dataSnapshot) {

    }*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void toastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

    }
}
