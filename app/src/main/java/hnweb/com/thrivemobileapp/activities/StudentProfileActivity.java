package hnweb.com.thrivemobileapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.Logout;
import hnweb.com.thrivemobileapp.utility.ToastUlility;

public class StudentProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private LoadingDialog loadingDialog;
    String studentId;
    TextView nameTV, addressTV, cityTV, zipTV, phoneTV, emailTV;
    ImageView logoutIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        loadingDialog = new LoadingDialog(this);
        studentId = getIntent().getExtras().getString("StudentID", "");

        logoutIV = (ImageView) findViewById(R.id.logoutIV);
        nameTV = (TextView) findViewById(R.id.nameTV);
        addressTV = (TextView) findViewById(R.id.addressTV);
        cityTV = (TextView) findViewById(R.id.cityTV);
        zipTV = (TextView) findViewById(R.id.zipTV);
        phoneTV = (TextView) findViewById(R.id.phoneTV);
        emailTV = (TextView) findViewById(R.id.emailTV);

        getStudentsDetails();


    }

    public void getStudentsDetails() {
        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.studentDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {
//                        settingsDialog.dismiss();

                        String FName = jobj.getString("FName");
                        String LName = jobj.getString("LName");
                        String Address = jobj.getString("Address");
                        String City = jobj.getString("City");
                        String State = jobj.getString("State");
                        String ZipCode = jobj.getString("ZipCode");
                        String Phone1 = jobj.getString("Phone1");
                        String Phone2 = jobj.getString("Phone2");
                        String Email = jobj.getString("Email");

                        nameTV.setText(FName + " " + LName);
                        addressTV.setText(Address);
                        cityTV.setText(City);
                        zipTV.setText(ZipCode);
                        phoneTV.setText(Phone1);
                        emailTV.setText(Email);

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loadingDialog.dismiss();
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
                params.put("StudentID", studentId);

                Log.e("PARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "student_list";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutIV:
                Logout.logout(this);
                break;
        }
    }
}
