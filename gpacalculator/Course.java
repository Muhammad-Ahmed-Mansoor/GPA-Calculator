package com.mansoor.gpacalculator;




public class Course
{
    //fields
    private String courseTitle;
    private int credHrs;
    private float courseGpa;
    private int semesterNum;
    private int courseNum;


    //constructor
    public Course(String gTitle, int ch, float gpa)
    {
        courseTitle=gTitle;
        courseGpa=gpa;
        credHrs=ch;
    }
    //---------------------------------------------------------------------------------------
    public Course(){}
    //---------------------------------------------------------------------------------------


    //setters
    public void setCourseTitle(String gTitle)
    {
        courseTitle=gTitle;
    }
    //---------------------------------------------------------------------------------------
    public void setCredHrs(int gHrs)
    {
        credHrs=gHrs;
    }
    //---------------------------------------------------------------------------------------
    public void setCourseGpa(float gGpa)
    {
        courseGpa=gGpa;
    }
    //---------------------------------------------------------------------------------------
    public void setSemesterNum(int semesterNum) {  this.semesterNum = semesterNum; }
    //---------------------------------------------------------------------------------------
    public void setCourseNum(int courseNum) { this.courseNum = courseNum;  }
    //---------------------------------------------------------------------------------------
    //getters
    public String getCourseTitle()
    {
        return courseTitle;
    }
    //---------------------------------------------------------------------------------------
    public int getCredHrs()
    {
        return credHrs;
    }
    //---------------------------------------------------------------------------------------
    public float getCourseGpa() {return courseGpa;}
    //---------------------------------------------------------------------------------------
    public int getSemesterNum() { return semesterNum; }
    //---------------------------------------------------------------------------------------

    public int getCourseNum() { return courseNum; }
    //---------------------------------------------------------------------------------------



}
