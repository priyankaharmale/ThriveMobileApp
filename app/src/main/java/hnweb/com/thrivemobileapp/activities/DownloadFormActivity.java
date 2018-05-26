package hnweb.com.thrivemobileapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.adapter.DownloadFormsAdapter;
import hnweb.com.thrivemobileapp.application.AppAPI;
import hnweb.com.thrivemobileapp.application.MainApplication;
import hnweb.com.thrivemobileapp.pojo.DownloadPDF;
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.Logout;
import hnweb.com.thrivemobileapp.utility.ToastUlility;

public class DownloadFormActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView logoutIV;
    RecyclerView formsRV;

    SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    ArrayList<DownloadPDF> downloadPDFArrayList;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    private ProgressDialog pDialog;
    ImageView my_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_form);

        loadingDialog = new LoadingDialog(this);
        downloadPDFArrayList = new ArrayList<DownloadPDF>();
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        logoutIV = (ImageView) findViewById(R.id.logoutIV);
        formsRV = (RecyclerView) findViewById(R.id.formsRV);
        formsRV.setLayoutManager(new LinearLayoutManager(this));
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
//        pDialog.show();

        getFilesURLList();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutIV:
                Logout.logout(this);
                break;

        }
    }

    public void getFilesURLList() {
        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.downloadURLS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {
//                        settingsDialog.dismiss();
                        loadingDialog.dismiss();

                        JSONArray jarr = jobj.getJSONArray("response");
                        for (int i = 0; i < jarr.length(); i++) {
                            DownloadPDF studentList = new DownloadPDF();
                            studentList.setFormID(jarr.getJSONObject(i).getString("FormID"));
                            studentList.setFormName(jarr.getJSONObject(i).getString("FormName"));
                            studentList.setCompanyName(jarr.getJSONObject(i).getString("CompanyName"));
                            studentList.setDescription(jarr.getJSONObject(i).getString("Description"));
                            studentList.setFileLocation(jarr.getJSONObject(i).getString("FileLocation"));
                            studentList.setUploadDate(jarr.getJSONObject(i).getString("UploadDate"));
                            downloadPDFArrayList.add(studentList);
                        }
                        formsRV.setAdapter(new DownloadFormsAdapter(downloadPDFArrayList, DownloadFormActivity.this,pDialog));

                    } else {
                        loadingDialog.dismiss();
                        ToastUlility.show(DownloadFormActivity.this, jobj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("TutorID", sharedPreferences.getString("TutorID", ""));

                Log.e("PARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "student_list";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

}
