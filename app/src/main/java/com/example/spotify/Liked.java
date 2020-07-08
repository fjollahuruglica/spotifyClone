package com.example.spotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Liked {

    private String id;
    private String name;
    private String artist;
    private String url;

    public Liked(String id, String name, String artist, String url){
        this.id=id;
        this.name=name;
        this.artist=artist;
        this.url=url;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}