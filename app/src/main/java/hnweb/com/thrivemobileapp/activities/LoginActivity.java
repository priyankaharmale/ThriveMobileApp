package hnweb.com.thrivemobileapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.application.AppAPI;
import hnweb.com.thrivemobileapp.application.MainApplication;
import hnweb.com.thrivemobileapp.firebase.MyFirebaseInstanceIDService;
import hnweb.com.thrivemobileapp.utility.CheckConnectivity;
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.ToastUlility;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userNameET, passwordET;
    private LoadingDialog loadingDialog;
    private SharedPreferences prefs;
    SharedPreferences pref1;
    SharedPreferences.Editor editor1;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameET = (EditText) findViewById(R.id.userNameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        loadingDialog = new LoadingDialog(this);
        prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = prefs.edit();

        pref1 = getApplicationContext().getSharedPreferences("MyPref1", MODE_PRIVATE);
        editor1 = pref1.edit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBTN:
                if (TextUtils.isEmpty(userNameET.getText().toString().trim()) && TextUtils.isEmpty(passwordET.getText().toString().trim())) {
                    userNameET.setError("Please enter username.");
                    passwordET.setError("Please enter password.");
                } else {
                    if (TextUtils.isEmpty(userNameET.getText().toString().trim())) {
                        userNameET.setError("Please enter username.");

                    } else if (TextUtils.isEmpty(passwordET.getText().toString().trim())) {
                        passwordET.setError("Please enter password.");
                    } else {

                        //MyFirebaseInstanceIDService fcmtokan = new MyFirebaseInstanceIDService();
                        Log.d("Tokan","0");
                        String t = MyFirebaseInstanceIDService.sendRegistrationToServer();
                        //String t = fcmtokan.sendRegistrationToServer();
                        Log.d("Tokan","1");

                        String FCMtoken = "";
                        if(t.equals("")){
                            Log.d("Tokan","t-NULL");
                            String fcmtokenkey = pref1.getString("fcmkey","notoken");
                            Log.e("fcmkey-",fcmtokenkey);
                            FCMtoken = fcmtokenkey;
                        }else {
                            Log.d("Tokan",t);
                            FCMtoken = t;
//                                    editor.putString("fcmtokenkey",t);
                        }

                        //call web service
                        doLogin(userNameET.getText().toString().trim(), passwordET.getText().toString().trim(),FCMtoken);
                    }
                }
                break;
            case R.id.forgotPaswordTV:
                forgotPasswordDialog();
                break;
        }
    }

    public void doLogin(final String userName, final String password, final String fcmtoken) {

        Log.e("LOGIN-fcmtoken", fcmtoken);

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("LOGIN", response + " link "+AppAPI.loginUrl);

                try {
                    JSONObject jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    String message = jobj.getString("message");

                    if (message_code == 1) {

//                        JSONArray jsonArray = jobj.getJSONArray("response");

                        String TutorID = jobj.getString("TutorID");
                        String FName = jobj.getString("FName");
                        String LName = jobj.getString("LName");
                        String Address = jobj.getString("Address");
                        String City = jobj.getString("City");
                        String State = jobj.getString("State");
                        String ZipCode = jobj.getString("ZipCode");
                        String Phone1 = jobj.getString("Phone1");
                        String Phone2 = jobj.getString("Phone2");
                        String Email = jobj.getString("Email");
                        String IsActive = jobj.getString("IsActive");
                        String SendWelcomeEmail = jobj.getString("SendWelcomeEmail");
                        String EmployeeNbr = jobj.getString("EmployeeNbr");
                        String BirthDate = jobj.getString("BirthDate");
                        String BackgroundClearance = jobj.getString("BackgroundClearance");
                        String TBTest = jobj.getString("TBTest");
                        String GradePref = jobj.getString("GradePref");
                        String Degree = jobj.getString("Degree");
                        String Language = jobj.getString("Language");
                        String TravelPref = jobj.getString("TravelPref");
                        String Notes = jobj.getString("Notes");
                        String TutorNbr = jobj.getString("TutorNbr");
                        String DateEntered = jobj.getString("DateEntered");


                        editor.putString("TutorID", TutorID);
                        editor.putString("FName", FName);
                        editor.putString("LName", LName);
                        editor.putString("Address", Address);
                        editor.putString("City", City);
                        editor.putString("State", State);
                        editor.putString("ZipCode", ZipCode);
                        editor.putString("Phone1", Phone1);
                        editor.putString("Phone2", Phone2);
                        editor.putString("Email", Email);
                        editor.putString("IsActive", IsActive);
                        editor.putString("SendWelcomeEmail", SendWelcomeEmail);
                        editor.putString("EmployeeNbr", EmployeeNbr);
                        editor.putString("BirthDate", BirthDate);
                        editor.putString("BackgroundClearance", BackgroundClearance);
                        editor.putString("TBTest", TBTest);
                        editor.putString("GradePref", GradePref);
                        editor.putString("Degree", Degree);
                        editor.putString("Language", Language);
                        editor.putString("TravelPref", TravelPref);
                        editor.putString("Notes", Notes);
                        editor.putString("TutorNbr", TutorNbr);
                        editor.putString("DateEntered", DateEntered);
                        editor.putString("Username", userName);
                        editor.putString("Password", password);
                        editor.commit();

                        loadingDialog.dismiss();

//                        ToastUlility.show(LoginActivity.this, message);
                        //Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                        //startActivity(intent);
                        //finish();

                        updateFCMtoken(fcmtoken,TutorID);


                    } else {
                        loadingDialog.dismiss();
                        ToastUlility.show(LoginActivity.this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
//                ToastUlility.show(getApplicationContext(), "Network Error,please try again");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserName", userName);
                params.put("Password", password);
//                params.put(AppParams.USER_TYPE, type);
                Log.e("PARAMS", params.toString());
                return params;

            }
        };

        String request_tag = "user_login";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    public void updateFCMtoken(final String FCMToken,final String TutorID) {

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.updateToken, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("updateresponse",response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {

                        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();

                        //settingsDialog.dismiss();
                    } else {
                        //settingsDialog.dismiss();
                        ToastUlility.show(LoginActivity.this, jobj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }

            }
        }, new Response.ErrorListener()

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

    public void doForgotPassword(final String email, final Dialog settingsDialog) {

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.forgotPasswordUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {
                        settingsDialog.dismiss();
                    } else {
                        settingsDialog.dismiss();
                        ToastUlility.show(LoginActivity.this, jobj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }

            }
        }, new Response.ErrorListener()

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
                params.put("Email", email);

                Log.e("PARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "user_fp";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);

    }

    public void forgotPasswordDialog() {
        final Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(this.getLayoutInflater().inflate(R.layout.forgot_password
                , null));
        settingsDialog.setCancelable(true);
        final EditText fppassTV = (EditText) settingsDialog.findViewById(R.id.fp_email_idET);
        Button submitBTN = (Button) settingsDialog.findViewById(R.id.fpsubmitBTN);
        Button fpcancelBTN = (Button) settingsDialog.findViewById(R.id.fpcancelBTN);
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fppassTV.getText().toString().trim().length() > 0 && Patterns.EMAIL_ADDRESS.matcher(fppassTV.getText().toString().trim()).matches()) {
                    String email = fppassTV.getText().toString().trim();
                    if (CheckConnectivity.checkInternetConnection(LoginActivity.this)) {
                        doForgotPassword(email, settingsDialog);
                    }

//                    Toast.makeText(SignInActivity.this, "Under Development ....", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(LoginActivity.this, "Please enter your correct email id", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fpcancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.dismiss();
            }
        });

        settingsDialog.show();
    }

}