package com.imran.customview;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChooserActivity extends Activity {

    ListView mListView;
    private final String VIEW1 = "Custom View";
    private final String VIEW2 = "Custom Stop Watch";

    String[] viewTexts = {VIEW1, VIEW2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        mListView = (ListView) findViewById(R.id.chooser_listview);
        mListView.setAdapter(new ChooserAdapter());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch ((int)id){
                    case 0:
                        intent = new Intent(getBaseContext(), CustomViewActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chooser, menu);
        return true;
    }

    private class ChooserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return viewTexts.length;
        }

        @Override
        public Object getItem(int position) {
            return viewTexts[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView textView = new TextView(getBaseContext());
            textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf"));
            textView.setTextSize(30);
            textView.setPadding(10, 10, 10, 10);
            textView.setTextColor(Color.BLACK);
            textView.setText((String)getItem(position));

            return textView;
        }
    }
    
}
