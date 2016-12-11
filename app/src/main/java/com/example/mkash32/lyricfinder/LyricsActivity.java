package com.example.mkash32.lyricfinder;

import android.content.Context;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class LyricsActivity extends AppCompatActivity {
    private Song song;
    private ImageView image;
    private String id;
    private LyricsRecyclerAdapter adapter;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");

        image = (ImageView) findViewById(R.id.image_parallax);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LyricsRecyclerAdapter(new Song(), this);
        recyclerView.setAdapter(adapter);

        Picasso.with(this).load(getIntent().getStringExtra("url")).into(image);
    }

}
