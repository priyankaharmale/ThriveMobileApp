package hnweb.com.thrivemobileapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hnweb.com.thrivemobileapp.MultipartRequest.MultiPart_Key_Value_Model;
import hnweb.com.thrivemobileapp.MultipartRequest.MultipartFileUploaderAsync;
import hnweb.com.thrivemobileapp.MultipartRequest.OnEventListener;
import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.adapter.ProgressUploadAdapter;
import hnweb.com.thrivemobileapp.application.AppAPI;
import hnweb.com.thrivemobileapp.utility.PickerUtils;

public class ProgressReportUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int FROM_GALLARY = 103;
    private static final int REQUEST_CAMERA = 5;
    String encodedImage1;
    RecyclerView imagesRV;
    ArrayList<String> imagesList = new ArrayList<String>();
    ArrayList<MultiPart_Key_Value_Model> mult_list;
    SharedPreferences sharedPreferences;
    ProgressUploadAdapter adapter;
    ProgressDialog progressDialog;

//    ImageView imageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_report_upload);
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        imagesRV = (RecyclerView) findViewById(R.id.imagesRV);
        imagesRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        imagesRV.setLayoutManager(layoutManager);
        progressDialog = new ProgressDialog(this);
        Log.e("EMAIL", sharedPreferences.getString("Email", ""));

//        imageIV = (ImageView) findViewById(R.id.imageIV);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                System.out.println("REQUEST_CAMERA");

                encodedImage1 = PickerUtils.cameraPath();
                imagesList.add(encodedImage1);
                adapter = new ProgressUploadAdapter(ProgressReportUploadActivity.this, imagesList);
                imagesRV.setAdapter(adapter);
//                Glide.with(this).load(new File(encodedImage1)).into(imageIV);

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewBTN:
                if(imagesList.size()>=4)
                {
                    final Dialog dialog = new Dialog(ProgressReportUploadActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before

                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.show();
                    Button declineButton = (Button) dialog.findViewById(R.id.tv_ok);
                    // if decline button is clicked, close the custom dialog
                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close dialog

                            dialog.dismiss();
                        }
                    });
                }
                else
                {
                    PickerUtils.takePictureIntent(this, REQUEST_CAMERA);

                }
                break;
            case R.id.submitBTN:
                if (imagesList.size() != 0) {
                    progressDialog.show();
                    progressDialog.setMessage("Uploading...");
                    progressDialog.setCancelable(false);
                    doSendProgressReport();

                } else {
                    Toast.makeText(this, "Please capture at lease one image and then send.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public void doSendProgressReport() {


        mult_list = new ArrayList<MultiPart_Key_Value_Model>();

        MultiPart_Key_Value_Model OneObject = new MultiPart_Key_Value_Model();


        Map<String, String> fileParams = new HashMap<>();
        for (int i = 0; i < imagesList.size(); i++) {
            fileParams.put("images[" + i + "]", imagesList.get(i).toString());
        }

        Map<String, String> Stringparams = new HashMap<>();
        Stringparams.put("Email", sharedPreferences.getString("Email", ""));

        String requestURL = AppAPI.sendProgressReport;
        System.out.println("Email"+sharedPreferences.getString("Email", ""));
        System.out.println("Stringparams"+Stringparams.size());
        System.out.println("fileParams"+fileParams.size());

        OneObject.setUrl(requestURL);
        OneObject.setFileparams(fileParams);
        OneObject.setStringparams(Stringparams);

        MultipartFileUploaderAsync someTask = new MultipartFileUploaderAsync(getApplicationContext(), OneObject, new OnEventListener<String>() {

            @Override
            public void onSuccess(String object) {
             //   System.out.println("object.toString"+object.toString());
                Toast.makeText(getApplicationContext(), "" + object, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jobj = new JSONObject(object);
                    int message_code = jobj.getInt("message_code");
                    String message = jobj.getString("message");
                    if (message_code == 1) {
                        imagesList.clear();
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
//                        notify();
                    } else {
                        progressDialog.dismiss();
                    }

                    Toast.makeText(ProgressReportUploadActivity.this, message, Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        someTask.execute();
        return;

    }
}
