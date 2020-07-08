package com.example.spotify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class PremiumFragment extends Fragment {
    private Button button, button2, button3, button4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_premium, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        button= getView().findViewById(R.id.button);
        button2= getView().findViewById(R.id.button2);
        button3= getView().findViewById(R.id.button3);
        button4= getView().findViewById(R.id.button4);

        final Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.spotify.com/us/purchase/offer/default-trial-1m/"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });


    }


}
