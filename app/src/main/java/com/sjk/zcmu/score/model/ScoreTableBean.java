package com.sjk.zcmu.score.model;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "所有成绩")
public class ScoreTableBean {
    @SmartColumn(id = 0, name = "学年")
    private String schoolYear;
    @SmartColumn(id = 1, name = "学期")
    private String term;
    @SmartColumn(id = 2, name = "课程代码")
    private String courseCode;
    @SmartColumn(id = 3, name = "课程名称")
    private String courseName;
    @SmartColumn(id = 4, name = "课程性质")
    private String courseNature;
    @SmartColumn(id = 5, name = "课程归属")
    private String courseAttach;
    @SmartColumn(id = 6, name = "学分")
    private String credit;
    @SmartColumn(id = 7, name = "绩点")
    private String point;
    @SmartColumn(id = 8, name = "成绩")
    private String score;
    @SmartColumn(id = 9, name = "辅修标记")
    private String minorMark;
    @SmartColumn(id = 10, name = "补考成绩")
    private String makeUpScore;
    @SmartColumn(id = 11, name = "重修成绩")
    private String retakeScore;
    @SmartColumn(id = 12, name = "学院名称")
    private String collegeName;
    @SmartColumn(id = 13, name = "备注")
    private String remark;
    @SmartColumn(id = 14, name = "重修标记")
    private String retakeMark;
    @SmartColumn(id = 15, name = "课程英文名称")
    private String englishNameOfCourse;

    public ScoreTableBean(String schoolYear, String term, String courseCode, String courseName, String courseNature, String courseAttach, String credit, String point, String score, String minorMark, String makeUpScore, String retakeScore, String collegeName, String remark, String retakeMark, String englishNameOfCourse) {
        this.schoolYear = schoolYear;
        this.term = term;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseNature = courseNature;
        this.courseAttach = courseAttach;
        this.credit = credit;
        this.point = point;
        this.score = score;
        this.minorMark = minorMark;
        this.makeUpScore = makeUpScore;
        this.retakeScore = retakeScore;
        this.collegeName = collegeName;
        this.remark = remark;
        this.retakeMark = retakeMark;
        this.englishNameOfCourse = englishNameOfCourse;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseNature() {
        return courseNature;
    }

    public void setCourseNature(String courseNature) {
        this.courseNature = courseNature;
    }

    public String getCourseAttach() {
        return courseAttach;
    }

    public void setCourseAttach(String courseAttach) {
        this.courseAttach = courseAttach;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMinorMark() {
        return minorMark;
    }

    public void setMinorMark(String minorMark) {
        this.minorMark = minorMark;
    }

    public String getMakeUpScore() {
        return makeUpScore;
    }

    public void setMakeUpScore(String makeUpScore) {
        this.makeUpScore = makeUpScore;
    }

    public String getRetakeScore() {
        return retakeScore;
    }

    public void setRetakeScore(String retakeScore) {
        this.retakeScore = retakeScore;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRetakeMark() {
        return retakeMark;
    }

    public void setRetakeMark(String retakeMark) {
        this.retakeMark = retakeMark;
    }

    public String getEnglishNameOfCourse() {
        return englishNameOfCourse;
    }

    public void setEnglishNameOfCourse(String englishNameOfCourse) {
        this.englishNameOfCourse = englishNameOfCourse;
    }
}
