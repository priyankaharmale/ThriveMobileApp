package hnweb.com.thrivemobileapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hnweb.com.thrivemobileapp.R;
import hnweb.com.thrivemobileapp.activities.StudentProfileActivity;
import hnweb.com.thrivemobileapp.pojo.StudentList;

/**
 * Created by neha on 5/26/2017.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentListViewHolder> {
    ArrayList<StudentList> studentListArrayList;
    Activity activity;

    public StudentListAdapter(ArrayList<StudentList> studentListArrayList, Activity activity) {
        this.activity = activity;
        this.studentListArrayList = studentListArrayList;
    }

    @Override
    public StudentListAdapter.StudentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(activity).inflate(R.layout.student_list_item, null);
        StudentListViewHolder dlvh = new StudentListViewHolder(layoutView);

        return dlvh;
    }

    @Override
    public void onBindViewHolder(StudentListAdapter.StudentListViewHolder holder, final int position) {
        holder.studentItemTV.setText(studentListArrayList.get(position).getFName() + " " + studentListArrayList.get(position).getLName() + "\n" + studentListArrayList.get(position).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, StudentProfileActivity.class);
                intent.putExtra("StudentID", studentListArrayList.get(position).getStudentID());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentListArrayList.size();
    }

    public static class StudentListViewHolder extends RecyclerView.ViewHolder {

        public TextView studentItemTV;

        public StudentListViewHolder(View itemView) {
            super(itemView);

            studentItemTV = (TextView) itemView.findViewById(R.id.studentItemTV);
        }
    }
}
