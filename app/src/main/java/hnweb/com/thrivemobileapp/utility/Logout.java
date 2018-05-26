package hnweb.com.thrivemobileapp.utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import hnweb.com.thrivemobileapp.activities.LoginActivity;

/**
 * Created by neha on 5/26/2017.
 */

public class Logout {

    public static void logout(final Activity context) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context,
                        LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
//                toastDialog(context, "You are logout successfully");
                Toast.makeText(context,
                        "Logged out successfully", Toast.LENGTH_LONG).show();
                SharedPreferences settings = context.getApplicationContext()
                        .getSharedPreferences(context.getPackageName(),
                                Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                context.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();


    }
}
