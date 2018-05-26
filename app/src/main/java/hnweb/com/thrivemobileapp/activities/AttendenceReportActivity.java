package hnweb.com.thrivemobileapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import hnweb.com.thrivemobileapp.adapter.ReportListAdapter;
import hnweb.com.thrivemobileapp.application.AppAPI;
import hnweb.com.thrivemobileapp.application.MainApplication;
import hnweb.com.thrivemobileapp.pojo.AttendanceReport;
import hnweb.com.thrivemobileapp.utility.DateSetDialog;
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.Logout;
import hnweb.com.thrivemobileapp.utility.ToastUlility;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

public class AttendenceReportActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tutorTV, fromTV, toTV, totalTV, noRecordFoundTV;
    Spinner spinner;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    ArrayList<String> studentListArrayList = new ArrayList<String>();
    ArrayList<String> studentIdsList = new ArrayList<String>();
    String tutorID;
    ArrayList<AttendanceReport> attendanceReportList = new ArrayList<AttendanceReport>();
    static boolean subjectSS = false;
    String total;
    LinearLayout bottomLL;
    public static HintSpinner<String> defaultHintSpinner;
    ImageView logoutIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_report);

        loadingDialog = new LoadingDialog(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        tutorID = sharedPreferences.getString("TutorID", "");
        logoutIV = (ImageView) findViewById(R.id.logoutIV);
        tutorTV = (TextView) findViewById(R.id.tutorTV);
        fromTV = (TextView) findViewById(R.id.fromTV);
        toTV = (TextView) findViewById(R.id.toTV);
        spinner = (Spinner) findViewById(R.id.spinner);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalTV = (TextView) findViewById(R.id.totalTV);
        bottomLL = (LinearLayout) findViewById(R.id.bottomLL);
        noRecordFoundTV = (TextView) findViewById(R.id.noRecordFoundTV);

        tutorTV.setText(sharedPreferences.getString("FName", "") + " " + sharedPreferences.getString("LName", ""));

        getStudentsList(this, loadingDialog, studentListArrayList, studentIdsList, spinner, tutorID);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fromTV:
                DateSetDialog.datePicker(fromTV, this);
                break;
            case R.id.toTV:
                DateSetDialog.datePicker(toTV, this);
                break;
            case R.id.searchBTN:
                try {
                    if (!subjectSS) {
                        ToastUlility.show(AttendenceReportActivity.this, "Please select student");
                    } else {
                        Log.e("STUDENT ID", studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                        if (TextUtils.isEmpty(fromTV.getText().toString().trim()) && TextUtils.isEmpty(toTV.getText().toString().trim()) && spinner.getSelectedItem().toString().equalsIgnoreCase("Select Student")) {

                            ToastUlility.show(AttendenceReportActivity.this, "Please select from date ,to date and student");
                        } else if (TextUtils.isEmpty(fromTV.getText().toString().trim())) {
                            ToastUlility.show(AttendenceReportActivity.this, "Please select from date");
                        } else if (TextUtils.isEmpty(toTV.getText().toString().trim())) {
                            ToastUlility.show(AttendenceReportActivity.this, "Please select to date");
                        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select Student")) {
                            ToastUlility.show(AttendenceReportActivity.this, "Please select student");
                        } else if (spinner.getSelectedItemPosition() < 0) {
                            ToastUlility.show(AttendenceReportActivity.this, "Please select student");
                        } else {
//                    if (CheckConnectivity.checkInternetConnection(AttendenceReportActivity.this)) {
                            getAttendanceReport();
//                    }

                        }
                    }

                } catch (Exception e) {
                    ToastUlility.show(this, "Please select student");
                }


                break;
            case R.id.logoutIV:
                Logout.logout(this);
                break;
        }

    }


    public void getAttendanceReport() {

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.getAttendanceReport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {
//                        settingsDialog.dismiss();
                        noRecordFoundTV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loadingDialog.dismiss();
                        attendanceReportList.clear();
                        total = jobj.getString("total");
                        JSONArray jarr = jobj.getJSONArray("response");
                        for (int i = 0; i < jarr.length(); i++) {
                            AttendanceReport ar = new AttendanceReport();
                            ar.setFName(jarr.getJSONObject(i).getString("FName"));
                            ar.setLName(jarr.getJSONObject(i).getString("LName"));
                            ar.setAttendanceID(jarr.getJSONObject(i).getString("AttendanceID"));
                            ar.setTutorID(jarr.getJSONObject(i).getString("TutorID"));
                            ar.setStudentID(jarr.getJSONObject(i).getString("StudentID"));
                            ar.setStartDT(jarr.getJSONObject(i).getString("StartDT"));
                            ar.setEndDT(jarr.getJSONObject(i).getString("EndDT"));
                            ar.setTotalHours(jarr.getJSONObject(i).getString("TotalHours"));
                            ar.setPayRate(jarr.getJSONObject(i).getString("PayRate"));
                            ar.setTotalPay(jarr.getJSONObject(i).getString("TotalPay"));
                            ar.setComments(jarr.getJSONObject(i).getString("Comments"));
                            ar.setSubjectTaught(jarr.getJSONObject(i).getString("SubjectTaught"));
                            ar.setHourIn(jarr.getJSONObject(i).getString("HourIn"));
                            ar.setHourOut(jarr.getJSONObject(i).getString("HourOut"));
                            attendanceReportList.add(ar);
                        }

                        bottomLL.setVisibility(View.VISIBLE);
                        totalTV.setText(total);
                        recyclerView.setAdapter(new ReportListAdapter(AttendenceReportActivity.this, attendanceReportList));

                    } else {
                        String message = jobj.getString("message");
//                        ToastUlility.show(AttendenceReportActivity.this, message);
                        noRecordFoundTV.setText(message);
                        noRecordFoundTV.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        loadingDialog.dismiss();
                        bottomLL.setVisibility(View.GONE);
                        attendanceReportList.clear();
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
                ToastUlility.show(AttendenceReportActivity.this, "Network Error,please try again");
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("TutorID", tutorID);
                params.put("StudentID", studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                params.put("StartDT", fromTV.getText().toString());
                params.put("EndDT", toTV.getText().toString());

                Log.e("PARAMS", params.toString());
                return params;
            }
        };
        String request_tag = "attendance_report";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    public static void getStudentsList(final Activity activity, final LoadingDialog loadingDialog, final ArrayList<String> studentListArrayList, final ArrayList<String> studentIdsList, final Spinner spinner, final String tutorID) {
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

                        studentListArrayList.clear();
//                        studentListArrayList.add("Select Student");
                        studentListArrayList.add("ALL");
                        studentIdsList.clear();
//                        studentIdsList.add("-1");
                        studentIdsList.add("ALL");
                        JSONArray jarr = jobj.getJSONArray("response");
                        for (int i = 0; i < jarr.length(); i++) {
//                            StudentList studentList = new StudentList();
//                            studentList.setStudentID(jarr.getJSONObject(i).getString("StudentID"));
//                            studentList.setFName(jarr.getJSONObject(i).getString("FName"));
//                            studentList.setLName(jarr.getJSONObject(i).getString("LName"));
//                            studentList.setAddress(jarr.getJSONObject(i).getString("Address"));
                            studentIdsList.add(jarr.getJSONObject(i).getString("StudentID"));
                            studentListArrayList.add(jarr.getJSONObject(i).getString("FName") + jarr.getJSONObject(i).getString("LName"));
                        }


                        defaultHintSpinner = new HintSpinner<>(
                                spinner,
                                // Default layout - You don't need to pass in any layout id, just your hint text and
                                // your list data
                                new HintAdapter<String>(activity, "Select Student", studentListArrayList),
                                new HintSpinner.Callback<String>() {
                                    @Override
                                    public void onItemSelected(int position, String itemAtPosition) {
                                        // Here you handle the on item selected event (this skips the hint selected
                                        // event)
                                        subjectSS = true;
//                                        showSelectedItem(itemAtPosition);
                                    }
                                });
                        defaultHintSpinner.init();

                        // Creating adapter for spinner
//                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, studentListArrayList);
//
//                        // Drop down layout style - list view with radio button
//                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
//                        spinner.setAdapter(dataAdapter);
//                        studentsRV.setAdapter(new StudentListAdapter(studentListArrayList, StudentsListActivity.this));

                    } else {
                        loadingDialog.dismiss();
                        ToastUlility.show(activity, jobj.getString("message"));
                        Intent intent = new Intent(activity, DashBoardActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
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
                ToastUlility.show(activity, "Network Error,please try again");
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("TutorID", tutorID);

                Log.e("PARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "student_list";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

}
