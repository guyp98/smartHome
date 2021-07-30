package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;

public class LoadingPage  {

    private Activity activity;
    private AlertDialog dialog;

     public LoadingPage(Activity myActiviy){
        activity = myActiviy;
    }

    public void startLoadingDialog(){
         AlertDialog.Builder builder = new AlertDialog.Builder(activity);
         LayoutInflater inflater = activity.getLayoutInflater();
         builder.setView(inflater.inflate(R.layout.loading_screen,null));
         builder.setCancelable(false);

         dialog = builder.create();
         dialog.show();

    }


    public void dismissDialog(){
        dialog.dismiss();


    }
}