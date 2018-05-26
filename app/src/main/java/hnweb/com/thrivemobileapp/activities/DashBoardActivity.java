package hnweb.com.thrivemobileapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.application.AppAPI;
import hnweb.com.thrivemobileapp.application.MainApplication;
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.Logout;
import hnweb.com.thrivemobileapp.utility.ToastUlility;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {
    Intent intent;
    private LoadingDialog loadingDialog;
    SharedPreferences sharedPreferences;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        loadingDialog = new LoadingDialog(this);

        prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        editor = prefs.edit();

        try{
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("SplashrefreshedToken",refreshedToken);

            String tutorID = sharedPreferences.getString("TutorID", "");
            Log.d("SplashtutorID",tutorID);

            if(tutorID.equals("")){
                Log.d("SplashtutorID","nothingtodo");
            }else {
                updateFCMtoken(refreshedToken,tutorID);
            }

        }catch (Exception e){
            Log.e("Exception",e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.accountSummeryTV:
                intent = new Intent(this, TutorProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.attendenceReportTV:
                intent = new Intent(this, AttendenceReportActivity.class);
                startActivity(intent);
                break;
            case R.id.studentsTV:
                intent = new Intent(this, StudentsListActivity.class);
                startActivity(intent);
                break;
            case R.id.inoutAttendenceTV:
                intent = new Intent(this, InOutAttendenceActivity.class);
                intent.putExtra("StudentId","");
                startActivity(intent);
                break;
            case R.id.downloadFormTV:
                intent = new Intent(this, DownloadFormActivity.class);
                startActivity(intent);
                break;
            case R.id.timeCardTV:
                intent = new Intent(this, TimeCardActivity.class);
                startActivity(intent);
                break;
            case R.id.logoutBTN:
                Logout.logout(this);
                break;
            case R.id.progrssLL:
                intent = new Intent(this,ProgressReportUploadActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void updateFCMtoken(final String FCMToken,final String TutorID) {

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, AppAPI.updateToken, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("updateresponse",response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {

                       /* Intent intent = new Intent(SplashActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();*/
                        /*if (TextUtils.isEmpty(sharedPreferences.getString("TutorID", ""))) {
                            Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(DashBoardActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                        }*/

                    } else {
                        //settingsDialog.dismiss();
                        ToastUlility.show(DashBoardActivity.this, jobj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }

            }
        }, new com.android.volley.Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                ToastUlility.show(getApplicationContext(), "Network Error,please try again");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", TutorID);
                params.put("fcmtoken", FCMToken);
                params.put("devicetype", "Android");
                Log.e("tokenPARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "user_fp";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
