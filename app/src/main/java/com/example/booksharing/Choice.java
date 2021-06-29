package com.example.booksharing;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Choice extends AppCompatActivity {

    ImageView donee;
    ImageView donor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        donee=findViewById(R.id.imageView4);
        donor=findViewById(R.id.imageView5);
        getSupportActionBar().hide();

        donee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Choice.this,Home.class);
                startActivity(intent);
            }
        });
        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(Choice.this,SeniorHome.class);
               startActivity(intent);
            }
        });

    }
}
