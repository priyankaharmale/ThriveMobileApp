package hnweb.com.thrivemobileapp.activities;

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
import hnweb.com.thrivemobileapp.adapter.StudentListAdapter;
import hnweb.com.thrivemobileapp.application.AppAPI;
import hnweb.com.thrivemobileapp.application.MainApplication;
import hnweb.com.thrivemobileapp.pojo.StudentList;
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.Logout;
import hnweb.com.thrivemobileapp.utility.ToastUlility;

public class StudentsListActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView studentsRV;
    SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    ArrayList<StudentList> studentListArrayList;
    ImageView logoutIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        loadingDialog = new LoadingDialog(this);
        studentListArrayList = new ArrayList<StudentList>();
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        studentsRV = (RecyclerView) findViewById(R.id.studentsRV);
        studentsRV.setLayoutManager(new LinearLayoutManager(StudentsListActivity.this));
        logoutIV = (ImageView) findViewById(R.id.logoutIV);


        getStudentsList();
    }

    public void getStudentsList() {
        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.studentListForTutor, new Response.Listener<String>() {
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
                            StudentList studentList = new StudentList();
                            studentList.setStudentID(jarr.getJSONObject(i).getString("StudentID"));
                            studentList.setFName(jarr.getJSONObject(i).getString("FName"));
                            studentList.setLName(jarr.getJSONObject(i).getString("LName"));
                            studentList.setAddress(jarr.getJSONObject(i).getString("Address"));
                            studentListArrayList.add(studentList);
                        }
                        studentsRV.setAdapter(new StudentListAdapter(studentListArrayList, StudentsListActivity.this));

                    } else {
                        loadingDialog.dismiss();
                        ToastUlility.show(StudentsListActivity.this, jobj.getString("message"));
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutIV:
                Logout.logout(this);
                break;
        }
    }
}
