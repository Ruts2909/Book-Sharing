package com.example.booksharing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;
import java.security.AccessController;

public class donee_referrnce extends AppCompatActivity {
    LinearLayoutManager mLayoutManager; //for sorting
    SharedPreferences mSharedPref; //for saving sort settings
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donee_referrnce);
        ActionBar actionBar = getSupportActionBar();
        //set title
        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "newest"); //where if no settingsis selected newest will be default

        if (mSorting.equals("newest")) {
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means newest first
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        } else if (mSorting.equals("oldest")) {
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means oldest first
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }

        //RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        //set layout as LinearLayout
        mRecyclerView.setLayoutManager(mLayoutManager);

        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("References");
    }
    private void firebaseSearch(String searchText) {

        //convert string entered in SearchView to lowercase
        String query = searchText.toLowerCase();

        Query firebaseSearchQuery = mRef.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerAdapter<Model_R, ViewHolder_R> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model_R, ViewHolder_R>(
                        Model_R.class,
                        R.layout.row_r1,
                        ViewHolder_R.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder_R viewHolder, Model_R model, int position) {
                        viewHolder.setDetails(getApplicationContext(), model.Link,model.Subject, model.url,model.Datails,model.Doner);
                    }


                    @Override
                    public ViewHolder_R onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder_R viewHolder = super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View mView, int position) {
                                //Views
                                TextView mLink = mView.findViewById(R.id.bLink);
                                TextView mSubnames = mView.findViewById(R.id.bSubjectname);
                                TextView mDetails = mView.findViewById(R.id.bDetail);
                                TextView mDonors = mView.findViewById(R.id.bDonor);
                                ImageView mImg = mView.findViewById(R.id.url);

                                //get data from views
                                String mSubname = mSubnames.getText().toString();
                                String mDetail = mDetails.getText().toString();

                                String mDonor = mDonors.getText().toString();
                                String mlinks=mLink.getText().toString();
                                Drawable mDrawable = mImg.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();


                                //pass this data to new activity
                                Intent intent = new Intent(mView.getContext(), donee_referrnce.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", bytes);
                                intent.putExtra("Link",mlinks);
                                intent.putExtra("Subject Name", mSubname);
                                intent.putExtra("Link Details", mDetail);
                                intent.putExtra("Donor Name", mDonor);
                                startActivity(intent);

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        return viewHolder;
                    }


                };

        //set adapter to recyclerview
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model_R, ViewHolder_R> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model_R, ViewHolder_R>(
                        Model_R.class,
                        R.layout.row_r1,
                        ViewHolder_R.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder_R viewHolder, Model_R model, int position) {
                        viewHolder.setDetails(getApplicationContext(), model.Link,model.Subject, model.url,model.Datails,model.Doner);
                    }

                    @Override
                    public ViewHolder_R onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder_R viewHolder = super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View mView, int position) {
                                //Views
                                TextView mLink = mView.findViewById(R.id.bLink);
                                TextView mSubnames = mView.findViewById(R.id.bSubjectname);
                                TextView mDetails = mView.findViewById(R.id.bDetail);
                                TextView mDonors = mView.findViewById(R.id.bDonor);
                                ImageView mImg = mView.findViewById(R.id.url);


                                //get data from views
                                String mSubname = mSubnames.getText().toString();
                                String mDetail = mDetails.getText().toString();

                                String mDonor = mDonors.getText().toString();
                                String mlinks=mLink.getText().toString();
                                Drawable mDrawable = mImg.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                                //pass this data to new activity
                                Intent intent = new Intent(mView.getContext(), donee_referrnce.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", bytes);
                                intent.putExtra("Link",mlinks);
                                intent.putExtra("Subject Name", mSubname);
                                intent.putExtra("Link Details", mDetail);
                                intent.putExtra("Donor Name", mDonor);
                                startActivity(intent);

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        return viewHolder;
                    }

                };

        //set adapter to recyclerview
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar if it present
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //handle other action bar item clicks here
        if (id == R.id.action_sort) {
            //display alert dialog to choose sorting
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        //options to display in dialog
        String[] sortOptions = {" Newest", " Oldest"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by") //set title
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        // 0 means "Newest" and 1 means "oldest"
                        if (which == 0) {
                            //sort by newest
                            //Edit our shared preferences
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort", "newest"); //where 'Sort' is key & 'newest' is value
                            editor.apply(); // apply/save the value in our shared preferences
                            recreate(); //restart activity to take effect
                        } else if (which == 1) {
                            {
                                //sort by oldest
                                //Edit our shared preferences
                                SharedPreferences.Editor editor = mSharedPref.edit();
                                editor.putString("Sort", "oldest"); //where 'Sort' is key & 'oldest' is value
                                editor.apply(); // apply/save the value in our shared preferences
                                recreate(); //restart activity to take effect
                            }
                        }
                    }
                });
        builder.show();
    }




}