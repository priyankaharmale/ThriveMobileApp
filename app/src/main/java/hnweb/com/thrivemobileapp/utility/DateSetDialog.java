package hnweb.com.thrivemobileapp.utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by neha on 5/31/2017.
 */

public class DateSetDialog {

    public static void datePicker(final TextView dateTV, Activity activity) {

        int mYear, mMonth, mDay, mHour, mMinute;

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Log.e("dayofMont", dayOfMonth + "");

                        if (monthOfYear + 1 < 10) {
                            if (dayOfMonth < 10) {
                                dateTV.setText("0" + (monthOfYear + 1) + "/" + "0" + dayOfMonth + "/" + year);
                            } else {
                                dateTV.setText("0" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }

                        } else {
                            if (dayOfMonth < 10) {
                                dateTV.setText((monthOfYear + 1) + "/" + "0" + dayOfMonth + "/" + year);
                            } else {
                                dateTV.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }

                        }

//                        dobET.setError(null);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }
}
