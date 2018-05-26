package hnweb.com.thrivemobileapp.adapter;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.activities.DownloadFormActivity;
import hnweb.com.thrivemobileapp.pojo.DownloadFileFromURL;
import hnweb.com.thrivemobileapp.pojo.DownloadPDF;

/**
 * Created by neha on 6/19/2017.
 */

public class DownloadFormsAdapter extends RecyclerView.Adapter<DownloadFormsAdapter.DownloadPDFViewHolder> {
    ArrayList<DownloadPDF> downloadPDFArrayList;
    DownloadFormActivity activity;
    ProgressDialog pDialog;


    public DownloadFormsAdapter(ArrayList<DownloadPDF> downloadPDFArrayList, DownloadFormActivity downloadFormActivity, ProgressDialog pDialog) {
        this.downloadPDFArrayList = downloadPDFArrayList;
        this.activity = downloadFormActivity;
        this.pDialog = pDialog;
    }

    @Override
    public DownloadFormsAdapter.DownloadPDFViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(activity).inflate(R.layout.download_form_item, null);
        DownloadPDFViewHolder dlvh = new DownloadPDFViewHolder(layoutView);

        return dlvh;
    }

    @Override
    public void onBindViewHolder(DownloadFormsAdapter.DownloadPDFViewHolder holder, final int position) {

        holder.dateTV.setText(downloadPDFArrayList.get(position).getUploadDate());
        holder.descriptionTV.setText(downloadPDFArrayList.get(position).getDescription());
        holder.downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    new DownloadFileFromURL(downloadPDFArrayList.get(position).getFileLocation(), pDialog, activity).execute();
                } catch (Exception e) {
                    Toast.makeText(activity, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadPDFArrayList.size();
    }

    public class DownloadPDFViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTV, descriptionTV;
        public Button downloadBTN;


        public DownloadPDFViewHolder(View itemView) {
            super(itemView);
            dateTV = (TextView) itemView.findViewById(R.id.dateTV);
            downloadBTN = (Button) itemView.findViewById(R.id.downloadBTN);
            descriptionTV = (TextView) itemView.findViewById(R.id.descriptionTV);

        }
    }


}
