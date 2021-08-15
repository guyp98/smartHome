package com.example.realproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DeleteContactDialog extends AppCompatDialogFragment {

    private DeleteDialogListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete contact?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onYesClicked();
                    }
                });


        return builder.create();
    }

    public interface DeleteDialogListener{
        void onYesClicked();


    }

    @Override
    public void onAttach( Context context) {
        super.onAttach(context);

        listener = (DeleteDialogListener) context;
    }
}
