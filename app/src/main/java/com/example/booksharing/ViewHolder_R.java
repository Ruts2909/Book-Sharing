package com.example.booksharing;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder_R extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder_R(View itemView) {
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

    public void setDetails(Context ctx, String Link, String Subject, String url,String Datails,String Doner){
        //Views
        TextView mLink = mView.findViewById(R.id.bLink);
        TextView mSubname = mView.findViewById(R.id.bSubjectname);
        TextView mDetails = mView.findViewById(R.id.bDetail);
        TextView mDonors = mView.findViewById(R.id.bDonor);
        ImageView mImg = mView.findViewById(R.id.url);
        //set data to views
        mLink.setText(Link);
        mSubname.setText(Subject);
        mDetails.setText(Datails);
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


