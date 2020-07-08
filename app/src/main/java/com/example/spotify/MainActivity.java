package com.example.spotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String  clientId="b984bd1413434646bc152710d40fd8a6";
    private static final String CODE = "code";
    private String  clientSecret="7ea1bff5a6404349b6c397c64a61c157";
    private static final String  redirectUri="spotify://callback";
    private static final String  grant_type="authorization_code";
    private Button button;
    private LinearLayout linearLayout;
    static String Authcode;
    private String tokenType;
    private String scopeAccess;
    private Long expiresIn, ExpiryTime;
    private String refreshToken;
    private String AUTHORIZATION_CODE;
    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        button=findViewById(R.id.button);
        linearLayout=findViewById(R.id.login);


        loadData();

        if(AUTHORIZATION_CODE == null || AUTHORIZATION_CODE.equals("")) {
            linearLayout.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(
                            "https://accounts.spotify.com/authorize"+"?client_id="+clientId+"&response_type=code&redirect_uri="+redirectUri+
                                    "&scope=user-read-playback-state user-modify-playback-state user-read-currently-playing streaming app-remote-control user-read-email user-read-private playlist-read-collaborative playlist-modify-public playlist-read-private playlist-modify-private user-library-modify user-top-read user-read-playback-position user-read-recently-played user-follow-read user-follow-modify&show_dialog=true"
                    ));
                    startActivity(intent);
                }
            });

        } else if (!AUTHORIZATION_CODE.equals(" ") && System.currentTimeMillis() > ExpiryTime){
            new RefreshTokenGen().execute();
            Toast.makeText(MainActivity.this, "Token Refreshed",Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    bottomNavigation.setOnNavigationItemSelectedListener(navListener);
                    if (savedInstanceState == null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();
                    }

                }
            },1000);

        } else if (!AUTHORIZATION_CODE.equals("") && System.currentTimeMillis() <= ExpiryTime){
            bottomNavigation.setOnNavigationItemSelectedListener(navListener);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                Window w = getWindow();
//                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            }
            Window window = getWindow();
            window.setFormat(PixelFormat.RGBA_8888);

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
            }

        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri data=getIntent().getData();
        if (data != null && !TextUtils.isEmpty(data.getScheme())){
            String code = data.getQueryParameter(CODE);

            if (!TextUtils.isEmpty(code)) {

                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                AUTHORIZATION_CODE = code;
                Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://accounts.spotify.com/api/")
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                SpotifyClient client1 = retrofit.create(SpotifyClient.class);


                Call<AccessToken> accessTokenCall = client1.getAccessToken(
                        grant_type,
                        clientId,
                        clientSecret,
                        AUTHORIZATION_CODE,
                        redirectUri
                );

                accessTokenCall.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        Log.i("response", response.body() + "");
                        AccessToken responseBody = response.body();

                        Authcode = responseBody.getAccessToken();
                        tokenType = response.body().getTokenType();
                        scopeAccess = response.body().getScope();
                        expiresIn = response.body().getExpiresIn();
                        refreshToken = response.body().getRefreshToken();
                        ExpiryTime = System.currentTimeMillis() + (expiresIn * 1000);
                        saveData();

                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();

                    }
                });
            }
            if(TextUtils.isEmpty(code)) {
                finish();
            }

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.navigation_library:
                            selectedFragment = new LibraryFragment();
                            break;
                        case R.id.navigation_premium:
                            selectedFragment = new PremiumFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    public void  saveData(){
        SharedPreferences.Editor sharedPref = getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        sharedPref.putString("AuthCode", AUTHORIZATION_CODE);
        sharedPref.putString("secCode", Authcode);
        sharedPref.putString("scope", scopeAccess);
        sharedPref.putString("refresh", refreshToken);
        sharedPref.putLong("expiry", ExpiryTime);
        sharedPref.apply();


    }
    public void loadData(){
        SharedPreferences sharedPref = getSharedPreferences("authInfo",Context.MODE_PRIVATE);
        AUTHORIZATION_CODE = sharedPref.getString("AuthCode", "");
        Authcode = sharedPref.getString("secCode", "");
        scopeAccess= sharedPref.getString("scope", "");
        refreshToken = sharedPref.getString("refresh", "");
        ExpiryTime = sharedPref.getLong("expiry", 0);

    }

    class RefreshTokenGen extends AsyncTask<String, Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String jsonStr ="client_id="+clientId+"&client_secret="+clientSecret+ "&grant_type=refresh_token"+"&refresh_token=" + refreshToken;
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://accounts.spotify.com/api/token")
                    .post(RequestBody.create(mediaType, jsonStr))
                    .build();

            try {
                okhttp3.Response response = okHttpClient.newCall(request).execute();
                assert response.body() != null;
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject lJSONObject = new JSONObject(s);
                String ReAccessToken = lJSONObject.getString("access_token");
                String ReExpiresIn = lJSONObject.getString("expires_in");
                String scope = lJSONObject.getString("scope");
                Authcode = ReAccessToken;
                scopeAccess=scope;
                expiresIn = Long.parseLong(ReExpiresIn);
                ExpiryTime = System.currentTimeMillis() + (expiresIn * 1000);

                saveData();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
