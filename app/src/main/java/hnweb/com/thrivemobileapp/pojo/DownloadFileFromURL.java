package hnweb.com.thrivemobileapp.pojo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import hnweb.com.thrivemobileapp.activities.DownloadFormActivity;
import hnweb.com.thrivemobileapp.activities.WebViewActivity;

/**
 * Created by neha on 6/19/2017.
 */

public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    String fileLocation;
    ProgressDialog pDialog;
    Activity activity;
    String mypdfpath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + "StudentDailyProgressReport_Thrive" + System.currentTimeMillis() + ".pdf";

    public DownloadFileFromURL(String fileLocation, ProgressDialog pDialog, DownloadFormActivity activity) {
        this.pDialog = pDialog;
        this.fileLocation = fileLocation;
        this.activity = activity;
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.show();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(fileLocation);
            URLConnection conection = url.openConnection();
            conection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream
            OutputStream output = new FileOutputStream(mypdfpath);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        pDialog.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        pDialog.dismiss();

        // Displaying downloaded image into image view
        // Reading image path from sdcard
        String imagePath = mypdfpath;
//                = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + "StudentDailyProgressReport_Thrive.pdf";
        // setting downloaded into image view

        Toast.makeText(activity, "Dowloaded at Location\n" + imagePath, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("PATH", imagePath);
        activity.startActivity(intent);
//        Intent intent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse(imagePath));
//        intent.setType("application/pdf");
//        PackageManager pm = activity.getPackageManager();
//        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
//        if (activities.size() > 0) {
//            activity.startActivity(intent);
//        } else {
//            Toast.makeText(activity, "No pdf reader found, please install pdf reader to view pdf file.", Toast.LENGTH_SHORT).show();
//            // Do something else here. Maybe pop up a Dialog or Toast
//        }
//            my_image.setImageDrawable(Drawable.createFromPath(imagePath));
    }

}