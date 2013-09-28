package com.imran.customview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity {


    StopWatchView mTimerView;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().setDisplayShowTitleEnabled(false);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        mTimerView = (StopWatchView) findViewById(R.id.view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.buttonPlay:
                mTimerView.startAnimation();
                break;
            case R.id.buttonPause:
                break;
            case R.id.buttonRefresh:
                break;
        }

    }


}
