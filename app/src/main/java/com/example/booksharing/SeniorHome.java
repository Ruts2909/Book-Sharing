package com.example.booksharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SeniorHome extends AppCompatActivity {

    ImageView button_sBooks;
    ImageView button_sStationary;
    ImageView button_sNotes;
    ImageView button_sReferences;
    Button showprofile;
    Button logout_from_senior;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.senior_profile:
                Intent mi = new Intent(SeniorHome.this,senior_profile_display.class);
                startActivity(mi);
                break;
            case R.id.seniorLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_home2);


        init();

        button_sBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(SeniorHome.this,BooksSenior.class);
                startActivity(in);
            }
        });
        button_sStationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1=new Intent(SeniorHome.this,Material_senior.class);
                startActivity(in1);


            }
        });
        button_sNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2=new Intent(SeniorHome.this,Notes_senior.class);
                startActivity(in2);


            }
        });
        button_sReferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in3=new Intent(SeniorHome.this,ReferenceSenior.class);
                startActivity(in3);
            }
        });






    }

    void init(){
        button_sBooks=(ImageView)findViewById(R.id.imageView11);
        button_sStationary=(ImageView)findViewById(R.id.imageView15);
        button_sNotes=(ImageView)findViewById(R.id.imageView13);
        button_sReferences=(ImageView)findViewById(R.id.imageView14);

    }
}

