package com.example.spotify;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


public class LibraryFragment extends Fragment {
    private TabLayout tabLayout;
    private TabItem playlist;
    private TabItem artist;
    private TabItem album;
    private ViewPager viewPager;
    PageAdapter pageAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tabLayout= (TabLayout) getView().findViewById(R.id.tablayout);
        playlist= (TabItem) getView().findViewById(R.id.tabPlaylist);
        artist= (TabItem) getView().findViewById(R.id.tabArtist);
        album= (TabItem) getView().findViewById(R.id.tabAlbums);
        viewPager= (ViewPager) getView().findViewById(R.id.viewPager);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pageAdapter= new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
