package hnweb.com.thrivemobileapp.firebase;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by hnwebmarketing on 11/6/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    static String refreshedToken = "";
    SharedPreferences.Editor editor1;


    @Override
    public void onTokenRefresh() {
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("MyPref1", MODE_PRIVATE);
        editor1 = pref1.edit();
        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (refreshedToken.equals("")) {
            Log.d(TAG, "Next Time Generate: " + refreshedToken);
        } else {
            editor1.putString("fcmkey", refreshedToken);
            // Save the changes in SharedPreferences
            editor1.commit(); // commit changes
            Log.d(TAG, "token added to SharedPref " + refreshedToken);

            String fcmtokenkey = pref1.getString("fcmkey", "notoken");
            Log.e("fcmkey-", fcmtokenkey);
        }

        sendRegistrationToServer();
    }

    public static String sendRegistrationToServer() {
        //You can implement this method to store the token on your server
        //Not required for current project
        String regtoken = refreshedToken;
        Log.d(TAG, "Reg token:-" + regtoken);
        return regtoken;
    }


}
