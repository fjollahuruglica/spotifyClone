package com.example.spotify;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {
    private static ClickListener clickListener;
    Context context;

    private List<Item> itemArrayList= new ArrayList<>();

    public PlaylistAdapter(List<Item> itemArrayList, Context context){
        this.itemArrayList=itemArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list, parent, false);
            return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            Item track= itemArrayList.get(position);
            holder.songName.setText(track.getTrack().getName());
            holder.artist.setText(track.getTrack().getAlbum().getArtists().get(0).getName());

//            RequestOptions options = new RequestOptions()
//                    .centerCrop();
//
//            Glide.with(context).load(track.getImages().get(0).getUrl()).apply(options).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView songName;
        private TextView artist;
        private ImageView heart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener((View.OnClickListener) this);
            itemView.setOnLongClickListener((View.OnLongClickListener) this);
            songName=itemView.findViewById(R.id.songName);
            artist=itemView.findViewById(R.id.artist);

        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);


        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        PlaylistAdapter.clickListener = clickListener;
    }


}
