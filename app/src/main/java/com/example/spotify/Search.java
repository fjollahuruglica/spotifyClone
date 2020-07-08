package com.example.spotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Search {

    @SerializedName("albums")
    @Expose
    private Albums albums;
    @SerializedName("artists")
    @Expose
    private Artist artists;
    @SerializedName("tracks")
    @Expose
    private Track tracks;
    private int viewType;

    public Search(Albums albums){
        this.albums=albums;
        viewType=0;
    }
    public Search(Artist artists){
        this.artists=artists;
        viewType=1;
    }
    public Search(Track tracks){
        this.tracks=tracks;
        viewType=2;
    }
    public Albums getAlbums() {
        return albums;
    }

    public void setAlbums(Albums albums) {
        this.albums = albums;
    }

    public Artist getArtists() {
        return artists;
    }

    public void setArtists(Artist artists) {
        this.artists = artists;
    }

    public Track getTracks() {
        return tracks;
    }

    public void setTracks(Track tracks) {
        this.tracks = tracks;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}