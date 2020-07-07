package com.mansoor.gpacalculator;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class AddOrEditCourse extends AppCompatActivity
{
    public static final int EDIT_MODE=0;
    public static final int ADD_MODE=1;
    private final String[] modes={"Edit","Add"};
    private int currentMode;
    private int current_semester;
    private int courseToEdit;
    private EditText courseTitleEdit, courseGpaEdit,credHrsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.BlueTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_course);

        currentMode=(int)getIntent().getIntExtra(SemesterDetailActivity.MODE,0);
        current_semester = (int) getIntent().getIntExtra(SemesterDetailActivity.CURRENT_SEMESTER, 0);

        prepActionBar();

        //All other views prepped in XML. Now fetching them
        courseTitleEdit=findViewById(R.id.course_title);
        courseGpaEdit=findViewById(R.id.course_gpa);
        credHrsEdit=findViewById(R.id.course_cred_hrs);

        if(currentMode==EDIT_MODE)

        {
            courseToEdit = (int) getIntent().getIntExtra(SemesterDetailActivity.COURSE_TO_EDIT, 0);
            courseTitleEdit.setText(MainActivity.mainProfile.getCourseTitle(current_semester,courseToEdit));
            courseGpaEdit.setText(MainActivity.mainProfile.getCourseGpaStr(current_semester,courseToEdit));
            credHrsEdit.setText(((Integer)MainActivity.mainProfile.getCourseCredHrs(current_semester,courseToEdit)).toString());
        }



    }


    private void prepActionBar()
    {

        setSupportActionBar((Toolbar)findViewById(R.id.AddOrEditActionBar));
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(getString(R.string.add_or_edit_title,modes[currentMode]));
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    public void save(View view)
    {
        String gTitle,gCredHrs,gGpa;
        int gCredHrsValue=0;
        float gGpaValue=0;
        boolean correctCredHrsInput=true;
        boolean correctGpaInput=true;

        gTitle=courseTitleEdit.getText().toString();
        gCredHrs=credHrsEdit.getText().toString();
        gGpa=courseGpaEdit.getText().toString();

        //----------------------------
        //starting input verification

        //verifying correct credHrs Input
        try
        {
            gCredHrsValue = Integer.parseInt(gCredHrs);
            if(gCredHrsValue<=0)
                correctCredHrsInput=false;
        }
        catch (NumberFormatException e)
        {
            correctCredHrsInput=false;

        }
        //verifying correct courseGpa Input
        try
        {
            gGpaValue=Float.parseFloat(gGpa);

            if(gGpaValue<=0 || gGpaValue>4)
                correctGpaInput=false;

        }
        catch (NumberFormatException e)
        {
            correctGpaInput=false;
        }

        //handling the incorrect input if discovered
        if(!correctCredHrsInput || !correctGpaInput || gTitle.length()<1)
        {
            if(!correctCredHrsInput)
            {
                credHrsEdit.setText(null);
                credHrsEdit.setHint(getResources().getString(R.string.input_error));
            }

            if(!correctGpaInput)
            {
                courseGpaEdit.setText(null);
                courseGpaEdit.setHint(getResources().getString(R.string.input_error));
            }

            if(gTitle.length()<1)
            {
                courseTitleEdit.setHint(getResources().getString(R.string.input_error));
            }

            return;
        }
        //end of input verification

        //now creating a new course object
        Course tempCourse=new Course(gTitle,gCredHrsValue,gGpaValue);

        if(currentMode==EDIT_MODE)
        {
            MainActivity.mainProfile.editCourse(current_semester,courseToEdit,tempCourse);
        }
        if(currentMode==ADD_MODE)
        {
            MainActivity.mainProfile.addCourse(current_semester,tempCourse);
        }


        finish();
    }

}
