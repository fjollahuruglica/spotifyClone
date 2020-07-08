package com.example.spotify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PlaylistFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private FifthAdapter fifthAdapter;
    private List<Item> itemArrayList=new ArrayList<>();
    private TextView total;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView= getView().findViewById(R.id.playlistRecyclerView);
        linearLayout= getView().findViewById(R.id.linearLayout);
        total= getView().findViewById(R.id.total);
        getMyPlaylistFromServer();

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getMyPlaylistFromServer(){
        SpotifyClient client=Interface.getInterface();

        Call<MyPlaylist> myPlaylistCall = client.getMyPlaylists();

        myPlaylistCall.enqueue(new Callback<MyPlaylist>() {
            @Override
            public void onResponse(Call<MyPlaylist> call, final Response<MyPlaylist> response) {


                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getItems());


                Type listType = new TypeToken<List<Item>>() {}.getType();
                itemArrayList = new Gson().fromJson(json, listType);
                fifthAdapter = new FifthAdapter(itemArrayList, getContext());
                recyclerView.setAdapter(fifthAdapter);

                fifthAdapter.setOnItemClickListener(new FifthAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getContext(), PlaylistActivity.class);
                        intent.putExtra("id", response.body().getItems().get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });

            }

            @Override
            public void onFailure(Call<MyPlaylist> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });


////Liked Songs
        Call<MyPlaylist> myLikedCall = client.getUserLikedTracks(1);

        myLikedCall.enqueue(new Callback<MyPlaylist>() {
            @Override
            public void onResponse(Call<MyPlaylist> call, final Response<MyPlaylist> response) {


                total.setText("by me");

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PlaylistActivity.class);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Call<MyPlaylist> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
    }

    }


