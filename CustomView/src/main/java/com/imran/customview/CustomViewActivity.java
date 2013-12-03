package com.imran.customview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class CustomViewActivity extends Activity implements CustomView.OnColorChangeListener{

    CustomView mCustomView;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        mCustomView = (CustomView) findViewById(R.id.custom);
        mCustomView.setOnColorChangedListener(this);

        mTextView = (TextView) findViewById(R.id.text_view);
        mTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.custom_view, menu);
        return true;
    }

    @Override
    public void onColorChanged(String color) {
        mTextView.setText("Background color set to " + color);
    }
}
