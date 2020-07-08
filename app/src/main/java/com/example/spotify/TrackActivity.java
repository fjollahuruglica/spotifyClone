package com.example.spotify;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrackActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar seekBar;
    private TextView name, songName;
    private ImageView imageView, play, back, forward, pause, refresh, backIcon, like;
    private String id;
    private Handler seekHandler;
    private Runnable runnable;
    private boolean isLiked=true;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        imageView=findViewById(R.id.imageView);
        backIcon=findViewById(R.id.backIcon);
        name=findViewById(R.id.name);
        songName=findViewById(R.id.songName);
        seekBar=findViewById(R.id.seekBar);
        play=findViewById(R.id.play);
        back=findViewById(R.id.back);
        forward=findViewById(R.id.forward);
        pause=findViewById(R.id.pause);
        refresh=findViewById(R.id.reset);
        like=findViewById(R.id.like);
        list = new ArrayList<>();
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
            }
        });

        sqLiteDatabase = this.openOrCreateDatabase("likedSongsDB", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS songsLiked(id  VARCHAR(200), name VARCHAR(200), artist VARCHAR(200), url VARCHAR(200))");



        Intent intent=getIntent();

        id=intent.getStringExtra("id");

        getTrackDataFromServer();
    }

    private void getTrackDataFromServer(){

        final SpotifyClient client=Interface.getInterface();

        final Call<Track> trackCall= client.getTrack(id);
        final ProgressDialog progressDialog;
        progressDialog=new ProgressDialog(this);
        progressDialog.setMax(1000);
        progressDialog.show();
        trackCall.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, final Response<Track> response) {
                progressDialog.dismiss();
                Track tracks = response.body();
                songName.setText(tracks.getName());

                queryDB();
                saveData2();

                loadData2();

//                sqLiteDatabase.execSQL("DROP TABLE songsLiked");

                final Track track = response.body();
                    if (list.contains(id)) {
                        Log.i("record", "lalallala");
                        like.setImageResource(R.drawable.heart);
                        isLiked = true;
                        like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isLiked) {
                                    like.setImageResource(R.drawable.heart_outline);
                                    sqLiteDatabase.execSQL("DELETE FROM songsLiked WHERE id=" + DatabaseUtils.sqlEscapeString(id));
                                    isLiked = false;
                                    saveData2();
                                } else {
                                    isLiked = true;
                                    sqLiteDatabase.execSQL("INSERT OR REPLACE INTO songsLiked(id, name, artist, url) VALUES (" + DatabaseUtils.sqlEscapeString(id) + ", " + DatabaseUtils.sqlEscapeString(track.getName()) + ", " + DatabaseUtils.sqlEscapeString(track.getArtists().get(0).getName()) + "," + DatabaseUtils.sqlEscapeString(track.getArtists().get(0).getName()) + ")");
                                    like.setImageResource(R.drawable.heart);
                                }

                            }

                        });

                    } else {
                        isLiked = true;
                        like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!isLiked) {
                                    like.setImageResource(R.drawable.heart);
                                    sqLiteDatabase.execSQL("INSERT OR REPLACE INTO songsLiked(id, name, artist, url) VALUES (" + DatabaseUtils.sqlEscapeString(id) + ", " + DatabaseUtils.sqlEscapeString(track.getName()) + ", " + DatabaseUtils.sqlEscapeString(track.getArtists().get(0).getName()) + "," + DatabaseUtils.sqlEscapeString(track.getArtists().get(0).getName()) + ")");
                                    isLiked = true;
                                } else {
                                    sqLiteDatabase.execSQL("DELETE FROM songsLiked WHERE id=" + DatabaseUtils.sqlEscapeString(id));
                                    like.setImageResource(R.drawable.heart_outline);
                                    isLiked = false;
                                }

                            }
                        });
                        like.setImageResource(R.drawable.heart_outline);
                        isLiked = true;
                    }


                if (tracks.getArtists().size()>1){
                    name.setText(tracks.getArtists().get(0).getName()+", "+tracks.getArtists().get(1).getName());
                } else if (tracks.getArtists().size()==1){
                    name.setText(tracks.getArtists().get(0).getName());
                }else {
                    name.setText("Unknown Artist");
                }
                RequestOptions options = new RequestOptions()
                        .centerCrop();

                Glide.with(getApplicationContext()).load(tracks.getAlbum().getImages().get(0).getUrl()).apply(options).into(imageView);

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                if (tracks.getPreviewUrl()!=null) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(tracks.getPreviewUrl()));
                }
                else {
                    Toast.makeText(getApplicationContext(), "This song has no preview audio", Toast.LENGTH_LONG).show();
                    play.setEnabled(false);
                    pause.setEnabled(false);
                    forward.setEnabled(false);
                    back.setEnabled(false);
                    refresh.setEnabled(false);

                }
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        seekBar.setMax(mediaPlayer.getDuration());
//                        mediaPlayer.start();
                        changeSeekbar();
                    }
                });

                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            play.setVisibility(View.VISIBLE);
                            pause.setVisibility(View.GONE);
                        }
                    }
                });

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("hello", "im");
                        mediaPlayer.start();
                        play.setVisibility(View.GONE);
                        pause.setVisibility(View.VISIBLE);
                        changeSeekbar();

                    }
                });
                forward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                    }
                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                    }
                });

                refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                        play.setVisibility(View.GONE);
                        pause.setVisibility(View.VISIBLE);
                        changeSeekbar();
                    }
                });

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser){
                            mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
    }

    private void changeSeekbar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()){
            runnable= new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();
                }
            };
         seekHandler= new Handler();
         seekHandler.postDelayed(runnable, 1000);
        }
    }

    public void queryDB(){
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM songsLiked", null);
        int idIndex = c.getColumnIndex("id");
        int nameIndex = c.getColumnIndex("name");
        int artistIndex = c.getColumnIndex("artist");
        int urlIndex = c.getColumnIndex("url");

        c.moveToFirst();
        int count = c.getCount();
        while (count != 0){
            String idName= c.getString(idIndex);
            String name = c.getString(nameIndex);
            String artist = c.getString(artistIndex);
            String url = c.getString(urlIndex);
            list.add(idName);
            list.add(name);
            list.add(artist);
            list.add(url);
            c.moveToNext();
            count--;
        }

    }
    public void  saveData2(){
        SharedPreferences.Editor sharedPref = getSharedPreferences("hello", Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        sharedPref.putString("list", gson.toJson(list));
        sharedPref.apply();

    }
    public void loadData2(){
        SharedPreferences sharedPref = getSharedPreferences("hello",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonText = sharedPref.getString("list", null);
        Type listType = new TypeToken<List<String>>() {}.getType();
        list = gson.fromJson(jsonText,listType);
    }
}
