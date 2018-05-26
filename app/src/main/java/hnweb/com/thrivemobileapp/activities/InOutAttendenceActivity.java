package hnweb.com.thrivemobileapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.application.AppAPI;
import hnweb.com.thrivemobileapp.application.MainApplication;
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.Logout;
import hnweb.com.thrivemobileapp.utility.ToastUlility;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

public class InOutAttendenceActivity extends AppCompatActivity implements View.OnClickListener {

    CustomCalendarView calendarView;
    SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    ArrayList<String> studentListArrayList = new ArrayList<String>();
    ImageView logoutIV;
    Spinner spinner;
    boolean subjectSS = false;
    RelativeLayout calenderRL;
    ArrayList<String> studentIdsList = new ArrayList<String>();
    TextView tutorHrsTV;
    String selectedDate, tutorID;
    public static HintSpinner<String> defaultHintSpinner;
    String StudentId;

    //ArrayList<TutorCalendarDetails> calendarDetailsList;
    ArrayList<HashMap<String, String>> calendarDetailsList;
    ArrayList<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out_attendence);

        loadingDialog = new LoadingDialog(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        tutorID = sharedPreferences.getString("TutorID", "");

        StudentId = getIntent().getExtras().getString("StudentId");
        Log.e("StudentId", StudentId);

        logoutIV = (ImageView) findViewById(R.id.logoutIV);
        spinner = (Spinner) findViewById(R.id.spinner);
        tutorHrsTV = (TextView) findViewById(R.id.tutorHrsTV);

        calenderRL = (RelativeLayout) findViewById(R.id.calenderRL);

        //calendarDetailsList = new ArrayList<TutorCalendarDetails>();
        calendarDetailsList = new ArrayList<>();
        list = new ArrayList<String>();

        TextView tutorTv = (TextView) findViewById(R.id.tutorTV);
        tutorTv.setText(sharedPreferences.getString("FName", "") + " " + sharedPreferences.getString("LName", ""));

        getStudentsList(this, loadingDialog, studentListArrayList, studentIdsList, spinner, tutorID);
//        getStudentsList();

        //setCalenderView(); // Comment on 25/10/2017

        if (StudentId.equals("")) {
            Log.d("oncratedatesend", "oncrate Studentid blank");

        } else {

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int monthnew = month + 1;
            int day = calendar.get(Calendar.DATE);

            getCalendarDateList(InOutAttendenceActivity.this, loadingDialog, tutorID, StudentId,
                    String.valueOf(year), String.valueOf(monthnew));

            //setCalenderView();
            //String datesend = String.valueOf(monthnew)+"/"+String.valueOf(day)+"/"+String.valueOf(year);
            //Log.d("oncratedatesend","oncrate student"+StudentId +" Date "+datesend);
            //calendarView.setBackgroundColorOfRedOrGreen(list,datesend);

        }


        //getHrsLeft();
        //getCalendarDateList(InOutAttendenceActivity.this, loadingDialog, tutorID, StudentId, String.valueOf(year), String.valueOf(monthnew));


    }

    public void setCalenderView() {

        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        //Initialize calendar with date
        final Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                Log.e("selectedDate", "0 " + String.valueOf(date));
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                NumberFormat f = new DecimalFormat("00");
                selectedDate = df.format(date);
                Log.e("selectedDate", "1 " + selectedDate);
                String monthStr = "";
                String[] dateSelect = selectedDate.split("/");

                Log.e("selectedDate", "2 " + dateSelect.toString());

                calendarView.setBackgroundColorOfRedOrGreen(list, df.format(date));

                int yearcurrent = Calendar.getInstance().get(Calendar.YEAR);
                int monthcurrent = Calendar.getInstance().get(Calendar.MONTH) + 1;

                if (monthcurrent < 10) {
                    monthStr = "0" + monthcurrent;
                } else {
                    monthStr = String.valueOf(monthcurrent);
                }

                int dayCurrent = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                Log.e("selectedDate", "3 " + String.valueOf(dayCurrent));

                int noOfDays = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

                Log.e("selectedDate", "4 " + "yearcurrent " + yearcurrent + " monthStr " + monthStr + " month");

                if (Integer.parseInt(dateSelect[2].toString()) == yearcurrent && dateSelect[0].toString().equals(monthStr)) {
                    Log.e("selectedDate", "5 " + "Upper0 IF " + dateSelect[2].toString() + "  " + dateSelect[0].toString());

                    if (dayCurrent < 16) {

                        if (0 < Integer.parseInt(dateSelect[1].toString()) && Integer.parseInt(dateSelect[1].toString()) < 16) {
                            Log.e("selectedDate", "6 " + "Upper1 IF " + dateSelect[1].toString() + "  " + dateSelect[1].toString());
                            afterDateSelection(date);
                        } else {
                            Log.e("selectedDate", "7 " + "Upper1 else " + dateSelect[1].toString() + "  " + dateSelect[1].toString());
                            ToastUlility.show(InOutAttendenceActivity.this, "You have selected a date outside of the pay period.");
                        }

                    } else if (dayCurrent > 15) {

                        if (15 < Integer.parseInt(dateSelect[1].toString()) && Integer.parseInt(dateSelect[1].toString()) < (noOfDays + 1)) {
                            Log.e("selectedDate", "8 " + "Upper2 IF " + dateSelect[1].toString() + "  " + dateSelect[1].toString());
                            afterDateSelection(date);
                        } else {
                            Log.e("selectedDate", "9 " + "Upper2 IF " + dateSelect[1].toString() + "  " + dateSelect[1].toString());
                            ToastUlility.show(InOutAttendenceActivity.this, "You have selected a date outside of the pay period.");
                        }

                    } else {
                        Log.e("selectedDate", "10 " + "Upper2 IF " + dateSelect[1].toString() + "  " + dateSelect[1].toString());
                        ToastUlility.show(InOutAttendenceActivity.this, "You have selected a date outside of the pay period.");
                    }

                } else {
                    Log.e("selectedDate", "11 " + "Upper0 Else " + dateSelect[2].toString() + "  " + dateSelect[0].toString());
                    ToastUlility.show(InOutAttendenceActivity.this, "You have selected a date outside of the pay period.");
                }


//                Toast.makeText(InOutAttendenceActivity.this, df.format(date), Toast.LENGTH_SHORT).show();


//                ConstantVal.date_selected = df.format(date);
//                if (distinctAvailableDatesList.contains(ConstantVal.date_selected)) {
////                    callweservicesToBook(df.format(date));
//                    if (CheckConnectivity.checkInternetConnection(TherapistMyProfileActivity.this)) {
////                        callweservicesToBook(df.format(date), date);
//                    } else {
////                        CheckConnectivity.noNetMeg(TherapistMyProfileActivity.this);
//                    }
//
//
//                } else {
////                    Toast.makeText(TherapistMyProfileActivity.this, "To update schedule go to My Profile and update status.", Toast.LENGTH_SHORT).show();
//                }


            }

            @Override
            public void onMonthChanged(Date date) {

                //SimpleDateFormat df0 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df0 = new SimpleDateFormat("MM/dd/yyyy");
                Log.i("onMonthSelected", df0.format(date));
//                int monthChanged = Integer.parseInt(dateFormat.format(date1));
//                int monthCurrent = Integer.parseInt(dateFormat.format(date));
//                if (monthChanged == monthCurrent)
                //Log.e("Date", df.format(date));

                calendarView.setBackgroundColorOfRedOrGreen(list, df0.format(date));


                for (int i = 0; i < calendarDetailsList.size(); i++) {
                    HashMap<String, String> map = calendarDetailsList.get(i);

                    try {

                        String StartDT = map.get("StartDT");

                        if (df0.format(date).equalsIgnoreCase(StartDT)) {
                            Log.d("Equal ", "3 " + df0.format(date));
                            //eventmsg.setText(map.get("event_name"));
                            //eventmsg.setBackgroundResource(R.mipmap.orangebg2x);
                            //Log.d("5 " , "5");
                        } else {
                            //eventmsg.setText("");
                            //montheventmsg.setText("");
                            //Log.d("6 " , "6");
                            Log.d("NotEqual ", "3 " + df0.format(date));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Exception", e.getMessage());
                    }


                }


//                calendarView.setNewDateColor(distinctAvailableDatesList, df.format(date));
////                calendarView.refreshCalendar(currentCalendar);
//                DateFormat dateFormat = new SimpleDateFormat("MM");
//                Date date1 = new Date();
//                Log.e("Month", dateFormat.format(date1));
////
//                int monthChanged = Integer.parseInt(dateFormat.format(date1));
//                int monthCurrent = Integer.parseInt(dateFormat.format(date));
//                if (monthChanged == monthCurrent)
//                    calendarView.setSelectedDayGreen(distinctAvailableDatesList);
//                else
//                    calendarView.setSelectedDayRed(monthCurrent);


//                Toast.makeText(TherapistProfileActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void afterDateSelection(Date date) {



        if (date.getTime() > System.currentTimeMillis()) {
            ToastUlility.show(InOutAttendenceActivity.this, "You have selected a future date.");
        } else {
            //if (Double.parseDouble(tutorHrsTV.getText().toString().trim()) == 0) {
            //    ToastUlility.show(InOutAttendenceActivity.this, "Student does not have enough hours for this entry");
            //} else {

                getPerDayAttendance(tutorHrsTV.getText().toString().trim());
//                        ToastUlility.show(InOutAttendenceActivity.this, "Valid Date");
            //}
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBTN:
                try {
                    if (subjectSS) {
                        if (spinner.getSelectedItem().toString().equalsIgnoreCase("")) {
                            ToastUlility.show(this, "Please select student");
                        } else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select Student")) {
                            ToastUlility.show(this, "Please select student");
                        } else {
                            calenderRL.setVisibility(View.VISIBLE);

                            Log.e("SubmitTutorSel", studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                            String StudID = studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim();

                            getHrsLeft(StudID);

                            // get current year、month and day
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int monthnew = month + 1;
                            int day = calendar.get(Calendar.DATE);
                            Log.d("calendarDetails", "year " + year + " month " + month + " day " + day);
                            Log.d("calendarDetailsnew", "year " + year + " monthnew " + monthnew + " day " + day + " StudentId " + StudentId + " Here");

                            getCalendarDateList(InOutAttendenceActivity.this, loadingDialog, tutorID, StudID,
                                    String.valueOf(year), String.valueOf(monthnew));


                        }
                    } else {
                        ToastUlility.show(this, "Please select student");
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

    public void getHrsLeft(final String Stud_ID) {
        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.getTutoringHrsLeftOfStudent, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("INOUTRESPONSE", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {
//                        settingsDialog.dismiss();
                        loadingDialog.dismiss();
                        String tutoringHr = jobj.getString("TutoringHours");
                        tutorHrsTV.setText(tutoringHr);

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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
                params.put("StudentID", Stud_ID);

               /* if (StudentId.equalsIgnoreCase("")) {
                    params.put("StudentID", studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                } else {
                    params.put("StudentID", StudentId);
                }*/
//                params.put("SelectYear", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
//                params.put("SelectMonth", String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));

                Log.e("INOUTPARAMS", params.toString());
                return params;

            }
        };
        stringRequest.setShouldCache(false);
        String request_tag = "getHrs_left";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    public void getPerDayAttendance(final String hoursLeft) {
        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.getPerDayAttendance, new Response.Listener<String>() {
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
//                        for (int i=0;i<jarr.length();i++){
                        String AttendanceID = jarr.getJSONObject(0).getString("AttendanceID");
//                        }

                        attendanceoptionDialog(AttendanceID, response, hoursLeft);

//                        String tutoringHr = jobj.getString("TutoringHours");
//                        tutorHrsTV.setText("Tutoring Hrs Left : " + tutoringHr);

                    } else {
                        loadingDialog.dismiss();
                        Intent intent = new Intent(InOutAttendenceActivity.this, AddEditHoursActivity.class);
                        intent.putExtra("ComeFrom", "ADD");
                        intent.putExtra("date", selectedDate);
                        intent.putExtra("HrsLeft", hoursLeft);
                        intent.putExtra("StudentId", studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                        startActivity(intent);
//                        finish();
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
                params.put("StudentID", studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                params.put("StartDT", selectedDate);
//                params.put("SelectMonth", "5");


                Log.e("PARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "getHrs_left";

//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    public void deleteAttendance(final String attendanceID, final Dialog settingsDialog) {
        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.deleteAttendance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {
                        settingsDialog.dismiss();
                        loadingDialog.dismiss();
                        String meg = jobj.getString("message");
                        ToastUlility.show(InOutAttendenceActivity.this, meg);

                        Log.e("SubmitTutorSel", studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                        String StudID = studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim();

                        getHrsLeft(StudID);

                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int monthnew = month + 1;
                        int day = calendar.get(Calendar.DATE);

                        getCalendarDateList(InOutAttendenceActivity.this, loadingDialog, tutorID, StudID,
                                String.valueOf(year), String.valueOf(monthnew));


                    } else {
                        loadingDialog.dismiss();
                        String meg = jobj.getString("message");
                        ToastUlility.show(InOutAttendenceActivity.this, meg);
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
                params.put("AttendanceID", attendanceID);

                Log.e("PARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "delete_attendance";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    public void attendanceoptionDialog(final String attendanceID, final String response, final String hoursLeft) {
        final Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(this.getLayoutInflater().inflate(R.layout.attendance_option
                , null));
        settingsDialog.setCancelable(true);
//        final EditText fppassTV = (EditText) settingsDialog.findViewById(R.id.modifyTV);
        TextView modifyBTN = (TextView) settingsDialog.findViewById(R.id.modifyTV);
        TextView deleteBTN = (TextView) settingsDialog.findViewById(R.id.deleteTV);
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAttendance(attendanceID, settingsDialog);
            }
        });

        modifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.dismiss();
                Intent intent = new Intent(InOutAttendenceActivity.this, AddEditHoursActivity.class);
                intent.putExtra("ComeFrom", "Modify");
                intent.putExtra("date", selectedDate);
                intent.putExtra("StudentId", response);
                intent.putExtra("HrsLeft", hoursLeft);
                startActivity(intent);
            }
        });

        settingsDialog.show();
    }

    public void getStudentsList(final Activity activity, final LoadingDialog loadingDialog, final ArrayList<String> studentListArrayList, final ArrayList<String> studentIdsList, final Spinner spinner, final String tutorID) {
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
                                        subjectSS = true;
//                                        showSelectedItem(itemAtPosition);
                                    }
                                });
                        defaultHintSpinner.init();
                        if (StudentId.equalsIgnoreCase("")) {
                            // do nothing
                        } else {
                            spinner.setSelection(studentIdsList.indexOf(StudentId));
                            calenderRL.setVisibility(View.VISIBLE);

                            Log.e("SubmitTutorSel", studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                            String StudID = studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim();

                            getHrsLeft(StudID);
                        }

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
        }, new Response.ErrorListener() {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }


    public void getCalendarDateList(final Activity activity, final LoadingDialog loadingDialog,
                                    final String tutorID, final String Stud_ID, final String CurrentYear,
                                    final String CurrentMonth) {
        //loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.getCalendarDates, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("CalendarRESPONSE", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {

                        //loadingDialog.dismiss();
                        list.clear();
                        calendarDetailsList.clear();


                        JSONArray jarr = jobj.getJSONArray("response");
                        for (int i = 0; i < jarr.length(); i++) {


                            // tmp hash map for single contact
                            HashMap<String, String> tempcalendarevent = new HashMap<>();

                            // adding each child node to HashMap key => value
                            tempcalendarevent.put("AttendanceID", jarr.getJSONObject(i).getString("AttendanceID"));
                            tempcalendarevent.put("Comments", jarr.getJSONObject(i).getString("Comments"));
                            tempcalendarevent.put("StartDT", jarr.getJSONObject(i).getString("StartDT"));
                            tempcalendarevent.put("EndDT", jarr.getJSONObject(i).getString("EndDT"));
                            tempcalendarevent.put("HourIn", jarr.getJSONObject(i).getString("HourIn"));
                            tempcalendarevent.put("HourOut", jarr.getJSONObject(i).getString("HourOut"));
                            // adding contact to contact list
                            calendarDetailsList.add(tempcalendarevent);
                            //calendarView.setSelectedDayGreen(calendarevents);
                            list.add(jarr.getJSONObject(i).getString("StartDT"));


                            /*TutorCalendarDetails tutorCalendarDetails = new TutorCalendarDetails();
                            tutorCalendarDetails.setAttendanceID(jarr.getJSONObject(i).getString("AttendanceID"));
                            tutorCalendarDetails.setComments(jarr.getJSONObject(i).getString("Comments"));
                            tutorCalendarDetails.setStartDT(jarr.getJSONObject(i).getString("StartDT"));
                            tutorCalendarDetails.setEndDT(jarr.getJSONObject(i).getString("EndDT"));
                            tutorCalendarDetails.setHourIn(jarr.getJSONObject(i).getString("HourIn"));
                            tutorCalendarDetails.setHourOut(jarr.getJSONObject(i).getString("HourOut"));
                            calendarDetailsList.add(tutorCalendarDetails);*/

                            setCalenderView();


                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int monthnew = month + 1;
                            int day = calendar.get(Calendar.DATE);

                            String datesend = String.valueOf(monthnew) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
                            Log.d("datesend", datesend);

                            //SimpleDateFormat df0 = new SimpleDateFormat("MM/dd/yyyy");

                            calendarView.setBackgroundColorOfRedOrGreen(list, datesend);

                        }


                    } else {
                        //loadingDialog.dismiss();
                        ToastUlility.show(activity, jobj.getString("message"));

                        setCalenderView();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }*/
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

                //params.put("StudentID", StudentID);
                /*if (StudentId.equalsIgnoreCase("")) {
                    params.put("StudentID", studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                    Log.e("CalendarPARAMS", "IF StudID "+studentIdsList.get(spinner.getSelectedItemPosition()).toString().trim());
                } else {
                    params.put("StudentID", StudentId);
                    Log.e("CalendarPARAMS", "ELSE StudID "+ StudentId);
                }*/

                params.put("TutorID", tutorID);
                params.put("StudentID", Stud_ID);
                params.put("SelectYear", CurrentYear);
                params.put("SelectMonth", CurrentMonth);

                Log.e("CalendarPARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "getHrs_left";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }


}
