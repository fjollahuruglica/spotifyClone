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

public class AlbumActivity extends AppCompatActivity {
    private String id;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<Item> itemArrayList=new ArrayList<>();
    private TextView albumName, artist;
    private ImageView backIcon, imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        recyclerView=findViewById(R.id.playlistRecyclerView2);
        albumName=findViewById(R.id.albumName);
        artist=findViewById(R.id.artist);
        backIcon=findViewById(R.id.backIcon);
        imageView=findViewById(R.id.imageView);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();

        id=intent.getStringExtra("id");

        getAlbumDataFromServer();
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }


    private void getAlbumDataFromServer() {


        SpotifyClient client = Interface.getInterface();

        final Call<Album> albumCall = client.getAlbum(id);

        albumCall.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, final Response<Album> response) {
                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getTracks().getItems());

                albumName.setText(response.body().getName());
                artist.setText(response.body().getArtists().get(0).getName());
                RequestOptions options = new RequestOptions()
                        .centerCrop();

                Glide.with(getApplicationContext()).load(response.body().getImages().get(0).getUrl()).apply(options).into(imageView);

                Type listType = new TypeToken<List<Item>>() {}.getType();
                itemArrayList = new Gson().fromJson(json, listType);
                listAdapter = new ListAdapter(itemArrayList, getApplicationContext());

                recyclerView.setAdapter(listAdapter);

                listAdapter.setOnItemClickListener(new ListAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getApplication(), TrackActivity.class);
                        intent.putExtra("id", response.body().getTracks().getItems().get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });



            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
    }
}
