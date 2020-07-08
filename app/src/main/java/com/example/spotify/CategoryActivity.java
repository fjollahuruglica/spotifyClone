package com.example.spotify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private String id;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Item> itemArrayList=new ArrayList<>();
    private TextView textView;
    private ImageView backIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent=getIntent();

        recyclerView=findViewById(R.id.catRecyclerView);
        textView=findViewById(R.id.textView);
        backIcon=findViewById(R.id.backIcon);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id=intent.getStringExtra("id");

        getCategoryDataFromServer();
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void getCategoryDataFromServer() {


        SpotifyClient client = Interface.getInterface();

        final Call<CatPlaylist> catPlaylistCall = client.getCategoryPlaylist(id, "US");

        catPlaylistCall.enqueue(new Callback<CatPlaylist>() {
            @Override
            public void onResponse(Call<CatPlaylist> call, final Response<CatPlaylist> response) {

                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getPlaylists().getItems());
                Log.i("love", response.body().getPlaylists().getItems().get(0).getName()+"");

                textView.setText(response.body().getPlaylists().getItems().get(0).getName());
                Type listType = new TypeToken<List<Item>>() {}.getType();
                itemArrayList = new Gson().fromJson(json, listType);
                categoryAdapter = new CategoryAdapter(itemArrayList, getApplicationContext());

                recyclerView.setAdapter(categoryAdapter);

                categoryAdapter.setOnItemClickListener(new CategoryAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getApplication(), PlaylistActivity.class);
                        intent.putExtra("id", response.body().getPlaylists().getItems().get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });



            }

            @Override
            public void onFailure(Call<CatPlaylist> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
    }
}
