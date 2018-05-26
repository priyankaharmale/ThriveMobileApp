package hnweb.com.thrivemobileapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.utility.Logout;

public class TutorProfileActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView tutorProfileRV;
    TextView nameTV, addressTV, cityTV, stateTV, zipcodeTV, phone1TV, phone2TV,
            emailTV, statusTV, subjectsTTV, usernameTV,
            passwordTV, backgroundclearanceTV, tbTestTV,
            gradePrefTV, degreeTV, travelPrefTV, languageTV, notesTV;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Intent bundle;
    SharedPreferences sharedPreferences;
    ImageView logoutIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);


        prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        editor = prefs.edit();

        logoutIV = (ImageView) findViewById(R.id.logoutIV);
        nameTV = (TextView) findViewById(R.id.nameTV);
        addressTV = (TextView) findViewById(R.id.addressTV);
        cityTV = (TextView) findViewById(R.id.cityTV);
        stateTV = (TextView) findViewById(R.id.stateTV);
        zipcodeTV = (TextView) findViewById(R.id.zipcodeTV);
        phone1TV = (TextView) findViewById(R.id.phone1TV);
        phone2TV = (TextView) findViewById(R.id.phone2TV);
        emailTV = (TextView) findViewById(R.id.emailTV);
        statusTV = (TextView) findViewById(R.id.statusTV);
        subjectsTTV = (TextView) findViewById(R.id.subjectsTTV);
        usernameTV = (TextView) findViewById(R.id.usernameTV);
        passwordTV = (TextView) findViewById(R.id.passwordTV);
        backgroundclearanceTV = (TextView) findViewById(R.id.backgroundclearanceTV);
        tbTestTV = (TextView) findViewById(R.id.tbTestTV);
        gradePrefTV = (TextView) findViewById(R.id.gradePrefTV);
        degreeTV = (TextView) findViewById(R.id.degreeTV);
        travelPrefTV = (TextView) findViewById(R.id.travelPrefTV);
        languageTV = (TextView) findViewById(R.id.languageTV);
        notesTV = (TextView) findViewById(R.id.notesTV);

        nameTV.setText(sharedPreferences.getString("FName", "") + " " + sharedPreferences.getString("LName", ""));
        addressTV.setText(sharedPreferences.getString("Address", ""));
        cityTV.setText(sharedPreferences.getString("City", ""));
        stateTV.setText(sharedPreferences.getString("State", ""));
        zipcodeTV.setText(sharedPreferences.getString("ZipCode", ""));
        phone1TV.setText(sharedPreferences.getString("Phone1", ""));
        phone2TV.setText(sharedPreferences.getString("Phone2", ""));
        emailTV.setText(sharedPreferences.getString("Email", ""));
        statusTV.setText(sharedPreferences.getString("IsActive", ""));
        subjectsTTV.setText("");
        usernameTV.setText(sharedPreferences.getString("Username", ""));
        passwordTV.setText(sharedPreferences.getString("Password", ""));
        backgroundclearanceTV.setText(sharedPreferences.getString("BackgroundClearance", ""));
        tbTestTV.setText(sharedPreferences.getString("TBTest", ""));
        gradePrefTV.setText(sharedPreferences.getString("GradePref", ""));
        degreeTV.setText(sharedPreferences.getString("Degree", ""));
        travelPrefTV.setText(sharedPreferences.getString("TravelPref", ""));
        languageTV.setText(sharedPreferences.getString("Language", ""));
        notesTV.setText(sharedPreferences.getString("Notes", ""));


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
