package br.com.italoschramm.bibilinguo.components;

import android.content.Context;
import android.content.DialogInterface;

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
}
