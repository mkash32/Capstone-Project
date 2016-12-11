package com.example.mkash32.lyricfinder;

/**
 * Created by Aakash on 11/12/16.
 */

public class Song {
    private String title, artist, lyrics, imageUrl;
    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public Song() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
