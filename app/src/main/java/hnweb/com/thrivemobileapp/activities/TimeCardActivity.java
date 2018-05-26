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
import android.widget.CheckBox;
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

public class TimeCardActivity extends AppCompatActivity implements View.OnClickListener {

    TextView fromTV, toTV;
    Spinner spinner;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    ArrayList<String> studentListArrayList = new ArrayList<String>();
    ArrayList<String> studentIdsList = new ArrayList<String>();
    String tutorID, total_hours_for_selected_period, payroll_for_selected_period;
    ArrayList<AttendanceReport> attendanceReportList = new ArrayList<AttendanceReport>();
    LinearLayout bottomLL;
    TextView totalHrsTV, payrollTV;
    CheckBox verify1CB, verify2CB;
    static boolean subjectSS = false;
    public static HintSpinner<String> defaultHintSpinner;
    ImageView logoutIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_card);

        loadingDialog = new LoadingDialog(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        tutorID = sharedPreferences.getString("TutorID", "");

        logoutIV = (ImageView) findViewById(R.id.logoutIV);
        fromTV = (TextView) findViewById(R.id.fromTV);
        toTV = (TextView) findViewById(R.id.toTV);
        spinner = (Spinner) findViewById(R.id.spinner);
        bottomLL = (LinearLayout) findViewById(R.id.bottomLL);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        payrollTV = (TextView) findViewById(R.id.payrollTV);
        totalHrsTV = (TextView) findViewById(R.id.totalHrsTV);
        verify1CB = (CheckBox) findViewById(R.id.verify1CB);
        verify2CB = (CheckBox) findViewById(R.id.verify1CB);

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
            case R.id.processBTN:
                try {
                    if (!subjectSS) {
                        ToastUlility.show(TimeCardActivity.this, "Please select student");
                    } else {
                        if (TextUtils.isEmpty(fromTV.getText().toString().trim()) && TextUtils.isEmpty(toTV.getText().toString().trim()) && spinner.getSelectedItem().toString().equalsIgnoreCase("Select Student")) {

                            ToastUlility.show(TimeCardActivity.this, "Please select from date ,to date and student");
                        } else if (TextUtils.isEmpty(fromTV.getText().toString().trim())) {
                            ToastUlility.show(TimeCardActivity.this, "Please select from date");
                        } else if (TextUtils.isEmpty(toTV.getText().toString().trim())) {
                            ToastUlility.show(TimeCardActivity.this, "Please select to date");
                        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select Student")) {
                            ToastUlility.show(TimeCardActivity.this, "Please select student");
                        } else if (spinner.getSelectedItemPosition() < 0) {
                            ToastUlility.show(TimeCardActivity.this, "Please select student");
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
            case R.id.submitBTN:
                try {
                    if (!subjectSS) {
                        ToastUlility.show(TimeCardActivity.this, "Please select student");
                    } else {
                        if (TextUtils.isEmpty(fromTV.getText().toString().trim()) && TextUtils.isEmpty(toTV.getText().toString().trim())
                                && spinner.getSelectedItem().toString().equalsIgnoreCase("Select Student") && verify1CB.isChecked() == false && verify2CB.isChecked() == false) {

                            ToastUlility.show(TimeCardActivity.this, "Please select from date ,to date and student");
                        } else if (TextUtils.isEmpty(fromTV.getText().toString().trim())) {
                            ToastUlility.show(TimeCardActivity.this, "Please select from date");
                        } else if (TextUtils.isEmpty(toTV.getText().toString().trim())) {
                            ToastUlility.show(TimeCardActivity.this, "Please select to date");
                        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select Student")) {
                            ToastUlility.show(TimeCardActivity.this, "Please select student");
                        } else if (spinner.getSelectedItemPosition() < 0) {
                            ToastUlility.show(TimeCardActivity.this, "Please select student");
                        } else if (verify1CB.isChecked() == false) {
                            ToastUlility.show(TimeCardActivity.this, "Please verify Have you verified student hours are correct on your time card and progress reports?");
                        } else if (verify2CB.isChecked() == false) {
                            ToastUlility.show(TimeCardActivity.this, "Please verify I verify that all my hours are true and correct.");
                        } else {
//                    if (CheckConnectivity.checkInternetConnection(AttendenceReportActivity.this)) {
                            submitTimeCard();
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.getTimeCard, new Response.Listener<String>() {
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
                        attendanceReportList.clear();
                        recyclerView.setAdapter(null);

                        total_hours_for_selected_period = jobj.getString("total_hours_for_selected_period");
                        payroll_for_selected_period = jobj.getString("payroll_for_selected_period");

//                        attendanceReportList.clear();
//                        total = jobj.getString("total");
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
                        totalHrsTV.setText(total_hours_for_selected_period);
                        payrollTV.setText(payroll_for_selected_period);
                        //                        totalTV.setText(total);
                        recyclerView.setAdapter(new ReportListAdapter(TimeCardActivity.this, attendanceReportList));

                    } else {
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
                ToastUlility.show(TimeCardActivity.this, "Network Error,please try again");
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


    public void submitTimeCard() {

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.submitTimeCard, new Response.Listener<String>() {
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
                        ToastUlility.show(TimeCardActivity.this, jobj.getString("message"));
                        Intent intent = new Intent(TimeCardActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();


                    } else {
                        loadingDialog.dismiss();
                        ToastUlility.show(TimeCardActivity.this, jobj.getString("message"));

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
                ToastUlility.show(TimeCardActivity.this, "Network Error,please try again");
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
                        studentListArrayList.add("ALL");
                        studentIdsList.clear();
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
////                        SpinnerAdapter dataAdapter = new SpinnerAdapter(activity, android.R.layout.simple_list_item_1);
//                        // Drop down layout style - list view with radio button
//                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                        dataAdapter.addAll(studentListArrayList);
//                        // attaching data adapter to spinner
//                        spinner.setAdapter(dataAdapter);
//                        studentsRV.setAdapter(new StudentListAdapter(studentListArrayList, StudentsListActivity.this));

                    } else {
                        loadingDialog.dismiss();
                        ToastUlility.show(activity, jobj.getString("message"));
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
                Intent intent = new Intent(activity, DashBoardActivity.class);
                activity.startActivity(intent);
                activity.finish();
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
