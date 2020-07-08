package com.example.spotify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArtistActivity extends AppCompatActivity {
    private String id, name, image;
    private ImageView backIcon, imageView;
    private List<Track> itemArrayList=new ArrayList<>();
    private List<Item> itemArrayList2=new ArrayList<>();
    private RecyclerView recyclerView, recyclerView2;
    private ArtistsTopAdapter artistsTopAdapter;
    private TextView artist, textView, textView2;
    private AlbArAdapter albArAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        Intent intent=getIntent();

        backIcon=findViewById(R.id.backIcon);
        recyclerView=findViewById(R.id.artistRecyclerView2);
        recyclerView2=findViewById(R.id.artistRecyclerView3);
        imageView=findViewById(R.id.imageView);
        artist=findViewById(R.id.artist);
        textView=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        image=intent.getStringExtra("image");


        getTopTrackDataFromServer();
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        RecyclerView.LayoutManager layoutManager2= new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
    }

    private void getTopTrackDataFromServer() {


        SpotifyClient client = Interface.getInterface();

        final Call<GetTop> itemCall = client.getArtistTopTracks(id, "US");

        itemCall.enqueue(new Callback<GetTop>() {
            @Override
            public void onResponse(Call<GetTop> call, final Response<GetTop> response) {
                textView.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getTracks());
                artist.setText(name);
                RequestOptions options = new RequestOptions()
                        .centerCrop();

                Glide.with(getApplicationContext()).load(image).apply(options).into(imageView);

                Type listType = new TypeToken<List<Track>>() {}.getType();
                itemArrayList = new Gson().fromJson(json, listType);
                artistsTopAdapter = new ArtistsTopAdapter(itemArrayList, getApplicationContext());

                recyclerView.setAdapter(artistsTopAdapter);

                artistsTopAdapter.setOnItemClickListener(new ArtistsTopAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getApplication(), TrackActivity.class);
                        intent.putExtra("id", response.body().getTracks().get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });


            }

            @Override
            public void onFailure(Call<GetTop> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });


        final Call<MyAlbums> artistAlbumCall = client.getArtistAlbums(id, "album");

        artistAlbumCall.enqueue(new Callback<MyAlbums>() {
            @Override
            public void onResponse(Call<MyAlbums> call, final Response<MyAlbums> response) {

                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getItems());
                Log.i("love", response+"");

                Type listType = new TypeToken<List<Item>>() {}.getType();
                itemArrayList2 = new Gson().fromJson(json, listType);
                albArAdapter = new AlbArAdapter(itemArrayList2, getApplicationContext());

                recyclerView2.setAdapter(albArAdapter);

                albArAdapter.setOnItemClickListener(new AlbArAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getApplication(), AlbumActivity.class);
                        intent.putExtra("id", response.body().getItems().get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });



            }

            @Override
            public void onFailure(Call<MyAlbums> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
    }
}
