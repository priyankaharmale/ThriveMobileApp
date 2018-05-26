package hnweb.com.thrivemobileapp.application;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Spinner;

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

import hnweb.com.thrivemobileapp.activities.DashBoardActivity;
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.ToastUlility;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

/**
 * Created by neha on 5/25/2017.
 */

public class AppAPI {
    public static HintSpinner<String> defaultHintSpinner;

    //public static String BASE_URL = "http://104.37.185.20/~tech599/tech599.com/johnanil/thriveportal/api/";
    //http://thriveacademics.com/tutorportal/api/
    //public static String BASE_URL = "http://thriveacademics.com/tutorportal/api/";

   public static String BASE_URL = "http://thriveportal.net/api/";

  //  public static String BASE_URL = "http://tech599.com/~tech599/tech599.com/johnman/Thrive_Academics/api/";

    public static String loginUrl = BASE_URL + "tutor_login.php";
    public static String forgotPasswordUrl = BASE_URL + "tutor_forgot_password.php";
    public static String studentListForTutor = BASE_URL + "students_list_for_tutor.php";
    public static String studentDetails = BASE_URL + "student_details.php";
    public static String getTutoringHrsLeftOfStudent = BASE_URL + "tutoring_hrs_left.php";
    public static String getSubjectList = BASE_URL + "get_list_of_subjects.php";

    public static String addInOutAttendance = BASE_URL + "add_in_out_attendance.php";
    public static String getPerDayAttendance = BASE_URL + "check_perdate_attendance.php";
    public static String deleteAttendance = BASE_URL + "delete_attendance.php";
    public static String modifyAttendance = BASE_URL + "modify_in_out_attendance.php";
    public static String getAttendanceReport = BASE_URL + "get_attendace_report_of_tutor.php";
    public static String getTimeCard = BASE_URL + "get_tutor_time_card.php";
    public static String submitTimeCard = BASE_URL + "submit_time_card_by_tutor.php";

    public static String downloadURLS = BASE_URL + "get_list_of_forms.php";
    public static String sendProgressReport = BASE_URL + "send_tutor_pics_to_admin.php";
    public static String updateToken = BASE_URL + "update_token.php";
    public static String getCalendarDates = BASE_URL + "get_tutoring_attendance_of_student_by_month_year.php";


//    public static String loginUrl = "http://designer321.com/johnilbwd/thriveportal/api/tutor_login.php";
//    public static String forgotPasswordUrl = "http://designer321.com/johnilbwd/thriveportal/api/tutor_forgot_password.php";
//    public static String studentListForTutor = "http://designer321.com/johnilbwd/thriveportal/api/students_list_for_tutor.php";
//    public static String studentDetails = "http://designer321.com/johnilbwd/thriveportal/api/student_details.php";
//    public static String getTutoringHrsLeftOfStudent = "http://designer321.com/johnilbwd/thriveportal/api/tutoring_hrs_left.php";
//    public static String getSubjectList = "http://designer321.com/johnilbwd/thriveportal/api/get_list_of_subjects.php";
//
//    public static String addInOutAttendance = "http://designer321.com/johnilbwd/thriveportal/api/add_in_out_attendance.php";
//    public static String getPerDayAttendance = "http://designer321.com/johnilbwd/thriveportal/api/check_perdate_attendance.php";
//    public static String deleteAttendance = "http://designer321.com/johnilbwd/thriveportal/api/delete_attendance.php";
//    public static String modifyAttendance = "http://designer321.com/johnilbwd/thriveportal/api/modify_in_out_attendance.php";
//    public static String getAttendanceReport = "http://designer321.com/johnilbwd/thriveportal/api/get_attendace_report_of_tutor.php";
//    public static String getTimeCard = "http://designer321.com/johnilbwd/thriveportal/api/get_tutor_time_card.php";
//    public static String submitTimeCard = "http://designer321.com/johnilbwd/thriveportal/api/submit_time_card_by_tutor.php";
//
//    public static String downloadURLS = "http://designer321.com/johnilbwd/thriveportal/api/get_list_of_forms.php";
//    public static String sendProgressReport = "http://designer321.com/johnilbwd/thriveportal/api/send_tutor_pics_to_admin.php";

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
                        studentIdsList.clear();
//                        studentIdsList.add("-1");
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
