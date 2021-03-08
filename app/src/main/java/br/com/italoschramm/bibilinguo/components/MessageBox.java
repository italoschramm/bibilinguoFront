package br.com.italoschramm.bibilinguo.components;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

public class MessageBox {

    private Context context;

    public MessageBox(Context context){
        this.context = context;
    }

    public void generateAlert(String message, String title){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    public void generateAlertWithOkNewIntent(String message, String title, Intent intent, Activity activity){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(intent);
            }
        });

        dialog.show();
    }
}
