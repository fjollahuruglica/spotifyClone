package com.example.spotify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText editText;
    private RecyclerView recyclerView;
    private TextView textView;
    private ImageView imageView, search;
    private SearchAdapter searchAdapter;
    private List<Item> itemArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText=findViewById(R.id.editText);
        recyclerView=findViewById(R.id.searchRecyclerView2);
        imageView=findViewById(R.id.imageView);
        search=findViewById(R.id.imageView3);
        textView=findViewById(R.id.textView3);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==1){
                    getSearchActivityDataFromServer();
                    search.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void getSearchActivityDataFromServer() {
        SpotifyClient client=Interface.getInterface();

        Call<Search> searchCall = client.search(String.valueOf(editText.getText()), "track");

        searchCall.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, final Response<Search> response) {

                Gson gson1 = new Gson();
                String json = gson1.toJson(response.body().getTracks().getItems());


                Type listType = new TypeToken<List<Item>>() {}.getType();
                itemArrayList = new Gson().fromJson(json, listType);
                searchAdapter = new SearchAdapter(itemArrayList, getApplicationContext());
                recyclerView.setAdapter(searchAdapter);
                searchAdapter.setOnItemClickListener(new SearchAdapter.ClickListener() {
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
            public void onFailure(Call<Search> call, Throwable t) {
                Log.i("error", t.getMessage());
            }

        });
    }
}
