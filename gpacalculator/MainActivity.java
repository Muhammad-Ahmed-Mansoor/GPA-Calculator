//package name
package com.mansoor.gpacalculator;

//imports
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
{

    //an array of theme names used for this and the semester detail activities
    public static String[] themes={"GreenTheme",//gpa 3.5 and above
            "TurquoiseTheme",//gpa 3 and above
            "OrangeTheme",//gpa 2.5 and above
            "RedTheme",//gpa 2 and above
            "BlackTheme",//below 2
            "BlueTheme"};//unavailable

    //key for an extra of the intent that launches the next activty. Contains semester number selected by the user
    public static final String SEMESTER_SELECTED="com.mansoor.gpacalculator.SEMESTER_SELECTED";

    /*
    The Only instance of the Profile class. Used throughout the app so declared static.
    Allows access to all the courses and related information
    */
    public static Profile mainProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /* initializing mainProfile with the app's default external directory. Once the main profile
        * is set up, the information is used to set the appropriate theme. Note: Theme MUST be set before the call
        * to setContentView()*/
        mainProfile=new Profile(getExternalFilesDir(null));
        setTheme(getCgpaTheme());

        //Necessary usual stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the action bar
        setSupportActionBar(((Toolbar) findViewById(R.id.semesterSelectActionBar)));

        //now to initialize and set properties of the semesterButtons[]
        setUpButtons();

        //setting the textView to display the CGPA at the bottom of the screen
        setUpCgpaView();


    }//------------------------------------------------------------------------------------------

    /*Overriding onRestart() and adding recreate() to it so that the theme can update with the current
    * cgpa at each entry point of the activity*/
    @Override
    public void onRestart()
    {

        super.onRestart();
        recreate();

    }//------------------------------------------------------------------------------------------


    /*The dimensions of the 8 semester buttons are defined in xml Layout. This method puts the SGPA as the text and the appropriate
    * drawable as background, as well as decides which button should be disables based on inaccessibilty of the semester*/
    private void setUpButtons()
    {
        Button[] semesterButtons=new Button[8];

        for(int i=1; i<=Profile.NUM_SEMESTERS; i++)
        {
            //fetch from xml Layout
            semesterButtons[i-1]=findViewById(getResources().getIdentifier("semester_button"+((Integer)i).toString(),"id",getPackageName()));

            //set the semester number as a tag to be used by the onclick() to identify which semester is chosen
            semesterButtons[i-1].setTag(i);

            //set sgpa as text
            semesterButtons[i-1].setText(getString(R.string.semester_button_text,mainProfile.getSemesterGpaStr(i)));

            //set the correct background drawable
            semesterButtons[i-1].setBackground(getSemesterButtonBackground(i));

            //decides which button should be disables based on inaccessibilty of the semester
            semesterButtons[i-1].setEnabled(mainProfile.isSemesterAccessible(i));

        }

    }//------------------------------------------------------------------------------------------

    //setting the textView to display the CGPA at the bottom of the screen
    private void setUpCgpaView()
    {
        //fetch the textView from xml
        TextView cgpaView = findViewById(R.id.semesterSelectCgpaView);

        //the cpa(both the literal title and the actual value) are stored in a spannable string
        SpannableString cgpaString=new SpannableString(getString(R.string.cgpa_view_text)+mainProfile.getCgpaStr());

        //the sgpa value (index 5 to 9) of cgpaString is given twice the size for emphasis and aesthetic purposes
        cgpaString.setSpan(new RelativeSizeSpan(2),5,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //set the string as the text
        cgpaView.setText(cgpaString);

    }//------------------------------------------------------------------------------------------

    //the onClick() for the semester buttons
    public void semesterButtonPressed(View buttonPressed)
    {
        /*An intent is prepared and semesterDetailActivity is called. The button's tag is sent as an extra to give
        * the child activity the semester# to give details of*/
        Intent semesterDetailsIntent=new Intent(this,SemesterDetailActivity.class);
        semesterDetailsIntent.putExtra(SEMESTER_SELECTED,(int)buttonPressed.getTag());
        startActivity(semesterDetailsIntent);
    }

    //------------------------------------------------------------------------------------------

    //Returns the appropriate theme based on the cgpa
    private int getCgpaTheme()
    {
     int index=0;                       // the index of themes[] array
     if(mainProfile.isCgpaAvailable())
     {
         float cgpa = mainProfile.getCgpa();

         if (cgpa >= 3.5)
             index = 0;
         else if (cgpa >= 3)
             index = 1;
         else if (cgpa >= 2.5)
             index = 2;
         else if (cgpa >= 2)
             index=3;
         else
             index=4;
     }
     else
     {
         index=5;
     }
     return getResources().getIdentifier(themes[index],"style",getPackageName());
    }
    //------------------------------------------------------------------------------------------

    private Drawable getSemesterButtonBackground(int semesterNumber)
    {
        String drawableName="g"+((Integer)getGpaGroupNum(semesterNumber)).toString()+"s"+((Integer)semesterNumber).toString();
          Drawable bgDrawable=getResources().getDrawable(getResources().getIdentifier(drawableName,"drawable",getPackageName()));
        //now to make an inset drawable
        InsetDrawable bgInsetDrawable=new InsetDrawable(bgDrawable,15);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            return new RippleDrawable(new ColorStateList(
                    new int[][]
                            {
                                    new int[]{}
                            },
                    new int[]
                            {
                                    ContextCompat.getColor(getApplicationContext(),R.color.white)
                            }
            ),bgInsetDrawable,null);
        }
        else
        {
            return bgInsetDrawable;
        }


    }//------------------------------------------------------------------------------------------

    private int getGpaGroupNum(int semesterNumber)
    {
        int grade;

        if(mainProfile.isSemesterAvailable(semesterNumber))
        {
            float sgpa=mainProfile.getSemesterGpa(semesterNumber);

            if(sgpa==4)
                grade=0;
            else if(sgpa>=3.8)
                grade=1;
            else if(sgpa>=3.5)
                grade=2;
            else if(sgpa>=3)
                grade=3;
            else if(sgpa>=2.5)
                grade=4;
            else if(sgpa>=2)
                grade=5;
            else
                grade=6;




        }
        else if(mainProfile.isSemesterAccessible(semesterNumber))
        {
            grade=7;
        }
        else
        {
            grade=8;
        }

        return grade;
    }//------------------------------------------------------------------------------------------






}



