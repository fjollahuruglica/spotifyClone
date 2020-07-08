package com.example.spotify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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


public class AlbumFragment extends Fragment {
    private RecyclerView recyclerView;
    private SixthAdapter sixthAdapter;
    private List<Item> itemArrayList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView= getView().findViewById(R.id.albumRecyclerView);
        getMyPlaylistFromServer();

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getMyPlaylistFromServer(){
        SpotifyClient client=Interface.getInterface();

        Call<MyAlbums> myAlbumsCall = client.getMyAlbums();

        myAlbumsCall.enqueue(new Callback<MyAlbums>() {
            @Override
            public void onResponse(Call<MyAlbums> call, final Response<MyAlbums> response) {
                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getItems());


                Type listType = new TypeToken<List<Item>>() {}.getType();
                itemArrayList = new Gson().fromJson(json, listType);
                sixthAdapter = new SixthAdapter(itemArrayList, getContext());
                recyclerView.setAdapter(sixthAdapter);

                sixthAdapter.setOnItemClickListener(new SixthAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getContext(), AlbumActivity.class);
                        intent.putExtra("id", response.body().getItems().get(position).getAlbum().getId());
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
