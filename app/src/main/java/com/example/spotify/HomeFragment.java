package com.example.spotify;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private FirstAdapter firstAdapter;
    private SecondAdapter secondAdapter;
    private ThirdAdapter thirdAdapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private List<Item> itemArrayList=new ArrayList<>();
    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);


    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getHomeFragmentDataFromServer();
        recyclerView= (RecyclerView) getView().findViewById(R.id.firstRecyclerView);
        recyclerView2= (RecyclerView) getView().findViewById(R.id.secondRecyclerView);
        recyclerView3= (RecyclerView) getView().findViewById(R.id.thirdRecyclerView);
        textView= getView().findViewById(R.id.textView);
        textView2= getView().findViewById(R.id.textView2);
        textView3= getView().findViewById(R.id.textView3);

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager2= new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView3.setLayoutManager(layoutManager2);

    }

    private void getHomeFragmentDataFromServer(){


        SpotifyClient client=Interface.getInterface();


/// New releases


        final Call<NewReleases> newReleasesCall= client.getNewReleases("US",6);

        final ProgressDialog progressDialog;
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMax(1000);
        progressDialog.show();
        newReleasesCall.enqueue(new Callback<NewReleases>() {
            @Override
            public void onResponse(Call<NewReleases> call, final Response<NewReleases> response) {
                progressDialog.dismiss();
                textView.setVisibility(View.VISIBLE);
                Gson gson1= new Gson();
                String json=gson1.toJson(response.body().getAlbums().getItems());


                Type listType= new TypeToken<List<Item>>(){}.getType();
                itemArrayList=new Gson().fromJson(json, listType);
                firstAdapter=new FirstAdapter(itemArrayList, getContext());

                recyclerView.setAdapter(firstAdapter);

                firstAdapter.setOnItemClickListener(new FirstAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent= new Intent(getContext(), AlbumActivity.class);
                        intent.putExtra("id", response.body().getAlbums().getItems().get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });


            }

            @Override
            public void onFailure(Call<NewReleases> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
////// Recently Played

        Call<RecentlyPlayed> recentlyPlayedCall=client.getRecentlyPlayed(10);

        recentlyPlayedCall.enqueue(new Callback<RecentlyPlayed>() {
            @Override
            public void onResponse(Call<RecentlyPlayed> call, final Response<RecentlyPlayed> response) {
                progressDialog.dismiss();
                textView2.setVisibility(View.VISIBLE);
                Gson gson1= new Gson();
                final String json=gson1.toJson(response.body().getItems());


                Type listType= new TypeToken<List<Item>>(){}.getType();
                itemArrayList=new Gson().fromJson(json, listType);
                secondAdapter=new SecondAdapter(itemArrayList, getContext());
                recyclerView2.setAdapter(secondAdapter);
                Log.i("responseee", itemArrayList.get(1).getTrack().getName()+"ahhahaha");

                secondAdapter.setOnItemClickListener(new SecondAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent= new Intent(getContext(), TrackActivity.class);
                        intent.putExtra("id", response.body().getItems().get(position).getTrack().getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });


            }

            @Override
            public void onFailure(Call<RecentlyPlayed> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });

////// Top Artists
        Call<FavArtist> favArtistCall=client.getFavArtists("artists",10);

        favArtistCall.enqueue(new Callback<FavArtist>() {
            @Override
            public void onResponse(Call<FavArtist> call, final Response<FavArtist> response) {
                progressDialog.dismiss();
                textView3.setVisibility(View.VISIBLE);
                Gson gson1= new Gson();
                String json=gson1.toJson(response.body().getItems());

                Log.i("hehe", json+"une");

                Type listType= new TypeToken<List<Item>>(){}.getType();
                itemArrayList=new Gson().fromJson(json, listType);
                thirdAdapter=new ThirdAdapter(itemArrayList, getContext());
                recyclerView3.setAdapter(thirdAdapter);

                thirdAdapter.setOnItemClickListener(new ThirdAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent= new Intent(getContext(), ArtistActivity.class);
                        intent.putExtra("id", response.body().getItems().get(position).getId());
                        intent.putExtra("name", response.body().getItems().get(position).getName());
                        intent.putExtra("image", response.body().getItems().get(position).getImages().get(0).getUrl());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });

            }

            @Override
            public void onFailure(Call<FavArtist> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });

    }



}
