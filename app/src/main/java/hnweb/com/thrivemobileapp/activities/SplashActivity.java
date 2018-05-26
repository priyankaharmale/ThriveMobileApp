package hnweb.com.thrivemobileapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.application.AppAPI;
import hnweb.com.thrivemobileapp.application.MainApplication;
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.PermissionUtility;
import hnweb.com.thrivemobileapp.utility.ToastUlility;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Intent bundle;
    SharedPreferences sharedPreferences;
    private PermissionUtility putility;
    private ArrayList<String> permission_list;
    private Handler handler = new Handler();
    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL = "https://docs.google.com/forms/d/e/1FAIpQLSdYWHbIHEXXnoTnJ1WNim9NLRZoERFRL8W76P-JywtUgyrKOQ/formResponse";
    //input element ids found from the live form page
    public static final String EMAIL_KEY = "entry.819347022";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        editor = prefs.edit();



        permissions();
//        timedifference();
        PostDataTask postDataTask = new PostDataTask();
//execute asynctask
        postDataTask.execute(URL, deviceInfo());


//        new PostDataTask().execute(SpreadSheetTask.URL, SpreadSheetTask.deviceInfo(SplashActivity.this));
//        int noOfDays = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
//        Log.e("MONTH", String.valueOf(noOfDays));

    }

    @Override
    public void onClick(View view) {

        if (TextUtils.isEmpty(sharedPreferences.getString("TutorID", ""))) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
        }

    }

//    public void timedifference() {
//        String dateStart = "08:30:00";
//        String dateStop = "13:00:00";
//
////HH converts hour in 24 hours format (0-23), day calculation
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//
//        Date d1 = null;
//        Date d2 = null;
//
//        try {
//            d1 = format.parse(dateStart);
//            d2 = format.parse(dateStop);
//
//            //in milliseconds
//            long diff = d2.getTime() - d1.getTime();
//            long diffHours = diff / (60 * 60 * 1000) % 24;
//
//            String time = String.format("%02d:%02d",
//                    TimeUnit.MILLISECONDS.toHours(diff),
//                    TimeUnit.MILLISECONDS.toMinutes(diff) -
//                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)));
//
//            Log.e("test", time + " hours, ");
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }



    public void permissions() {
        putility = new PermissionUtility(this);
        permission_list = new ArrayList<String>();
//        permission_list.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
//        permission_list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permission_list.add(android.Manifest.permission.INTERNET);
        permission_list.add(android.Manifest.permission.ACCESS_WIFI_STATE);
        permission_list.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        permission_list.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permission_list.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        permission_list.add(android.Manifest.permission.READ_CONTACTS);
        permission_list.add(android.Manifest.permission.CAMERA);
//        permission_list.add(android.Manifest.permission.SEND_SMS);
//        permission_list.add(Manifest.permission.READ_SMS);
//        permission_list.add(Manifest.permission.READ_CONTACTS);
//        permission_list.add(Manifest.permission.RECEIVE_SMS);
//        permission_list.add(Manifest.permission.BROADCAST_SMS);
//        permission_list.add(Manifest.permission.READ_PHONE_STATE);

        putility.setListner(new PermissionUtility.OnPermissionCallback() {
            @Override
            public void OnComplete(boolean is_granted) {
                Log.i("OnPermissionCallback", "is_granted = " + is_granted);
                if (is_granted) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//go next

//                            goNextScreen();
                        }
                    }, 3000);

                } else {
                    putility.checkPermission(permission_list);
                }
            }
        });


        putility.checkPermission(permission_list);
    }

    class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String email = contactData[1];
          /*  String subject = contactData[2];
            String message = contactData[3];
            String device = contactData[4];*/
            String postBody = "";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = EMAIL_KEY + "=" + URLEncoder.encode(email, "UTF-8")
                ;
            } catch (UnsupportedEncodingException ex) {
                result = false;
            }

            try {
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            } catch (IOException exception) {
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //Print Success or failure message accordingly
//            Toast.makeText(context, result ? "Message successfully sent!" : "There was some error in sending message. Please try again after some time.", Toast.LENGTH_LONG).show();
        }


    }

    public String deviceInfo() {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;

        String s = "Debug-infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";

        s += "\n RELEASE: " + android.os.Build.VERSION.RELEASE;
        s += "\n BRAND: " + android.os.Build.BRAND;
        s += "\n DISPLAY: " + android.os.Build.DISPLAY;
        s += "\n CPU_ABI: " + android.os.Build.CPU_ABI;
        s += "\n CPU_ABI2: " + android.os.Build.CPU_ABI2;
        s += "\n UNKNOWN: " + android.os.Build.UNKNOWN;
        s += "\n HARDWARE: " + android.os.Build.HARDWARE;
        s += "\n Build ID: " + android.os.Build.ID;
        s += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
        s += "\n SERIAL: " + android.os.Build.SERIAL;
        s += "\n USER: " + android.os.Build.USER;
        s += "\n HOST: " + android.os.Build.HOST;
        s += "\n APK Version: " + version;

        return s;
    }
}
