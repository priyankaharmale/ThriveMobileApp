package hnweb.com.thrivemobileapp.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.pojo.AttendanceReport;

/**
 * Created by neha on 6/1/2017.
 */

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportListViewHolder> {

    Activity activity;
    ArrayList<AttendanceReport> attendanceReportList;


    public ReportListAdapter(Activity activity, ArrayList<AttendanceReport> attendanceReportList) {
        this.activity = activity;
        this.attendanceReportList = attendanceReportList;
    }

    @Override
    public ReportListAdapter.ReportListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(activity).inflate(R.layout.report_list_adpter_item, null);
        ReportListViewHolder rlvh = new ReportListViewHolder(layoutView);

        return rlvh;
    }

    @Override
    public void onBindViewHolder(ReportListAdapter.ReportListViewHolder holder, int position) {

        holder.studentNameTV.setText(attendanceReportList.get(position).getFName() + " " + attendanceReportList.get(position).getLName());
        holder.subjectTV.setText(attendanceReportList.get(position).getSubjectTaught());
        holder.inTimeTV.setText(attendanceReportList.get(position).getHourIn());
        holder.outTimeTV.setText(attendanceReportList.get(position).getHourOut());
        holder.totalHoursTV.setText(attendanceReportList.get(position).getTotalHours());
        holder.rateTV.setText("$ " + attendanceReportList.get(position).getPayRate());
        holder.dateTV.setText(attendanceReportList.get(position).getEndDT());
        holder.totalPayTV.setText("$ " + attendanceReportList.get(position).getTotalPay());
    }

    @Override
    public int getItemCount() {
        return attendanceReportList.size();
    }

    public class ReportListViewHolder extends RecyclerView.ViewHolder {
        public TextView studentNameTV, subjectTV, inTimeTV, outTimeTV, totalHoursTV, rateTV, dateTV, totalPayTV;

        public ReportListViewHolder(View itemView) {
            super(itemView);

            studentNameTV = (TextView) itemView.findViewById(R.id.studentNameTV);
            subjectTV = (TextView) itemView.findViewById(R.id.subjectTV);
            inTimeTV = (TextView) itemView.findViewById(R.id.inTimeTV);
            outTimeTV = (TextView) itemView.findViewById(R.id.outTimeTV);
            totalHoursTV = (TextView) itemView.findViewById(R.id.totalHoursTV);
            rateTV = (TextView) itemView.findViewById(R.id.rateTV);
            dateTV = (TextView) itemView.findViewById(R.id.dateTV);
            totalPayTV = (TextView) itemView.findViewById(R.id.totalPayTV);

        }
    }
}
