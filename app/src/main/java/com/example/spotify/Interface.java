package com.example.spotify;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Interface {

    public static SpotifyClient getInterface(){
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder newRequest = request.newBuilder().header("Authorization", "Bearer " + MainActivity.Authcode);
                return chain.proceed(newRequest.build());
            }
        });
        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .client(okBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        SpotifyClient client = retrofit.create(SpotifyClient.class);

        return client;
    }

}
