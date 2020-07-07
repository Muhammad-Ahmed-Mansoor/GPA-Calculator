package com.mansoor.gpacalculator;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class CourseView extends LinearLayout

{
    private int courseNum;
    private TextView textView;
    private ImageButton deleteButton;
    private ImageButton editButton;
    private Context context;
    private int currentSemester;
    private final int iconWidth= 150;
    private final int iconHeight= 150;
    private SemesterDetailActivity parentActivity;
    private String[] colors={"green","turquoise","orange","red","Black"};

    public CourseView(Context gContext,int semNum,int gCourseNum,SemesterDetailActivity parent)

    {
        super(gContext);
        context=gContext;
        courseNum=gCourseNum;
        currentSemester=semNum;
        parentActivity=parent;

        setOrientation(LinearLayout.HORIZONTAL);

        setUpTextView();
        setUpEditButton();
        setUpDeleteButton();

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //for borders
        this.setBackground(getResources().getDrawable(R.drawable.bottomborders));
        setLayoutParams(params);

    }



    private void setUpTextView()
    {
        //setting up strings
        String courseTitle=MainActivity.mainProfile.getCourseTitle(currentSemester,courseNum);
        String credHrs=((Integer)(MainActivity.mainProfile.getCourseCredHrs(currentSemester,courseNum))).toString();
        String courseGpa=String.format(Locale.US,"%1.1f",MainActivity.mainProfile.getCourseGpa(currentSemester,courseNum));;
        String bullet="\u25CF    ";//4 spaces
        String full=bullet+courseTitle+"\n        Credit Hours: "+credHrs+"\n        Grade: "+courseGpa;


        //setting up spannable strings
        SpannableString spanString=new SpannableString(full);
        spanString.setSpan(new RelativeSizeSpan(0.8f),bullet.length()+courseTitle.length()
                ,spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(getCourseGpaColor())), 0
                , 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Gray)), bullet.length() + courseTitle.length()
               , spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        //setting up the layout params
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,6f);



        //setting up the textView
        textView=new TextView(context);
        textView.setText(spanString);
        textView.setLayoutParams(params);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        textView.setTextColor(getResources().getColor(R.color.Black));
        textView.setPadding(8,8,8,8);
        this.addView(textView);

    }

    private void setUpDeleteButton()
    {
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(iconWidth,iconHeight,1f);
        deleteButton=new ImageButton(context);
        deleteButton.setLayoutParams(params);
        deleteButton.setBackground(getResources().getDrawable(R.drawable.delete));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(currentSemester !=8)
                {
                    if (MainActivity.mainProfile.getNumCourses(currentSemester) == 1 && MainActivity.mainProfile.isSemesterAvailable(currentSemester + 1)) {
                        Toast.makeText(parentActivity.getApplicationContext(), R.string.cant_del_course_string, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        parentActivity.deleteAndRefresh(courseNum);
                    }
                }
                else
                {
                    parentActivity.deleteAndRefresh(courseNum);
                }

            }
        });


        this.addView(deleteButton);

    }

    private void setUpEditButton()
    {
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(iconWidth,iconHeight,1f);
        editButton=new ImageButton(context);
        editButton.setLayoutParams(params);
        editButton.setBackground(getResources().getDrawable(R.drawable.edit));

        editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)

            {
                parentActivity.editCourse(courseNum);
            }
        });
        this.addView(editButton);

    }

    public void setCourseNum(int num)
    {
        courseNum=num;
    }

    private int getCourseGpaColor()
    {
        int index;

        float gpa = MainActivity.mainProfile.getCourseGpa(currentSemester,courseNum);

        if (gpa >= 3.5)
            index = 0;
        else if (gpa >= 3)
            index = 1;
        else if (gpa >= 2.5)
            index = 2;
        else if (gpa >= 2)
            index=3;
        else
            index=4;




        return getResources().getIdentifier(colors[index],"color",parentActivity.getPackageName());
    }



}
