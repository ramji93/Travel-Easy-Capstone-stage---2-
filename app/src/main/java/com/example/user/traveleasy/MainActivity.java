package com.example.user.traveleasy;

import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Window;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

            Explode s = new Explode();
            s.setMode(Explode.MODE_OUT);
            //s.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in));
            s.setDuration(400);
            s.excludeTarget(android.R.id.statusBarBackground, true);
            s.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setExitTransition(s);

        }

        setContentView(R.layout.activity_main);





    }


}

