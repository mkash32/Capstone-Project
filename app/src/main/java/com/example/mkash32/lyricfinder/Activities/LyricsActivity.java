package com.example.mkash32.lyricfinder.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.example.mkash32.lyricfinder.Adapters.LyricsRecyclerAdapter;
import com.example.mkash32.lyricfinder.R;
import com.example.mkash32.lyricfinder.Services.ApiIntentService;
import com.example.mkash32.lyricfinder.Services.DataIntentService;
import com.example.mkash32.lyricfinder.Song;
import com.squareup.picasso.Picasso;

public class LyricsActivity extends AppCompatActivity {
    private Song song;
    private ImageView imageView;
    private String title, artist, url;
    private LyricsRecyclerAdapter adapter;
    private LyricsReceiver receiver;
    private Context context = this;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        title = getIntent().getStringExtra("title");
        artist = getIntent().getStringExtra("artist");
        url = getIntent().getStringExtra("url");

        // Register Receiver
        IntentFilter filter = new IntentFilter(LyricsReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new LyricsReceiver();
        registerReceiver(receiver, filter);

        // Start intent service to get lyrics
        Intent i = new Intent(this, ApiIntentService.class);
        i.setAction(ApiIntentService.ACTION_LYRICS);
        i.putExtra(ApiIntentService.EXT_TITLE, title);
        i.putExtra(ApiIntentService.EXT_ARTIST, artist);
        startService(i);

        this.setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.image_parallax);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LyricsRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        Picasso.with(this).load(url).into(imageView);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class LyricsReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.example.mkash32.lyricsfinder.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LyricsActivity", "Received lyrics");
            String lyrics = intent.getStringExtra(ApiIntentService.RESPONSE_LYRICS);
            adapter.setLyrics(lyrics);

            // Start intent service to save this song as recent into the song table
            boolean shouldInsert = intent.getBooleanExtra(ApiIntentService.RESPONSE_SHOULD_INSERT, false);
            if(shouldInsert) {
                Intent i = new Intent(activity, DataIntentService.class);
                i.setAction(DataIntentService.ACTION_INSERT);
                i.putExtra(DataIntentService.EXT_TITLE, title);
                i.putExtra(DataIntentService.EXT_ARTIST, artist);
                i.putExtra(DataIntentService.EXT_IMAGE, url);
                i.putExtra(DataIntentService.EXT_LYRICS, lyrics);
                i.putExtra(DataIntentService.EXT_RECENT, 1);
                startService(i);
            }
        }


    }

}
