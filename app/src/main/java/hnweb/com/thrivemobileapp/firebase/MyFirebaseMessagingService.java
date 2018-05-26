package hnweb.com.thrivemobileapp.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Random;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.activities.SplashActivity;

/**
 * Created by hnwebmarketing on 11/6/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String TicketStatusCheck = "";
    String Password, TutorID, Username;
    Intent intent;
    PendingIntent pendingIntent;
    Context context = this;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        editor = prefs.edit();

        String mailid = sharedPreferences.getString("Email", "");
        Log.e("mailid-", mailid);
        TutorID = sharedPreferences.getString("TutorID", "");
        Log.e("TutorID-", TutorID);
        Username = sharedPreferences.getString("Username", "");
        Log.e("Username-", Username);
        Password = sharedPreferences.getString("Password", "");
        Log.e("Password-", Password);

        //Displaying data in log         //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Received: " + remoteMessage);
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().toString());

        String CurrentString = remoteMessage.getData().toString();
        String[] separated = CurrentString.split("=");
        String a = separated[0]; // this will contain "Fruit"
        String b = separated[1]; // this will contain " they taste good"
        Log.d(TAG, "separated String a= : " + a);
        Log.d(TAG, "separated String b= : " + b);

        String str = method(b);
        Log.d(TAG, "My String is : " + str);

        sendNotification(str);

    }
//==================================================================================================
    private void sendNotification(String str) {

        String title = null, message = null;
        Log.d(TAG, "messageBody: " + str);

        final int NOTIFY_ID = 1002;
        // There are hardcoding only for show it's just strings
        //String name = "hnweb.com.thrivemobileapp_channel";
        //String id = "hnweb.com.thrivemobileapp_channel_1"; // The user-visible name of the channel.
        //String description = "hnweb.com.thrivemobileapp_first_channel"; // The user-visible description of the channel.


        /*OREO OS Push Notification */
        //https://stackoverflow.com/questions/45668079/notificationchannel-issue-in-android-o
        //https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase/38795553#38795553
        //https://stackoverflow.com/questions/43093260/notification-not-showing-in-android-8-oreo
        //https://stackoverflow.com/questions/46990995/on-android-8-oreo-api-26-and-later-notification-does-not-display
        //https://stackoverflow.com/questions/44489657/android-o-reporting-notification-not-posted-to-channel-but-it-is
        //

        try {

            JSONObject jsonObject = new JSONObject(str);
//==================================================================================================
            title = jsonObject.getString("title");
            Log.d("title:- ", title);
            message = jsonObject.getString("description");
            Log.d("message:- ", message);
            //String notificationtype = jsonObject.getString("notificationtype");
            //Log.d("notificationtype:- ", notificationtype);
//==================================================================================================
            Log.e("DisplayNoti", "Notification Display Continue Here");

            if (Username.equals("") && Password.equals("")) {
                Log.e("GenerateNotification", "Not Login No Notification");
            } else {


                intent = new Intent(this, SplashActivity.class);
                //intent = new Intent(this, DashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    // The id of the channel.
                    String id = "my_channel_01";
                    // The user-visible name of the channel.
                    CharSequence name = getString(R.string.channel_name);
                    // The user-visible description of the channel.
                    String description = getString(R.string.channel_description);
                    int importance = NotificationManager.IMPORTANCE_LOW;
                    NotificationChannel mChannel = null;

                    mChannel = new NotificationChannel(id, name,importance);

                    // Configure the notification channel.
                    mChannel.setDescription(description);
                    mChannel.enableLights(true);
                    // Sets the notification light color for notifications posted to this
                    // channel, if the device supports this feature.
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mNotificationManager.createNotificationChannel(mChannel);

                    mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    // Sets an ID for the notification, so it can be updated.
                    int notifyID = 1;
                    // The id of the channel.
                    String CHANNEL_ID = "my_channel_01";
                    // Create a notification and set the notification channel.
                    Notification notification = new Notification.Builder(this)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSmallIcon(R.mipmap.app_icon)
                            .setChannelId(CHANNEL_ID)
                            .build();

                    //display multiple notifications in android Using below Code.
                    Random random = new Random();
                    int m = random.nextInt(9999 - 1000) + 1000;
                    // Issue the notification.
                    mNotificationManager.notify(m, notification);

                }else {
                    /*intent = new Intent(this, SplashActivity.class);
                    //intent = new Intent(this, DashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 0, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                    Log.d("type:- ", "typep");*/

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.app_icon)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    //display multiple notifications in android Using below Code.
                    Random random = new Random();
                    int m = random.nextInt(9999 - 1000) + 1000;
                    notificationManager.notify(m, notificationBuilder.build());
                }


                //GenerateNotification(title, message);
                Log.e("GenerateNotification", "NearByUser");
            }


//==================================================================================================
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.getMessage());
        }
//==================================================================================================
    }
//==================================================================================================
    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '}') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
//==================================================================================================
    public void GenerateNotification(String title, String message) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //display multiple notifications in android Using below Code.
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notificationBuilder.build());

    }
//==================================================================================================
}
