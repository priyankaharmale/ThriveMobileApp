package hnweb.com.thrivemobileapp.pojo;

/**
 * Created by hnwebmarketing on 10/25/2017.
 */

public class TutorCalendarDetails {

    private String AttendanceID,Comments,StartDT,EndDT,HourIn,HourOut;


    public String getAttendanceID() {
        return AttendanceID;
    }

    public void setAttendanceID(String attendanceID) {
        AttendanceID = attendanceID;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getStartDT() {
        return StartDT;
    }

    public void setStartDT(String startDT) {
        StartDT = startDT;
    }

    public String getEndDT() {
        return EndDT;
    }

    public void setEndDT(String endDT) {
        EndDT = endDT;
    }

    public String getHourIn() {
        return HourIn;
    }

    public void setHourIn(String hourIn) {
        HourIn = hourIn;
    }

    public String getHourOut() {
        return HourOut;
    }

    public void setHourOut(String hourOut) {
        HourOut = hourOut;
    }
}
