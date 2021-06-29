package com.example.booksharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    ImageView button_dBooks;
    ImageView button_dStationary;
    ImageView button_dNotes;
    ImageView button_dReferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.senior_profile:
                Intent mi = new Intent(Home.this, senior_profile_display.class);
                startActivity(mi);
                break;
            case R.id.seniorLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        button_dBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Home.this,donee_books_list.class);
                startActivity(in);
            }
        });


        button_dStationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1=new Intent(Home.this,donee_stationary.class);
                startActivity(in1);


            }
        });
        button_dNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2=new Intent(Home.this,donee_notes.class);
                startActivity(in2);


            }
        });
        button_dReferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent in3=new Intent(Home.this,donee_referrnce.class);
                startActivity(in3);
            }
        });





    }

        void init(){
            button_dBooks=(ImageView)findViewById(R.id.jimageView11);
            button_dStationary=(ImageView)findViewById(R.id.jimageView15);
            button_dNotes=(ImageView)findViewById(R.id.jimageView13);
            button_dReferences=(ImageView)findViewById(R.id.jimageView14);

        }
    }
