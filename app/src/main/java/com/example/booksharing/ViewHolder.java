package com.example.booksharing;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import static android.content.Context.*;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String servicestring = Context.DOWNLOAD_SERVICE;


//    String fileurl1,title;



    public ViewHolder(View itemView) {
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

    public void setDetails(Context ctx, String AuthersName, String Bookname, String url,String Datails,String MobileNO,String Doner,String fileurl){
        //Views
        TextView mAuther = mView.findViewById(R.id.bAuthername);
        TextView mBook = mView.findViewById(R.id.bBookname);
        TextView mDetails = mView.findViewById(R.id.bDetail);
        TextView mMobno = mView.findViewById(R.id.bMobileNo);
        TextView mDonors = mView.findViewById(R.id.bDonor);
        ImageView mImg = mView.findViewById(R.id.url);
        TextView mfileurl = mView.findViewById(R.id.bFileurl);
        Button uplfile = mView.findViewById(R.id.uFileurl);
        String fileurl1 = fileurl;
        String title = Bookname;

        //set data to views
        mAuther.setText(AuthersName);
        mBook.setText(Bookname);
        mDetails.setText(Datails);
        mMobno.setText(MobileNO);
        mDonors.setText(Doner);
        if (fileurl.isEmpty()){
             mfileurl.setText("The file is not available");
            uplfile.setVisibility(View.INVISIBLE);}
        else{
            mfileurl.setVisibility(View.INVISIBLE);
            uplfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // downloadpdf(fileurl1,title);
                    DownloadManager.Request request=new DownloadManager.Request(Uri.parse(fileurl1));
                    String tempTitle=title.replace("","");
                    request.setTitle(tempTitle);
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,tempTitle+".pdf");
                    DownloadManager downloadManager  = (DownloadManager) ctx.getApplicationContext().getSystemService(servicestring);
                    request.setMimeType("application/pdf");
                    request.allowScanningByMediaScanner();
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                    downloadManager.enqueue(request);
                }
            });
        }


        Picasso.get().load(url).into(mImg);
    }

   // private void downloadpdf(String fileurl1,String title) {
//        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(fileurl1));
//        String tempTitle=title.replace("","_");
//        request.setTitle(tempTitle);
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
//            request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        }
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,tempTitle+".pdf");
//        DownloadManager downloadManager  = (DownloadManager)context.getSystemService(servicestring);
//        request.setMimeType("application/pdf");
//        request.allowScanningByMediaScanner();
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
////        downloadManager.enqueue(request);

   // }

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


