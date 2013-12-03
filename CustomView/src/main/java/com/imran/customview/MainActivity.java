package com.imran.customview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

    StopWatchView mTimerView;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimerView = (StopWatchView) findViewById(R.id.view);
        ((TextView) findViewById(R.id.text_view_stop_watch)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button_start_pause:

                if (!mTimerView.isRunning()) {
                    ((ImageButton) findViewById(R.id.button_start_pause)).setImageResource(android.R.drawable.ic_media_pause);
                    mTimerView.start();
                } else {
                    ((ImageButton) findViewById(R.id.button_start_pause)).setImageResource(android.R.drawable.ic_media_play);
                    mTimerView.pause();
                }
                break;
            case R.id.buttonRefresh:
                ((ImageButton) findViewById(R.id.button_start_pause)).setImageResource(android.R.drawable.ic_media_play);
                mTimerView.reset();
                break;
        }
    }
}
