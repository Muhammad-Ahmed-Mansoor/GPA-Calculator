package com.mansoor.gpacalculator;

import android.animation.LayoutTransition;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.support.design.widget.FloatingActionButton;
import android.support.constraint.ConstraintLayout;

public class Surprise extends AppCompatActivity {

    private final int NUM_IMAGES=12;
    private final String IMAGE_NAME="test";
    private int currentImageNum=1;
    private ImageView imageView;
    private FloatingActionButton leftButton;
    private ConstraintLayout mainLayout;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise);

        imageView=findViewById(R.id.imageView);
        leftButton=findViewById(R.id.leftButton);
        mainLayout=findViewById(R.id.mainLayout);
        animationDrawable=(AnimationDrawable)getResources().getDrawable(R.drawable.test1);

        LayoutTransition changeTransitions=mainLayout.getLayoutTransition();
        changeTransitions.setDuration(1000);



        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //It's the start, so set the first drawable as the
        imageView.setImageDrawable(getImage(currentImageNum));
        Drawable tempAnimation = imageView.getDrawable();
        if (tempAnimation instanceof Animatable) {
            ((Animatable)tempAnimation).start();
        }


    }


    @Override
    public void onRestart()
    {
        super.onRestart();
        finishAndRemoveTask();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    private Drawable getImage(int num)
    {
        if(currentImageNum==1)
            return animationDrawable;
        else
            return getResources().getDrawable(getResources().getIdentifier(IMAGE_NAME+((Integer)num).toString(),"drawable",getPackageName()));
    }


    public void onRightClick(View view)
    {
        currentImageNum=currentImageNum%NUM_IMAGES;
        currentImageNum++;
        changeImage();


        Drawable tempAnimation = imageView.getDrawable();
        if (tempAnimation instanceof Animatable) {
            ((Animatable)tempAnimation).start();
        }
    }

    public void onLeftClick(View view)
    {

        if(currentImageNum==1)
        {
            currentImageNum=NUM_IMAGES;
        }
        else
        {
            currentImageNum--;
        }

        changeImage();





        Drawable tempAnimation = imageView.getDrawable();
        if (tempAnimation instanceof Animatable) {
            ((Animatable)tempAnimation).start();
        }
    }

    public void onCloseButton(View view)
    {
        finishAndRemoveTask();
    }

    public void changeImage()
    {
        mainLayout.removeView(imageView);
        imageView.setImageDrawable(getImage(currentImageNum));
        mainLayout.addView(imageView);

    }


}
