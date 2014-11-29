package com.example.cackharot.geosnap.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {

    private String title;
    private String message;
    private DialogInterface.OnClickListener onClickListener;
    private String negativeBtnText;
    private String positiveBtnText;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(this.title)
                .setMessage(this.message)
                .setPositiveButton(this.positiveBtnText, onClickListener)
                .setNegativeButton(this.negativeBtnText, onClickListener)
                .create();
    }

    public AlertDialogFragment setPositiveBtnText(String positiveBtnText) {
        this.positiveBtnText = positiveBtnText;
        return this;
    }

    public AlertDialogFragment setNegativeBtnText(String negativeBtnText) {
        this.negativeBtnText = negativeBtnText;
        return this;
    }

    public AlertDialogFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public AlertDialogFragment setMessage(String message) {
        this.message = message;
        return this;
    }

    public AlertDialogFragment setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }
}