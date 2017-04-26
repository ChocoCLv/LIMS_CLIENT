package com.choco.limsclient.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by choco on 2017/4/23.
 */

public class ProjectInfo {
    private String courseName;
    private String projectName;
    private String projectStartTime;
    private String projectEndTime;
    private String projectDate;
    private String courseId;
    private String teacherId;
    private String teacherName;
    private String projectLoc;
    private JSONObject info;

    public ProjectInfo(String courseName, String projectName, String teacherId, String projectDate, String projectStartTime, String projectEndTime, String projectLoc) {
        this.courseName = courseName;
        this.projectName = projectName;
        this.projectStartTime = projectStartTime;
        this.projectEndTime = projectEndTime;
        this.projectDate = projectDate;
        this.teacherId = teacherId;
        this.projectLoc = projectLoc;
    }

    public ProjectInfo(String info){
        try {
            JSONObject jo = new JSONObject(info);
            this.info = jo;
            parseInfoFromJSONObject(this.info);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ProjectInfo(JSONObject info){
        this.info = info;
        parseInfoFromJSONObject(this.info);
    }

    private void parseInfoFromJSONObject(JSONObject info){
        try {
            this.courseName = info.getString("COURSE_NAME");
            this.projectName = info.getString("PROJECT_NAME");
            this.projectStartTime = info.getString("PROJECT_START_TIME");
            this.projectEndTime = info.getString("PROJECT_END_TIME");
            this.projectDate = info.getString("PROJECT_DATE");
            this.teacherId = info.getString("TEACHER_ID");
            this.projectLoc = info.getString("PROJECT_LOC");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String toString(){
        return info.toString();
    }

    public String get(String key) {
        String result = null;
        if ("courseName".equals(key)) {
            result = getCourseName();
        } else if ("projectName".equals(key)) {
            result = getProjectName();
        } else if ("projectDateTime".equals(key)) {
            result = getProjectDateTime();
        } else if ("projectLoc".equals(key)) {
            result = getProjectLoc();
        }
        return result;
    }

    public String getProjectDateTime() {
        return projectDate + " " + projectStartTime + "-" + projectEndTime;
    }


    public String getProjectLoc() {
        return projectLoc;
    }

    public void setProjectLoc(String projectLoc) {
        this.projectLoc = projectLoc;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(String projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public String getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(String projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    public String getProjectDate() {
        return projectDate;
    }

    public void setProjectDate(String projectDate) {
        this.projectDate = projectDate;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
