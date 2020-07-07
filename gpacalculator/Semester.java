package com.mansoor.gpacalculator;


import java.util.ArrayList;

public class Semester
{

    private int semesterNum;
    private ArrayList<Course> semesterCourses;
    private boolean availability;//num of semesters 0 or not
    private boolean accessibilty;//if a semester is unavailable, the ones after it are inaccessible

    //constructors

    public Semester(int num)
    {
        semesterCourses=new ArrayList<Course>();
        semesterNum=num;
        availability=true;
        accessibilty=true;
    }
//---------------------------------------------------------------------------------------

    //mutators
    public void addCourse(Course gCourse)
    {
        semesterCourses.add(gCourse);

    }
    //---------------------------------------------------------------------------------------
    public void removeCourse(int courseNum)
    {
        semesterCourses.remove(courseNum-1);
    }
    //---------------------------------------------------------------------------------------
    public void setAvailable(boolean avail)
    {
        availability=avail;
    }
    //---------------------------------------------------------------------------------------
    public void setAccessible(boolean avail)
    {
        accessibilty=avail;

        if(!accessibilty)
            availability=false; //an inaccessible semester is by default unavailable
    }
    //---------------------------------------------------------------------------------------


    //accessors
    public int getNumCourses()
    {
        return semesterCourses.size();
    }
    //---------------------------------------------------------------------------------------
    public Course getCourse(int index)
    {
        return semesterCourses.get(index-1);
    }
    //---------------------------------------------------------------------------------------
    public float getSGPA()
    {
        float sumProd=0;
        int sum=0;

        for(int i=1;i<=getNumCourses();i++)
        {
            sum += getCourse(i).getCredHrs();
            sumProd+= (getCourse(i).getCredHrs()*getCourse(i).getCourseGpa());

        }



        return (sumProd/sum);
    }

    //---------------------------------------------------------------------------------------
    public boolean isAvailable()
    {
        return availability;
    }
    //---------------------------------------------------------------------------------------
    public boolean isAccessible()
    {
        return accessibilty;
    }
    //---------------------------------------------------------------------------------------
    public void overWriteCourse(int courseNum,Course newCourse)
    {
        semesterCourses.set(courseNum-1,newCourse);
    }
    //---------------------------------------------------------------------------------------


}

