package com.example.mkash32.lyricfinder.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mkash32.lyricfinder.Services.ApiIntentService;
import com.example.mkash32.lyricfinder.Services.DataIntentService;
import com.example.mkash32.lyricfinder.Constants;
import com.example.mkash32.lyricfinder.R;

public class SearchActivity extends AppCompatActivity {

    private EditText searchText;
    private Button searchButton;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchButton = (Button) findViewById(R.id.button_search);
        searchText = (EditText) findViewById(R.id.searchText);

        // Initialize by clearing search table
        Intent clear = new Intent(activity, DataIntentService.class);
        clear.setAction(DataIntentService.ACTION_DELETE);
        activity.startService(clear);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear before each search
                Intent clear = new Intent(activity, DataIntentService.class);
                clear.setAction(DataIntentService.ACTION_DELETE);
                activity.startService(clear);

                // Perform search
                String text = searchText.getText().toString();
                String url = Constants.getSearchURL(text);
                Intent i = new Intent(activity, ApiIntentService.class);
                i.setAction(ApiIntentService.ACTION_SEARCH);
                i.putExtra("url", url);
                activity.startService(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear before exiting
        Intent clear = new Intent(this, DataIntentService.class);
        clear.setAction(DataIntentService.ACTION_DELETE);
        startService(clear);
    }
}
