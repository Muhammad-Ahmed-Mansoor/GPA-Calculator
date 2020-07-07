package com.mansoor.gpacalculator;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SemesterDetailActivity extends AppCompatActivity {

    public static final String MODE="com.mansoor.gpa.MODE";
    public static final String CURRENT_SEMESTER="com.mansoor.gpa.CURRENT_SEMESTER";
    public static final String COURSE_TO_EDIT="com.mansoor.gpa.COURSE_TO_EDIT";

    private int currentSemester;
    private TextView gpaView;
    private LinearLayout courseLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        //set current semester
        currentSemester=getIntent().getIntExtra(MainActivity.SEMESTER_SELECTED,0);

        setTheme(getSgpaTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_detail);



        //set Action bar
        prepActionBar();

        //set the bottom text View
        prepGpaView();

        //fill the scrollView with courses
        fillLinearLayout();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        recreate();
    }




    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract(pure = true)
    private String getOrdinal(int cardinal)
    {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (cardinal % 100) {
            case 11:
            case 12:
            case 13:
                return cardinal + "th";
            default:
                return cardinal + sufixes[cardinal % 10];

        }


    }


    private void prepActionBar()
    {
        setSupportActionBar((Toolbar)findViewById(R.id.semesterDetailsActionBar));
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(getString(R.string.semester_detail_screen_title,getOrdinal(currentSemester)));
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    private void prepGpaView()
    {
        gpaView= (TextView) findViewById(R.id.semesterDetailGpaView);
        String gpaString=getString(R.string.sgpa_view_text)+MainActivity.mainProfile.getSemesterGpaStr(currentSemester)+"       "+
                getString(R.string.cgpa_view_text)+MainActivity.mainProfile.getCgpaStr();

        SpannableString gpaSpanString=new SpannableString(gpaString);

        gpaSpanString.setSpan(new RelativeSizeSpan(2),5,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        gpaSpanString.setSpan(new RelativeSizeSpan(2),21,25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        gpaView.setText(gpaSpanString);


    }


    private void fillLinearLayout()
    {


        courseLayout=(LinearLayout)findViewById(R.id.courseLinearLayout);

        if(!MainActivity.mainProfile.isSemesterAvailable(currentSemester))
        {
            addCourseNotAvailableMessage();
            return;
        }

        CourseView temp;
        for(int i=1;i<=MainActivity.mainProfile.getNumCourses(currentSemester);i++)
        {
            temp=new CourseView(getApplicationContext(),currentSemester,i,this);
            courseLayout.addView(temp,i-1);
        }
        //adding an empty textView at the end so that FAB does not block last course
        addBlankCourse();
    }

    public void deleteAndRefresh(int courseRemoved)
    {
        int prevTheme=getSgpaTheme();

        //removing the course from profile
        MainActivity.mainProfile.removeCourse(currentSemester,courseRemoved);
        //removing the course from list
        courseLayout.removeViewAt(courseRemoved-1);
        //refreshing the GPA Viewer
        prepGpaView();

        //if no course is available showing the message No course Avaialable
        if(!MainActivity.mainProfile.isSemesterAvailable(currentSemester))
        {
            addCourseNotAvailableMessage();
            recreateWithDelay();
            return;
        }

        //updating the course numbers in all the courseViews
        for(int i=1;i<=MainActivity.mainProfile.getNumCourses(currentSemester);i++)
        {
            ((CourseView)courseLayout.getChildAt(i-1)).setCourseNum(i);
        }

        if(getSgpaTheme() != prevTheme)
        {
            recreateWithDelay();
        }



    }

    public void addCourse(View view)
    {
        Intent addCourseIntent=new Intent(this,AddOrEditCourse.class);
        addCourseIntent.putExtra(MODE,AddOrEditCourse.ADD_MODE);
        addCourseIntent.putExtra(CURRENT_SEMESTER,currentSemester);
        startActivity(addCourseIntent);
    }

    public void editCourse(int courseToEdit)
    {
        Intent editCourseIntent=new Intent(this,AddOrEditCourse.class);
        editCourseIntent.putExtra(MODE,AddOrEditCourse.EDIT_MODE);
        editCourseIntent.putExtra(CURRENT_SEMESTER,currentSemester);
        editCourseIntent.putExtra(COURSE_TO_EDIT,courseToEdit);
        startActivity(editCourseIntent);
    }


    public void addCourseNotAvailableMessage()
    {
        TextView noCourse=new TextView(getApplicationContext());
        noCourse.setTextColor(getResources().getColor(R.color.Gray));
        LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        noCourse.setGravity(Gravity.CENTER);
        noCourse.setText(getResources().getString(R.string.no_course_available));
        noCourse.setLayoutParams(param);
        courseLayout.addView(noCourse,0);
    }

    public void addBlankCourse()
    {
        TextView noCourse=new TextView(getApplicationContext());
        LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        noCourse.setText(null);
        noCourse.setLayoutParams(param);
        courseLayout.addView(noCourse);
    }

    private int getSgpaTheme()
    {
        int index=0;
        if(MainActivity.mainProfile.isSemesterAvailable(currentSemester))
        {
            float sgpa = MainActivity.mainProfile.getSemesterGpa(currentSemester);

            if (sgpa >= 3.5)
                index = 0;
            else if (sgpa >= 3)
                index = 1;
            else if (sgpa >= 2.5)
                index = 2;
            else if (sgpa >= 2)
                index=3;
            else
                index=4;
        }

        else
        {
            index=5;
        }

        return getResources().getIdentifier(MainActivity.themes[index],"style",getPackageName());
    }

    private void recreateWithDelay()
    {

        Handler mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
               recreate();
            }
        },500);
    }



}
