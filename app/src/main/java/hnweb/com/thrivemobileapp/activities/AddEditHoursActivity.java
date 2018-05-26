package hnweb.com.thrivemobileapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.application.AppAPI;
import hnweb.com.thrivemobileapp.application.MainApplication;
import hnweb.com.thrivemobileapp.utility.LoadingDialog;
import hnweb.com.thrivemobileapp.utility.ToastUlility;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

public class AddEditHoursActivity extends AppCompatActivity implements View.OnClickListener {

    public HintSpinner<String> defaultHintSpinner;

    Spinner hhSS, mmSS, ampmSS, hhES, mmES, ampmES, subjectS;
    boolean hhS = false, mmS = false, ampmS = false, hhE = false, mmE = false, ampmE = false, subjectB = false;
    String[] hhInSpinner = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String[] mmSpinner = {"00", "15", "30", "45"};
    String[] ampmSpinner = {"AM", "PM"};
    String[] hhOutSpinner = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    ArrayList<String> hhInSpinnerList = new ArrayList<String>(Arrays.asList(hhInSpinner));
    ArrayList<String> hhOutSpinnerList = new ArrayList<String>(Arrays.asList(hhOutSpinner));
    ArrayList<String> mmSpinnerList = new ArrayList<String>(Arrays.asList(mmSpinner));
    ArrayList<String> ampmSpinnerList = new ArrayList<String>(Arrays.asList(ampmSpinner));
    String dateSelected, StudentId;
    EditText commentsTV;
    ArrayList<String> subjectIdList = new ArrayList<String>();
    ArrayList<String> subjectNameList = new ArrayList<String>();
    ArrayList<String> subjectRateList = new ArrayList<String>();
    String comeFrom, hrsLeft;
    String attendanceId;

    String timedifferenecmodify = "", Totalcomeherehrs = "", minustotal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_hours);

        comeFrom = getIntent().getExtras().getString("ComeFrom");
        dateSelected = getIntent().getExtras().getString("date");
        StudentId = getIntent().getExtras().getString("StudentId");
        hrsLeft = getIntent().getExtras().getString("HrsLeft");

        Log.e("IntentData", "comeFrom " + comeFrom + " dateSelected " + dateSelected + " StudentId " + StudentId + " hrsLeft " + hrsLeft);
        Log.e("DATE", dateSelected);
        Log.e("StudentId", StudentId);
        Log.e("hrsLeft", hrsLeft);

        hhSS = (Spinner) findViewById(R.id.hhSS);
        mmSS = (Spinner) findViewById(R.id.mmSS);
        ampmSS = (Spinner) findViewById(R.id.ampmSS);
        hhES = (Spinner) findViewById(R.id.hhES);
        mmES = (Spinner) findViewById(R.id.mmES);
        ampmES = (Spinner) findViewById(R.id.ampmES);
        subjectS = (Spinner) findViewById(R.id.subjectS);
        commentsTV = (EditText) findViewById(R.id.commentsTV);

        loadingDialog = new LoadingDialog(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);

        setSpinners(hhSS, hhInSpinnerList, "InTime(Hr)", "hhS");
        setSpinners(mmSS, mmSpinnerList, "Min:", "mmS");
        setSpinners(ampmSS, ampmSpinnerList, "AM/PM", "ampmS");

        setSpinners(hhES, hhOutSpinnerList, "OutTime(Hr)", "hhE");
        setSpinners(mmES, mmSpinnerList, "Min:", "mmE");
        setSpinners(ampmES, ampmSpinnerList, "AM/PM", "ampmE");
//        setSpinners(hhSS,hhSpinner);

        getSubjectList();

    }

    public void ifModify() {

        if (comeFrom.equals("Modify")) {
            try {
                JSONObject jobj = new JSONObject(StudentId);
                JSONArray jarr = jobj.getJSONArray("response");
                String studentID = jarr.getJSONObject(0).getString("StudentID");
                String HourIn = jarr.getJSONObject(0).getString("HourIn");
                String HourOut = jarr.getJSONObject(0).getString("HourOut");
                String SubjectTaught = jarr.getJSONObject(0).getString("SubjectTaught");
                String Comments = jarr.getJSONObject(0).getString("Comments");
                attendanceId = jarr.getJSONObject(0).getString("AttendanceID");
                Log.d("HoursIn", HourIn);
                Log.d("HoursOut", HourOut);

                String[] startTime = (HourIn.replace(" ", ":")).split(":");
                String[] endTime = (HourOut.replace(" ", ":")).split(":");


                this.StudentId = studentID;

                hhSS.setSelection(hhInSpinnerList.indexOf(startTime[0]));
                hhES.setSelection(hhOutSpinnerList.indexOf(endTime[0]));
                mmSS.setSelection(mmSpinnerList.indexOf(startTime[1]));
                mmES.setSelection(mmSpinnerList.indexOf(endTime[1]));
                ampmSS.setSelection(ampmSpinnerList.indexOf(startTime[2]));
                ampmES.setSelection(ampmSpinnerList.indexOf(endTime[2]));

                subjectS.setSelection(subjectNameList.indexOf(SubjectTaught));
                subjectB = true;
                commentsTV.setText(Comments);

//==================================================================================================
                final String inTime, outTime;

                if (ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim().equals("PM")) {
                    if (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) == 12) {
                        inTime = (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim())) + ":" +
                                mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
                    } else {
                        inTime = (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) + 12) + ":" +
                                mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
                    }

                } else {
                    if (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) == 12) {
                        inTime = Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) + 12 + ":" +
                                mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
                    } else {
                        inTime = hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim() + ":" +
                                mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
                    }

                }

                if (ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim().equals("PM")) {
                    if (Integer.parseInt(hhInSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) == 12) {
                        outTime = (Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim())) + ":" +
                                mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
                    } else {
                        outTime = (Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) + 12) + ":" +
                                mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
                    }

                } else {
                    if (Integer.parseInt(hhInSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) == 12) {
                        outTime = (Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) + 12) + ":" +
                                mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
                    } else {
                        outTime = hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim() + ":" +
                                mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
                    }

                }

                Log.e("diffTimemodify", "INT " + inTime);
                Log.e("diffTimemodify", "OUTT " + outTime);

                SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                Date d1 = null;
                Date d2 = null;


                try {

                    // Code Written by GaneshK & this is in execution

                    d1 = format.parse(inTime);
                    Log.e("diffTimemodify", "Date d1 " + String.valueOf(d1));
                    d2 = format.parse(outTime);
                    Log.e("diffTimemodify", "Date d2 " + String.valueOf(d2));

                    //in milliseconds
                    long diffnew = d2.getTime() - d1.getTime();
                    long diffHours = diffnew / (60 * 60 * 1000) % 24;
                    Log.e("diffTimemodify", "long diff " + String.valueOf(diffnew));

                    timedifferenecmodify = TimeUnit.MILLISECONDS.toHours(diffnew) + ":" + (TimeUnit.MILLISECONDS.toMinutes(diffnew) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffnew)));

                    Log.e("diffTimemodify", timedifferenecmodify + " hours, ");

                    String[] separated = timedifferenecmodify.split(":");
                    int hrs = Integer.parseInt(separated[0]); //
                    int mins = Integer.parseInt(separated[1]); //

                    Log.d("diffTimemodify", hrs + " hours, " + mins + " minutes");

                    String[] parts = hrsLeft.split("\\."); // escape .
                    int hrsLeftcomehrs = Integer.parseInt(parts[0]);
                    int hrsLeftcomemins = Integer.parseInt(parts[1]);

                    Log.d("diffTimehrsleft", hrsLeftcomehrs + " hours, " + hrsLeftcomemins + " minutes");

                    int cometotalhrs = hrs + hrsLeftcomehrs;
                    int cometotalmins = mins + hrsLeftcomemins;

                    Log.d("diffTimeTotal", cometotalhrs + " hours, " + cometotalmins + " minutes");
                    Totalcomeherehrs = cometotalhrs + ":" + cometotalmins;

                    Log.e("diffTimeTotalcome", Totalcomeherehrs + " hours, ");


                } catch (Exception e) {

                }

//==================================================================================================


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//==================================================================================================
    }

    public void setSpinners(Spinner hhSS, ArrayList<String> hhSpinner, String hint, final String selected) {


        defaultHintSpinner = new HintSpinner<>(
                hhSS,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<String>(AddEditHoursActivity.this, hint, hhSpinner),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected
                        // event)
                        if (selected.equalsIgnoreCase("hhS")) {
                            hhS = true;

                        } else if (selected.equalsIgnoreCase("mmS")) {
                            mmS = true;
                        } else if (selected.equalsIgnoreCase("ampmS")) {
                            ampmS = true;
                        } else if (selected.equalsIgnoreCase("hhE")) {
                            hhE = true;
                        } else if (selected.equalsIgnoreCase("mmE")) {
                            mmE = true;
                        } else if (selected.equalsIgnoreCase("ampmE")) {
                            ampmE = true;
                        }

                        Log.e("POSTITIONNNNN", String.valueOf(position));
//                                        showSelectedItem(itemAtPosition);
                    }

                });
        defaultHintSpinner.init();


//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddEditHoursActivity.this, android.R.layout.simple_spinner_item, hhSpinner);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        hhSS.setAdapter(dataAdapter);
    }

    public void getSubjectList() {
        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.getSubjectList, new Response.Listener<String>() {
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

                        /*subjectNameList.clear();
                        subjectNameList.add("Subject");
                        subjectIdList.clear();
                        subjectIdList.add("-1");
                        subjectRateList.clear();
                        subjectRateList.add("-1");*/
                        JSONArray jarr = jobj.getJSONArray("response");
                        for (int i = 0; i < jarr.length(); i++) {

                            subjectIdList.add(jarr.getJSONObject(i).getString("SubjectID"));
                            subjectNameList.add(jarr.getJSONObject(i).getString("SubjectName"));
                            subjectRateList.add(jarr.getJSONObject(i).getString("SubjectRate"));
                        }
//
                        defaultHintSpinner = new HintSpinner<>(
                                subjectS,
                                // Default layout - You don't need to pass in any layout id, just your hint text and
                                // your list data
                                new HintAdapter<String>(AddEditHoursActivity.this, "Select Subject", subjectNameList),
                                new HintSpinner.Callback<String>() {
                                    @Override
                                    public void onItemSelected(int position, String itemAtPosition) {
                                        // Here you handle the on item selected event (this skips the hint selected
                                        // event)
                                        subjectB = true;
//                                        subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim();
//                                        Log.e("RATE",subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim());
//                                        showSelectedItem(itemAtPosition);
                                    }
                                });
                        defaultHintSpinner.init();
//                        // Creating adapter for spinner
//                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddEditHoursActivity.this, android.R.layout.simple_spinner_item, subjectNameList);
//
//                        // Drop down layout style - list view with radio button
//                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                        // attaching data adapter to spinner
//                        subjectS.setAdapter(dataAdapter);
                        if (comeFrom.equals("Modify")) {
                            ifModify();
                        }

//                        studentsRV.setAdapter(new StudentListAdapter(studentListArrayList, StudentsListActivity.this));

                    } else {

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
        String request_tag = "subject_list";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    public void addInOutAttendance() {


        final String hourIn = hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim() + ":" +
                mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim();

        Log.e("hourIn0", hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourIn0", hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourIn0", hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());

        //final String hourOut = hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim() + ":" +
        //        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim();

        final String hourOut = hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim() + ":" +
                mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim();

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.addInOutAttendance, new Response.Listener<String>() {
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
                        String message = jobj.getString("message");
                        ToastUlility.show(AddEditHoursActivity.this, message);
                        onBackPressed();
//                        finish();
//                        Intent intent = new Intent(AddEditHoursActivity.this, InOutAttendenceActivity.class);
//                        startActivity(intent);
//                        finish();


                    } else {
                        loadingDialog.dismiss();
                        String message = jobj.getString("message");
                        ToastUlility.show(AddEditHoursActivity.this, message);
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
                params.put("StudentID", StudentId);
                params.put("StartDT", dateSelected);
                params.put("EndDT", dateSelected);

                //String diff = diffTime().replace(":", ".");
                String diff = diffTime();
                Log.d("TotalHoursdiff", diff);

                //params.put("TotalHours", diffTime());
                params.put("TotalHours", diff);
                params.put("PayRate", subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim());
                params.put("TotalPay", totalPay(subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim()));
                params.put("Comments", commentsTV.getText().toString().trim());
                params.put("SubjectTaught", subjectNameList.get(subjectS.getSelectedItemPosition()).toString().trim());
                params.put("HourIn", hourIn);
                params.put("HourOut", hourOut);

                Log.e("PARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "add_in_out_attendance";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    public String diffTime() {
        final String inTime, outTime;
        String time = "";
        float totaldiffbetinout = 0;
        float hours = 0;

//in time
        String in_hours = hhSS.getSelectedItem().toString();
        String in_mins = mmSS.getSelectedItem().toString();
        String in_ampm = ampmSS.getSelectedItem().toString();
        inTime = in_hours + ":" + in_mins + " " + in_ampm;
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        //out time

        String out_hours = hhES.getSelectedItem().toString();
        String out_mins = mmES.getSelectedItem().toString();
        String out_ampm = ampmES.getSelectedItem().toString();
        outTime = out_hours + ":" + out_mins + " " + out_ampm;


        Date d1, d2;
        try {
            d1 = dateFormat.parse(inTime);
            d2 = dateFormat.parse(outTime);
            System.out.println("d1 " + d1 + " d2" + d2);

            long diff = d2.getTime() - d1.getTime();
            System.out.println("difference in hours55" + diff);
            long seconds = diff / 1000;
            System.out.println("difference in seconds" + seconds);
            float minutes = seconds / 60;
            System.out.println("difference in minutes" + minutes);
            hours = minutes / 60;

            System.out.println("difference in hours" + hours);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        
       /* if (ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim().equals("PM")) {
            if (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) == 12) {
                inTime = (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim())) + ":" +
                        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
            } else {
                inTime = (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) + 12) + ":" +
                        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
            }

        } else {
            if (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) == 12) {
                inTime = Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) + 12 + ":" +
                        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
            } else {
                inTime = hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim() + ":" +
                        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
            }

        }

        if (ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim().equals("PM")) {
            if (Integer.parseInt(hhInSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) == 12) {
                outTime = (Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim())) + ":" +
                        mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
            } else {
                outTime = (Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) + 12) + ":" +
                        mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
            }

        } else {
            if (Integer.parseInt(hhInSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) == 12) {
                outTime = (Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) + 12) + ":" +
                        mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
            } else {
                outTime = hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim() + ":" +
                        mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
            }

        }

        Log.e("diffTime", "INT "+ inTime);
        Log.e("diffTime","OUTT "+ outTime);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        Date d1 = null;
        Date d2 = null;

        try {
            // Below Code Written by GauravA & this is in not execution upto ----- line
//--------------------------------------------------------------------------------------------------
            String AMPMSSStatusIF = ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim();
            String AMPMESStatusIF = ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim();

            if(AMPMSSStatusIF.equals("AM")&&AMPMESStatusIF.equals("PM")){

                Log.d("diffTime", "AMPMSSStatusIF "+ AMPMSSStatusIF +"  "+ AMPMESStatusIF+ " AMPMESStatusIF ");
                Log.d("diffTime", "Here AM & then PM Selected so we need to calculate time here.");

                // Code Written by GaneshK & this is in execution

                d1 = format.parse(inTime);
                Log.e("diffTime", "Date d1 "+ String.valueOf(d1));
                d2 = format.parse(outTime);
                Log.e("diffTime","Date d2 "+ String.valueOf(d2));

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();
                long diffHours = diff / (60 * 60 * 1000) % 24;
                Log.e("diffTime", "long diff "+ String.valueOf(diff));

                time = TimeUnit.MILLISECONDS.toHours(diff) + ":" +(TimeUnit.MILLISECONDS.toMinutes(diff) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)));

                Log.e("diffTime", time + " hours, ");

                String[] separated = time.split(":");
                String hrs = separated[0]; // this will contain "Fruit"
                String mins = separated[1]; // this will contain " they taste good"

                Log.d("diffTime", hrs + " hours, " + mins +" minutes");

                if(mins.equals("0")){
                    mins = "0";
                    Log.d("diffTime", hrs + " hours, " + mins +" minutes");
                }else if(mins.equals("15")){
                    mins = "25";
                    Log.d("diffTime", hrs + " hours, " + mins +" minutes");
                }else if(mins.equals("30")){
                    mins = "50";
                    Log.d("diffTime", hrs + " hours, " + mins +" minutes");
                }else if(mins.equals("45")){
                    mins = "75";
                    Log.d("diffTime", hrs + " hours, " + mins +" minutes");
                }else {
                    Log.d("diffTime", "Nothing" + " hours, " + "Nothing" +" minutes");
                }

                String totalhrs = hrs + "." + mins;
                Log.d("diffTime", "totalhrs " + totalhrs);

                totaldiffbetinout = Float.parseFloat(totalhrs);

                Log.d("diffTime", "totalhrs " + totaldiffbetinout);

            }else {

                String AMPMSSStatus = ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim();
                int hhintime = Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());
                int mmintime = Integer.parseInt(mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim());
                Log.d("diffTime", "hhintime "+ hhintime +" mmintime "+ mmintime+ " AMPMSSStatus "+AMPMSSStatus);

                if(hhintime == 12){
                    hhintime = 0;
                }else {

                }

                Log.d("diffTime", "modify hhintime "+hhintime);

                int hhintimetosec = hhintime * 60;
                int hhintimetosec_minadd = hhintimetosec + mmintime;
                Log.d("diffTime", "hhintimetosec_minadd "+hhintimetosec_minadd);
//==================================================================================================
                String AMPMESStatus = ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim();
                int hhouttime = Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim());
                int mmouttime = Integer.parseInt(mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim());
                Log.d("diffTime", "hhouttime "+ hhouttime + " mmouttime "+mmouttime + " AMPMESStatus "+AMPMESStatus);

                int hhouttimetosec = hhouttime * 60;
                int hhouttimetosec_minadd = hhouttimetosec + mmouttime;
                Log.d("diffTime", "hhouttimetosec_minadd "+ hhouttimetosec_minadd);
//==================================================================================================
                int diff_hhout_hhin = hhouttimetosec_minadd - hhintimetosec_minadd;
                Log.d("diffTime", "Difference hhouttime hhintime "+ diff_hhout_hhin);
//--------------------------------------------------------------------------------------------------
                totaldiffbetinout = (float) diff_hhout_hhin / (float) 60;
                Log.d("diffTime", "Total Difference "+ totaldiffbetinout);
//--------------------------------------------------------------------------------------------------
            }


        } catch (Exception e) {
            // TODO: handle exception
        }
*/
        //return time;
        return String.valueOf(hours);
    }

    public String totalPay(String payRate) {

        String diff = diffTime().replace(":", ".");
        Log.e("difftotal", diff);

        double total = Double.parseDouble(diff) * Double.parseDouble(payRate);

        Log.e("total", String.valueOf(total));
        return String.valueOf(total);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBTN:
                try {

                    if (!hhS) {
                        ToastUlility.show(AddEditHoursActivity.this, "Please select InTime(Hr)");
                    } else if (!mmS) {
                        ToastUlility.show(AddEditHoursActivity.this, "Please select start time Min:");
                    } else if (!ampmS) {
                        ToastUlility.show(AddEditHoursActivity.this, "Please select start AM/PM");
                    } else if (!hhE) {
                        ToastUlility.show(AddEditHoursActivity.this, "Please select OutTime(Hr)");
                    } else if (!mmE) {
                        ToastUlility.show(AddEditHoursActivity.this, "Please select end time Min:");
                    } else if (!ampmE) {
                        ToastUlility.show(AddEditHoursActivity.this, "Please select end AM/PM");
                    } else if (!subjectB) {
                        ToastUlility.show(this, "You must select a subject.");
                    } else {
                        if (hhSS.getSelectedItem().toString().equalsIgnoreCase("InTime(Hr)")) {
                            ToastUlility.show(AddEditHoursActivity.this, "Please select InTime(Hr)");
                        } else if (hhES.getSelectedItem().toString().equalsIgnoreCase("OutTime(Hr)")) {
                            ToastUlility.show(AddEditHoursActivity.this, "Please select OutTime(Hr)");
                        } else if (mmSS.getSelectedItem().toString().equalsIgnoreCase("Min:")) {
                            ToastUlility.show(AddEditHoursActivity.this, "Please select start time Min:");
                        } else if (mmES.getSelectedItem().toString().equalsIgnoreCase("Min:")) {
                            ToastUlility.show(AddEditHoursActivity.this, "Please select end time Min:");
                        } else if (ampmSS.getSelectedItem().toString().equalsIgnoreCase("AM/PM")) {
                            ToastUlility.show(AddEditHoursActivity.this, "Please select start AM/PM");
                        } else if (ampmES.getSelectedItem().toString().equalsIgnoreCase("AM/PM")) {
                            ToastUlility.show(AddEditHoursActivity.this, "Please select end AM/PM");
                        } else {
                            String subject = subjectS.getSelectedItem().toString().trim();

                            int outSpin = Integer.parseInt(hhOutSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());
                            int inSpin = Integer.parseInt(hhInSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim());
                            String ssampm = ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim();
                            String esampm = ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim();


                            Log.e("SpinStatus", "inSpin " + inSpin + " outSpin " + outSpin + " SSTIme " + ssampm + " SSTIme " + esampm);

                            if ((ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim())
                                    .equalsIgnoreCase((ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim()))) {

                                Log.e("SpinStatus", "inSpin " + inSpin + " outSpin " + outSpin + " SSTIme " + ssampm + " SSTIme " + esampm);

                                if (inSpin > outSpin) {

                                    Log.e("SpinStatus", "inSpin > outSpin " + "inSpin " + inSpin + " outSpin " + outSpin);

//                            submit();
                                    try {
                                        if (subject.equalsIgnoreCase("")) {
                                            ToastUlility.show(this, "You must select a subject.");
                                        } else if (subject.equalsIgnoreCase("Select Student")) {
                                            ToastUlility.show(this, "You must select a subject.");
                                        } else {
//                                    ToastUlility.show(this, subjectS.getSelectedItem().toString());

                                            submit();
                                        }

                                    } catch (Exception e) {
                                        ToastUlility.show(this, "You must select a subject.");
                                    }

                                } else {

                                    if (inSpin == outSpin) {

                                        Log.e("SpinStatus", "inSpin == outSpin " + "inSpin " + inSpin + " outSpin " + outSpin);

                                        if (Integer.parseInt(mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim()) <
                                                Integer.parseInt(mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim())) {

                                            try {
                                                if (subjectS.getSelectedItem().toString().equalsIgnoreCase("")) {
                                                    ToastUlility.show(this, "You must select a subject.");
                                                } else if (subjectS.getSelectedItem().toString().equalsIgnoreCase("Select Student")) {
                                                    ToastUlility.show(this, "You must select a subject.");
                                                } else {
//                                    ToastUlility.show(this, subjectS.getSelectedItem().toString());

                                                    submit();
                                                }

                                            } catch (Exception e) {
                                                ToastUlility.show(this, "You must select a subject.");
                                            }

                                        } else {
                                            ToastUlility.show(AddEditHoursActivity.this, "Selected out time minutes less than in time minutes");
                                        }
                                    } else {

                                        // Add this 17/10/2017 because below client point
                                        // this Toast only Here //ToastUlility.show(AddEditHoursActivity.this, "Selected out time less than in time.");
                                        // 1. When  I try to input 12pm and a later time such as 2pm the app gives the error message
                                        // "selected out time is less than in time" which makes no sense
                                        // since 12pm is less than 2pm and they go in that order

                                        Log.e("SpinStatus", "ELSE inSpin == outSpin " + "inSpin " + inSpin + " outSpin " + outSpin);

                                        Log.e("SpinStatus", "mmSS inSpin == outSpin " + mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim());
                                        Log.e("SpinStatus", "mmES inSpin == outSpin " + mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim());

                                        if (Integer.parseInt(mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim()) <=
                                                Integer.parseInt(mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim())) {
                                            try {
                                                if (subjectS.getSelectedItem().toString().equalsIgnoreCase("")) {
                                                    ToastUlility.show(this, "You must select a subject.");
                                                } else if (subjectS.getSelectedItem().toString().equalsIgnoreCase("Select Student")) {
                                                    ToastUlility.show(this, "You must select a subject.");
                                                } else {
                                                    //                                    ToastUlility.show(this, subjectS.getSelectedItem().toString());
                                                    Log.e("SpinStatus", "Submit");
                                                    submit();
                                                }
                                            } catch (Exception e) {
                                                ToastUlility.show(this, "You must select a subject.");
                                            }
                                        } else {
                                            Log.e("SpinStatus", "ELSE inSpin == outSpin " + "Selected out time minutes less than in time minutes");
                                            ToastUlility.show(AddEditHoursActivity.this, "Selected out time minutes less than in time minutes");
                                        }

                                    }
                                }
                            } else if ((ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim())
                                    .equalsIgnoreCase("AM") && (ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim())
                                    .equalsIgnoreCase("PM")) {

                                Log.e("SpinStatus", "AM == PM " + "inSpin " + inSpin + " outSpin " + outSpin);

                                submit();
                            } else {

                                Log.e("SpinStatus", "ELSE " + "inSpin " + inSpin + " outSpin " + outSpin);
                                ToastUlility.show(AddEditHoursActivity.this, "Please correct the AM/PM time stamp.");
                            }
                        }
                    }
                } catch (Exception e) {
                    ToastUlility.show(AddEditHoursActivity.this, "Student does not have enough hours for this entry");
                }


                break;
        }
    }

    public void submit() {
        String diff = diffTime().replace(":", ".");
        //totalPay(subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim());

        final String inTime, outTime;
        String time = "", totalhrs = "";
        int miustotalhrs = 0, miustotalmin = 0;

        /*if (ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim().equals("PM")) {
            if (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) == 12) {
                inTime = (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim())) + ":" +
                        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
            } else {
                inTime = (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) + 12) + ":" +
                        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
            }

        } else {
            if (Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) == 12) {
                inTime = Integer.parseInt(hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim()) + 12 + ":" +
                        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
            } else {
                inTime = hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim() + ":" +
                        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim();
            }

        }

        if (ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim().equals("PM")) {
            if (Integer.parseInt(hhInSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) == 12) {
                outTime = (Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim())) + ":" +
                        mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
            } else {
                outTime = (Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) + 12) + ":" +
                        mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
            }

        } else {
            if (Integer.parseInt(hhInSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) == 12) {
                outTime = (Integer.parseInt(hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim()) + 12) + ":" +
                        mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
            } else {
                outTime = hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim() + ":" +
                        mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim();
            }

        }

        Log.e("diffTime", "INT " + inTime);
        Log.e("diffTime", "OUTT " + outTime);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        Date d1 = null;
        Date d2 = null;*/


       /* try {

            // Code Written by GaneshK & this is in execution

            d1 = format.parse(inTime);
            Log.e("diffTime", "Date d1 " + String.valueOf(d1));
            d2 = format.parse(outTime);
            Log.e("diffTime", "Date d2 " + String.valueOf(d2));

            //in milliseconds
            long diffnew = d2.getTime() - d1.getTime();
            long diffHours = diffnew / (60 * 60 * 1000) % 24;
            Log.e("diffTime", "long diff " + String.valueOf(diffnew));

            time = TimeUnit.MILLISECONDS.toHours(diffnew) + ":" + (TimeUnit.MILLISECONDS.toMinutes(diffnew) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffnew)));

            Log.e("diffTime", time + " hours, ");

            String[] separated = time.split(":");
            String hrs = separated[0]; // this will contain "Fruit"
            String mins = separated[1]; // this will contain " they taste good"

            Log.d("diffTime", hrs + " hours, " + mins + " minutes");

            if (mins.equals("0")) {
                mins = "0";
                Log.d("diffTime", hrs + " hours, " + mins + " minutes");
            } else if (mins.equals("15")) {
                mins = "25";
                Log.d("diffTime", hrs + " hours, " + mins + " minutes");
            } else if (mins.equals("30")) {
                mins = "50";
                Log.d("diffTime", hrs + " hours, " + mins + " minutes");
            } else if (mins.equals("45")) {
                mins = "75";
                Log.d("diffTime", hrs + " hours, " + mins + " minutes");
            } else {
                Log.d("diffTime", "Nothing" + " hours, " + "Nothing" + " minutes");
            }

            totalhrs = hrs + "." + mins;
            Log.d("diffTime", "totalhrs " + totalhrs);

            String[] timeseparated = time.split(":");
            int timehrs = Integer.parseInt(timeseparated[0]); //
            int timemins = Integer.parseInt(timeseparated[1]); //

            Log.d("diffTimeseparat", timehrs + " hours, " + timemins + " minutes");

            Log.e("diffTimeTotalcome", Totalcomeherehrs + " hours, ");

            if (Totalcomeherehrs.equals("")) {


                String[] parts = hrsLeft.split("\\."); // escape .
                int hrsLeftcomehrs = Integer.parseInt(parts[0]);
                int hrsLeftcomemins = Integer.parseInt(parts[1]);

                Log.d("diffTimehrsleft", "Totalcomeherehrs IF " + hrsLeftcomehrs + " hours, " + hrsLeftcomemins + " minutes");

                int cometotalhrs = hrsLeftcomehrs;
                int cometotalmins = hrsLeftcomemins;

                Log.d("diffTimeTotal", "Totalcomeherehrs IF " + cometotalhrs + " hours, " + cometotalmins + " minutes");
                Totalcomeherehrs = cometotalhrs + ":" + cometotalmins;

                Log.e("diffTimeTotalcome", "Totalcomeherehrs IF " + Totalcomeherehrs + " hours, ");

                String[] separatedTotalcomeherehrs = Totalcomeherehrs.split(":");
                int comehrs = Integer.parseInt(separatedTotalcomeherehrs[0]); //
                int comemins = Integer.parseInt(separatedTotalcomeherehrs[1]); //
                Log.d("diffTimeCome", "Totalcomeherehrs IF " + comehrs + " hours, " + comemins + " minutes");

                miustotalhrs = comehrs - timehrs;
                miustotalmin = comemins - timemins;
                Log.d("diffTimeMINUS", "Totalcomeherehrs IF " + miustotalhrs + " hours, " + miustotalmin + " minutes");

                minustotal = miustotalhrs + "." + miustotalmin;
                Log.d("diffTimeMINUSvalue", "Totalcomeherehrs IF " + minustotal + " hours");

            } else {

                String[] separatedTotalcomeherehrs = Totalcomeherehrs.split(":");
                int comehrs = Integer.parseInt(separatedTotalcomeherehrs[0]); //
                int comemins = Integer.parseInt(separatedTotalcomeherehrs[1]); //
                Log.d("diffTimeCome", "Totalcomeherehrs Else " + comehrs + " hours, " + comemins + " minutes");

                miustotalhrs = comehrs - timehrs;
                miustotalmin = comemins - timemins;
                Log.d("diffTimeMINUS", "Totalcomeherehrs Else " + miustotalhrs + " hours, " + miustotalmin + " minutes");

                minustotal = miustotalhrs + "." + miustotalmin;
                Log.d("diffTimeMINUSvalue", "Totalcomeherehrs Else " + minustotal + " hours");

            }

        } catch (Exception e) {

        }*/
//==================================================================================================
        if (comeFrom.equals("Modify")) {

            Log.e("diff", "diff Modify IF " + diff + " hrsLeft " + hrsLeft);
            if (Double.parseDouble(diff) < Double.parseDouble(hrsLeft)) {

                modifyInOutAttendance();
                //diffTime(); // use for checking total hours time
            } else {
                Log.e("diff", "diff Modify Else" + diff + " hrsLeft " + hrsLeft);
                //ToastUlility.show(AddEditHoursActivity.this, "Only " + hrsLeft + " left of this student");


                float totalcheckvalue = Float.parseFloat(minustotal);
                Log.e("diff", "miustotalhrs IF " + diff + " FloateValue " + totalcheckvalue);


                if (miustotalhrs >= 0) {
                    Log.e("diff", "miustotalhrs if" + diff + " hrsLeft " + hrsLeft);
                    if (miustotalmin >= 0) {
                        Log.e("diff", "diff Modify Else" + diff + " hrsLeft " + hrsLeft);
                        modifyInOutAttendanceNEW(diff);
                    } else {
                        ToastUlility.show(AddEditHoursActivity.this, "Student does not have enough hours for this entry");
                    }
                } else {
                    ToastUlility.show(AddEditHoursActivity.this, "Student does not have enough hours for this entry");
                }
            }

        } else if (comeFrom.equals("ADD")) {

            if (Double.parseDouble(diff) < Double.parseDouble(hrsLeft)) {
                addInOutAttendance();
                //diffTime();// use for checking total hours time
            } else {
                //ToastUlility.show(AddEditHoursActivity.this, "Only " + hrsLeft + " left of this student");

                Log.e("diff", String.valueOf(miustotalhrs) + " totalhrs " + totalhrs);

                if (miustotalhrs >= 0) {
                    Log.e("diff", "miustotalhrs If " + String.valueOf(miustotalhrs) + " totalhrs " + totalhrs);
                    if (miustotalmin >= 0) {
                        Log.e("diff", "miustotalmin If " + String.valueOf(miustotalhrs) + " totalhrs " + totalhrs);
                        addInOutAttendanceNEW(diff);
                    } else {
                        ToastUlility.show(AddEditHoursActivity.this, "Student does not have enough hours for this entry");
                    }
                } else {
                    ToastUlility.show(AddEditHoursActivity.this, "Student does not have enough hours for this entry");
                }


            }

        }
    }

    public void modifyInOutAttendance() {


        final String hourIn = hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim() + ":" +
                mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim();

        Log.e("hourOut", hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", hourIn);

        final String hourOut = hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim() + ":" +
                mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim();

        Log.e("hourOut", hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", hourOut);

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.modifyAttendance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("EditRESPONSE", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {
//                        settingsDialog.dismiss();
                        loadingDialog.dismiss();
                        String message = jobj.getString("message");
                        Log.e("modifymessage", message);
                        ToastUlility.show(AddEditHoursActivity.this, message);
                        onBackPressed();
//                        finish();
//                        Intent intent = new Intent(AddEditHoursActivity.this, InOutAttendenceActivity.class);
//                        startActivity(intent);
//                        finish();


                    } else {

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
                params.put("AttendanceID", attendanceId);
//                params.put("StudentID", StudentId);
//                params.put("StartDT", dateSelected);
//                params.put("EndDT", dateSelected);

                String diff = diffTime().replace(":", ".");
                Log.d("TotalHoursdiff", diff);

                //params.put("TotalHours", diffTime());
                params.put("TotalHours", diff);
                params.put("PayRate", subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim());
                params.put("TotalPay", totalPay(subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim()));
                params.put("Comments", commentsTV.getText().toString().trim());
                params.put("SubjectTaught", subjectNameList.get(subjectS.getSelectedItemPosition()).toString().trim());
                params.put("HourIn", hourIn);
                params.put("HourOut", hourOut);

                Log.e("hourOut", params.toString());
                return params;

            }
        };
        String request_tag = "modify_in_out_attendance";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    public void modifyInOutAttendanceNEW(final String Difference) {

        Log.d("diffmodifyNEW", Difference + " hours");

        final String hourIn = hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim() + ":" +
                mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim();

        Log.e("hourOut", hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", hourIn);

        final String hourOut = hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim() + ":" +
                mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim();

        Log.e("hourOut", hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim());
        Log.e("hourOut", hourOut);

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.modifyAttendance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("EditRESPONSE", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                    int message_code = jobj.getInt("message_code");
                    if (message_code == 1) {
//                        settingsDialog.dismiss();
                        loadingDialog.dismiss();
                        String message = jobj.getString("message");
                        Log.e("modifymessage", message);
                        ToastUlility.show(AddEditHoursActivity.this, message);
                        onBackPressed();
//                        finish();
//                        Intent intent = new Intent(AddEditHoursActivity.this, InOutAttendenceActivity.class);
//                        startActivity(intent);
//                        finish();


                    } else {

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
                params.put("AttendanceID", attendanceId);
//                params.put("StudentID", StudentId);
//                params.put("StartDT", dateSelected);
//                params.put("EndDT", dateSelected);

                //params.put("TotalHours", diffTime());
                params.put("TotalHours", Difference);
                params.put("PayRate", subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim());
                params.put("TotalPay", totalPay(subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim()));
                params.put("Comments", commentsTV.getText().toString().trim());
                params.put("SubjectTaught", subjectNameList.get(subjectS.getSelectedItemPosition()).toString().trim());
                params.put("HourIn", hourIn);
                params.put("HourOut", hourOut);

                Log.e("diffmodifyNEW", params.toString());
                return params;

            }
        };
        String request_tag = "modify_in_out_attendance";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    public void addInOutAttendanceNEW(final String Difference) {


        final String hourIn = hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim() + ":" +
                mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmSS.getSelectedItemPosition()).toString().trim();

        Log.e("hourIn0", hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourIn0", hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());
        Log.e("hourIn0", hhInSpinnerList.get(hhSS.getSelectedItemPosition()).toString().trim());

        //final String hourOut = hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim() + ":" +
        //        mmSpinnerList.get(mmSS.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim();

        final String hourOut = hhOutSpinnerList.get(hhES.getSelectedItemPosition()).toString().trim() + ":" +
                mmSpinnerList.get(mmES.getSelectedItemPosition()).toString().trim() + " " + ampmSpinnerList.get(ampmES.getSelectedItemPosition()).toString().trim();

        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppAPI.addInOutAttendance, new Response.Listener<String>() {
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
                        String message = jobj.getString("message");
                        ToastUlility.show(AddEditHoursActivity.this, message);
                        onBackPressed();
//                        finish();
//                        Intent intent = new Intent(AddEditHoursActivity.this, InOutAttendenceActivity.class);
//                        startActivity(intent);
//                        finish();


                    } else {
                        loadingDialog.dismiss();
                        String message = jobj.getString("message");
                        ToastUlility.show(AddEditHoursActivity.this, message);
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
                params.put("StudentID", StudentId);
                params.put("StartDT", dateSelected);
                params.put("EndDT", dateSelected);

                //String diff = diffTime().replace(":", ".");
                String diff = diffTime();
                Log.d("TotalHoursdiff", diff);

                //params.put("TotalHours", diffTime());
                params.put("TotalHours", Difference);
                params.put("PayRate", subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim());
                params.put("TotalPay", totalPay(subjectRateList.get(subjectS.getSelectedItemPosition()).toString().trim()));
                params.put("Comments", commentsTV.getText().toString().trim());
                params.put("SubjectTaught", subjectNameList.get(subjectS.getSelectedItemPosition()).toString().trim());
                params.put("HourIn", hourIn);
                params.put("HourOut", hourOut);

                Log.e("PARAMS", params.toString());
                return params;

            }
        };
        String request_tag = "add_in_out_attendance";
//        queue.add(stringRequest);
        MainApplication.getInstance().addToRequestQueue(stringRequest, request_tag);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddEditHoursActivity.this, InOutAttendenceActivity.class);
        intent.putExtra("StudentId", StudentId);
        startActivity(intent);
        finish();

//        super.onBackPressed();
//        Intent intent = new Intent()
    }
}
