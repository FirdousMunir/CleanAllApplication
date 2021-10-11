package com.example.cleanapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

public class ProgDialog {

    Context context;
    ProgressDialog progressDialog;

    public ProgDialog(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    public void showProgress() {
        progressDialog.show();
    }

    public void hideProgress() {
        progressDialog.dismiss();
    }


}
