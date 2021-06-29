package com.example.booksharing;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder_M extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder_M(View itemView) {
        super(itemView);

        mView = itemView;

        //item click
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mClickListener.onItemClick(view, getAdapterPosition());
//            }
//        });
//        //item long click
//        itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                mClickListener.onItemLongClick(view, getAdapterPosition());
//                return true;
//            }
//        });

    }

    public void setDetails(Context ctx, String AuthersName, String Bookname, String url,String Datails,String MobileNO,String Doner){
        //Views
        TextView mAuther = mView.findViewById(R.id.bAuthername);
        TextView mBook = mView.findViewById(R.id.bBookname);
        TextView mDetails = mView.findViewById(R.id.bDetail);
        TextView mMobno = mView.findViewById(R.id.bMobileNo);
        TextView mDonors = mView.findViewById(R.id.bDonor);
        ImageView mImg = mView.findViewById(R.id.url);
        //set data to views
        mAuther.setText(AuthersName);
        mBook.setText(Bookname);
        mDetails.setText(Datails);
        mMobno.setText(MobileNO);
        mDonors.setText(Doner);
        Picasso.get().load(url).into(mImg);
    }

    private ViewHolder.ClickListener mClickListener;

    //interface to send callbacks
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View  view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}


