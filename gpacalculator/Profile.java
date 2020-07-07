package com.mansoor.gpacalculator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Profile
{
    public static final int NUM_SEMESTERS=8;
    private boolean cgpaAvailability;
    private static final int COURSE_ADDED=1;
    private static final int COURSE_REMOVED=2;
    private File externalDirectory;
    private Semester[] semesters;
    private float cgpa;

    //constructor
    public Profile(File gFile)
    {
        externalDirectory=gFile;
        semesters = new Semester[NUM_SEMESTERS];
        for (int i=0;i<NUM_SEMESTERS;i++)
            semesters[i]=new Semester(i+1);

        cgpaAvailability=false;
        loadAll();
    }


    //private methods
    private String getNumFileName(int semNum)
    {
        return ("s"+((Integer)semNum).toString()+"num.txt");
    }

    //---------------------------------------------------------------------------------------
    private String getCourseFileName(int semNum,int courseNum)
    {
        return ("s"+((Integer)semNum).toString()+"c"+((Integer)courseNum).toString()+".txt");
    }
    //---------------------------------------------------------------------------------------
    private boolean firstStart()
    {
        File s1num= new File(externalDirectory,"s1num.txt");
        return !s1num.exists();
    }
    //---------------------------------------------------------------------------------------
    private void createNewNumFiles()
    {
        File tempFile;
        FileWriter zeroWriter;

        for (int i=0; i<NUM_SEMESTERS; i++)
        {
            tempFile=new File(externalDirectory,getNumFileName(i+1));

            try
            {
                zeroWriter=new FileWriter(tempFile);
                zeroWriter.write("0");
                zeroWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }



        }
    }
    //---------------------------------------------------------------------------------------
    private void setSemestersAccessible(int start, int end, boolean access)

    {
        for(int i=start; i<=end; i++)
        {
            semesters[i-1].setAccessible(access);
        }
    }
    //---------------------------------------------------------------------------------------

    private void calcCgpa()
    {
        int sum=0;
        float sumProd=0;

        //if 1st semester is unavailable, CGPA is unavailable
        if(!isSemesterAvailable(1))
        {
            cgpaAvailability = false;
            return;
        }
        //if 1st semester is available, CGPA is available
        if(isSemesterAvailable(1))
            cgpaAvailability=true;


        for(int semNum=1; semNum<=NUM_SEMESTERS; semNum++)
        {
            if(!isSemesterAvailable(semNum))
                break;


            //now to calculate CGPA
            for(int courseNum=1 ; courseNum<=getNumCourses(semNum) ; courseNum++)
            {
                sum += getCourseCredHrs(semNum,courseNum);
                sumProd+= (getCourseCredHrs(semNum,courseNum)*getCourseGpa(semNum,courseNum));
            }


        }
        cgpa=sumProd/sum;

    }
    //---------------------------------------------------------------------------------------


    //public methods
    public void loadAll()
    {
        //if file system does not exist create it and set everything unavailable
        if(firstStart())
        {
            createNewNumFiles();
            semesters[0].setAvailable(false);
            setSemestersAccessible(2,NUM_SEMESTERS,false);
            return;
        }

        //now loading all courses into respective semesters
        File tempFile;
        Scanner tempScan;
        int tempNumCourses=0;
        Course tempCourse;


        for(int i=0; i<NUM_SEMESTERS; i++)
        {
            //getting number of courses in semester i+1
            tempFile=new File(externalDirectory,getNumFileName(i+1));

            try
            {
                tempScan=new Scanner(tempFile);
                tempNumCourses=tempScan.nextInt();
                tempScan.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            //if numCourses comes out to be zero, set this semester unavailable and the succeeding inaccessible
            if(tempNumCourses==0)
            {
                semesters[i].setAvailable(false);
                setSemestersAccessible(i+2,NUM_SEMESTERS,false);
                calcCgpa();
                return;
            }

            //now loading courses of semester i+1

            for(int j=1; j<= tempNumCourses; j++)
            {
                tempFile=new File(externalDirectory,getCourseFileName(i+1,j));
                tempCourse=new Course();
                try
                {
                    tempScan=new Scanner(tempFile);

                    tempCourse.setCourseTitle(tempScan.nextLine());
                    tempCourse.setCredHrs(tempScan.nextInt());
                    tempCourse.setCourseGpa(tempScan.nextFloat());

                    tempScan.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                semesters[i].addCourse(tempCourse);


            }//end for

        }//end for

        calcCgpa();


    }//end method
    //---------------------------------------------------------------------------------------


    public void saveSemester(int semNum, int prevAction)
    {
        /*Let N be the old number of courses before the need for saving presented itself.
         * If a course was removed, the Nth file will not be overwritten
         * and rather remain untouched. So Nth file must be removed. However,
         * if a course has been added, all files will be overwritten and
         * and any missing files will be recreated. Thus N+1 course files will
         * become available.*/

        //temporary local variables:
        File tempFile;
        FileWriter tempWriter;
        int N_old;
        int N_new=getNumCourses(semNum);

        if(prevAction==COURSE_REMOVED)
        {
            N_old=N_new+1;
            //deleting Nth file as stated
            tempFile=new File(externalDirectory,getCourseFileName(semNum,N_old));
            tempFile.delete();

        }



        //now rewriting the s?num.txt file
        tempFile=new File(externalDirectory,getNumFileName(semNum));
        try
        {
            tempWriter=new FileWriter(tempFile);
            tempWriter.write(((Integer)N_new).toString());
            tempWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        //now rewriting semester files
        for(int i=1;i<=N_new;i++)
        {
            tempFile=new File(externalDirectory,getCourseFileName(semNum,i));
            try
            {
                tempWriter=new FileWriter(tempFile);
                tempWriter.write(getCourseTitle(semNum,i)+"\n");
                tempWriter.write(((Integer)getCourseCredHrs(semNum,i)).toString()+"\n");
                tempWriter.write(((Float)getCourseGpa(semNum,i)).toString()+"\n");
                tempWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

    }
    //---------------------------------------------------------------------------------------

    public int getNumCourses(int semNum)
    {
        return semesters[semNum-1].getNumCourses();
    }
    //---------------------------------------------------------------------------------------

    public Semester getSemester(int semNum)
    {
        return semesters[semNum-1];
    }
    //---------------------------------------------------------------------------------------

    public String getCourseTitle(int semNum, int courseNum)
    {
        return semesters[semNum-1].getCourse(courseNum).getCourseTitle();
    }
    //---------------------------------------------------------------------------------------

    public int getCourseCredHrs(int semNum, int courseNum)
    {
        return semesters[semNum-1].getCourse(courseNum).getCredHrs();
    }
    //---------------------------------------------------------------------------------------

    public float getCourseGpa(int semNum, int courseNum)
    {
        return semesters[semNum-1].getCourse(courseNum).getCourseGpa();
    }
    //---------------------------------------------------------------------------------------
    public String getCourseGpaStr(int semNum, int courseNum)
    {
        return String.format(Locale.US,"%1.2f",semesters[semNum-1].getCourse(courseNum).getCourseGpa());

    }
    //---------------------------------------------------------------------------------------

    public void addCourse(int semNum, Course newCourse)
    {
        semesters[semNum-1].addCourse(newCourse);
        saveSemester(semNum,COURSE_ADDED);


        /*If the added course is the first for the semester then it's availability
         * must be set to true and accessibility of the next to be set to true
         * (unless the current semester is 8th then their is no next)*/
        if(!(semesters[semNum-1].isAvailable()))
        {
            semesters[semNum-1].setAvailable(true);
            if(semNum <8)
                semesters[semNum].setAccessible(true);

        }

        calcCgpa();
    }
    //---------------------------------------------------------------------------------------
    public void removeCourse(int semNum, int courseNum)
    {
        semesters[semNum-1].removeCourse(courseNum);
        saveSemester(semNum,COURSE_REMOVED);

        /*If the removed course is the last for the semester then it's availability
         * must be set to false and accessibility of the next to be set to false
         * (the situation that the next may contain courses will be handled in the
         * SemesterDetailsActivity)*/

        if(semesters[semNum-1].getNumCourses()==0)
        {
            semesters[semNum-1].setAvailable(false);
            if(semNum <8)
                semesters[semNum].setAccessible(false);
        }
        calcCgpa();
    }
    //---------------------------------------------------------------------------------------
    public boolean isSemesterAvailable(int semNum)
    {
        return semesters[semNum-1].isAvailable();
    }
    //---------------------------------------------------------------------------------------
    public boolean isSemesterAccessible(int semNum)
    {
        return semesters[semNum-1].isAccessible();
    }
    //---------------------------------------------------------------------------------------
    public void editCourse(int semNum, int courseNum, Course newCourse)
    {
        semesters[semNum-1].overWriteCourse(courseNum,newCourse);

        File tempFile=new File(externalDirectory,getCourseFileName(semNum,courseNum));

        try
        {
            FileWriter tempWriter=new FileWriter(tempFile);
            tempWriter.write(getCourseTitle(semNum,courseNum)+"\n");
            tempWriter.write(((Integer)getCourseCredHrs(semNum,courseNum)).toString()+"\n");
            tempWriter.write(((Float)getCourseGpa(semNum,courseNum)).toString()+"\n");
            tempWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        calcCgpa();
    }
    //---------------------------------------------------------------------------------------


    public float getCgpa()
    {
        return cgpa;
    }
    //---------------------------------------------------------------------------------------

    public float getSemesterGpa(int numSem)
    {
        return semesters[numSem-1].getSGPA();
    }
    //---------------------------------------------------------------------------------------
    public boolean isCgpaAvailable()
    {
        return  cgpaAvailability;
    }
    //---------------------------------------------------------------------------------------
    public String getCgpaStr()
    {
        if(isCgpaAvailable())
            return String.format(Locale.US,"%1.2f",cgpa);
        else
            return "N/A.";

    }
    //---------------------------------------------------------------------------------------

    public String getSemesterGpaStr(int numSem)
    {
        if(isSemesterAvailable(numSem))
            return String.format(Locale.US,"%1.2f",semesters[numSem-1].getSGPA());
        else
            return "N/A.";

    }
    //---------------------------------------------------------------------------------------

}

