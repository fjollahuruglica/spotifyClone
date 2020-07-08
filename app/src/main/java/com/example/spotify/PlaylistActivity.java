package com.example.spotify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    private String id;
    private RecyclerView recyclerView;
    private SQLiteDatabase sqLiteDatabase;
    private PlaylistAdapter playlistAdapter;
    private LikedAdapter likedAdapter;
    private List<Item> itemArrayList=new ArrayList<>();
    private TextView followers, playlist;
    private ImageView backIcon, imageView, heart;
    private List<Liked> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        recyclerView=findViewById(R.id.playlistRecyclerView3);
        playlist=findViewById(R.id.playlist);
        followers=findViewById(R.id.followers);
        backIcon=findViewById(R.id.backIcon);
        imageView=findViewById(R.id.imageView);
        list = new ArrayList<>();

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sqLiteDatabase = this.openOrCreateDatabase("likedSongsDB", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS songsLiked(id  VARCHAR(200), name VARCHAR(200), artist VARCHAR(200), url VARCHAR(200))");


        Intent intent=getIntent();
        id = intent.getStringExtra("id");

        if (id!=null) {
            getPlaylistDataFromServer();
        }else {
            getLikedDataFromServer();
        }
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getPlaylistDataFromServer() {

        SpotifyClient client = Interface.getInterface();

        final Call<GetPlaylist> getPlaylistCall = client.getPlaylist(id, "US");

        getPlaylistCall.enqueue(new Callback<GetPlaylist>() {
            @Override
            public void onResponse(Call<GetPlaylist> call, final Response<GetPlaylist> response) {

                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getTracks().getItems());


                playlist.setText(response.body().getName());
                followers.setText(response.body().getOwner().getDisplayName());
                RequestOptions options = new RequestOptions()
                        .centerCrop();

                Glide.with(getApplicationContext()).load(response.body().getImages().get(0).getUrl()).apply(options).into(imageView);

                Type listType = new TypeToken<List<Item>>() {}.getType();
                itemArrayList = new Gson().fromJson(json, listType);
                playlistAdapter = new PlaylistAdapter(itemArrayList, getApplicationContext());

                recyclerView.setAdapter(playlistAdapter);

                playlistAdapter.setOnItemClickListener(new PlaylistAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getApplication(), TrackActivity.class);
                        intent.putExtra("id", response.body().getTracks().getItems().get(position).getTrack().getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });

                Log.i("responseee", MainActivity.Authcode + " okokko");


            }

            @Override
            public void onFailure(Call<GetPlaylist> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
    }

    private void getLikedDataFromServer() {

        SpotifyClient client = Interface.getInterface();

                playlist.setText("Liked Songs");
                followers.setText("Liked by me");
                RequestOptions options = new RequestOptions()
                        .centerCrop();

                Glide.with(getApplicationContext()).load(R.drawable.liked).apply(options).into(imageView);
                Gson gson = new Gson();
                queryDB();

                String jsonText = gson.toJson(list);
                Type listType2 = new TypeToken<List<Liked>>() {}.getType();
                list = gson.fromJson(jsonText,listType2);
                likedAdapter = new LikedAdapter(list, getApplicationContext());

                recyclerView.setAdapter(likedAdapter);

                likedAdapter.setOnItemClickListener(new LikedAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Intent intent = new Intent(getApplicationContext(), TrackActivity.class);
                            intent.putExtra("id", list.get(position).getId());
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(int position, View v) {

                        }
                    });




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
            Liked liked= new Liked(idName, name, artist, url);
            list.add(liked);
            c.moveToNext();
            count--;
        }

    }

}
