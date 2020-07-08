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


public class ArtistFragment extends Fragment {
    private RecyclerView recyclerView;
    private SeventhAdapter seventhAdapter;
    private List<Item> itemArrayList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView= getView().findViewById(R.id.artistRecyclerView);

        getMyPlaylistFromServer();

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void getMyPlaylistFromServer(){
        SpotifyClient client=Interface.getInterface();

        Call<MyArtist> myArtistCall = client.getMyArtists("artist");
        final ProgressDialog progressDialog;
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMax(1000);
        progressDialog.show();
        myArtistCall.enqueue(new Callback<MyArtist>() {
            @Override
            public void onResponse(Call<MyArtist> call, final Response<MyArtist> response) {
                progressDialog.dismiss();

                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getArtists().getItems());


                Type listType = new TypeToken<List<Item>>() {}.getType();
                itemArrayList = new Gson().fromJson(json, listType);
                seventhAdapter = new SeventhAdapter(itemArrayList, getContext());
                recyclerView.setAdapter(seventhAdapter);

                seventhAdapter.setOnItemClickListener(new SeventhAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getContext(), ArtistActivity.class);
                        intent.putExtra("id", response.body().getArtists().getItems().get(position).getId());
                        intent.putExtra("name", response.body().getArtists().getItems().get(position).getName());
                        intent.putExtra("image", response.body().getArtists().getItems().get(position).getImages().get(0).getUrl());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });



            }

            @Override
            public void onFailure(Call<MyArtist> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
    }

}
