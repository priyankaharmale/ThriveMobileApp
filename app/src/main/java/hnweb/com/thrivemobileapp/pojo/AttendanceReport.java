package hnweb.com.thrivemobileapp.pojo;

/**
 * Created by neha on 6/1/2017.
 */

public class AttendanceReport {
    String FName;
    String LName;
    String AttendanceID;
    String TutorID;
    String StudentID;
    String StartDT;
    String EndDT;
    String TotalHours;
    String PayRate;
    String TotalPay;
    String Comments;
    String SubjectTaught;
    String HourIn;
    String HourOut;

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getAttendanceID() {
        return AttendanceID;
    }

    public void setAttendanceID(String attendanceID) {
        AttendanceID = attendanceID;
    }

    public String getTutorID() {
        return TutorID;
    }

    public void setTutorID(String tutorID) {
        TutorID = tutorID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
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

    public String getTotalHours() {
        return TotalHours;
    }

    public void setTotalHours(String totalHours) {
        TotalHours = totalHours;
    }

    public String getPayRate() {
        return PayRate;
    }

    public void setPayRate(String payRate) {
        PayRate = payRate;
    }

    public String getTotalPay() {
        return TotalPay;
    }

    public void setTotalPay(String totalPay) {
        TotalPay = totalPay;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getSubjectTaught() {
        return SubjectTaught;
    }

    public void setSubjectTaught(String subjectTaught) {
        SubjectTaught = subjectTaught;
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
