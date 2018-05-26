package hnweb.com.thrivemobileapp.utility;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by HNWEB PC-01 on 8/2/2016.
 */
public class ToastUlility {
    public static void show(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

}
