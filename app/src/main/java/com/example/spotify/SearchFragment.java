package com.example.spotify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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


public class SearchFragment extends Fragment {
    private ForthAdapter forthAdapter;
    private RecyclerView recyclerView;
    private List<Item> itemArrayList=new ArrayList<>();
    private LinearLayout linearLayout;
    private Button button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getSearchFragmentDataFromServer();
        recyclerView= (RecyclerView) getView().findViewById(R.id.searchRecyclerView);
        button= getView().findViewById(R.id.button);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(gridLayoutManager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getSearchFragmentDataFromServer() {
        SpotifyClient client=Interface.getInterface();
/// Genre

        Call<Genre> genreCall = client.getGenre("US");

        final ProgressDialog progressDialog;
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMax(100);
        progressDialog.show();
        genreCall.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, final Response<Genre> response) {
                progressDialog.dismiss();

                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getCategories().getItems());


                Type listType = new TypeToken<List<Item>>() {}.getType();
                itemArrayList = new Gson().fromJson(json, listType);
                forthAdapter = new ForthAdapter(itemArrayList, getContext());
                recyclerView.setAdapter(forthAdapter);

                forthAdapter.setOnItemClickListener(new ForthAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getContext(), CategoryActivity.class);
                        intent.putExtra("id", response.body().getCategories().getItems().get(position).getId());
                        startActivity(intent);
                        Log.i("love", "une");
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });



            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
    }
}
