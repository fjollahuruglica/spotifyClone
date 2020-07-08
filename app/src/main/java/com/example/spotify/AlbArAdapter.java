package com.example.spotify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbArAdapter extends RecyclerView.Adapter<AlbArAdapter.MyViewHolder> {
    private static ClickListener clickListener;
    Context context;
    private List<Item> itemArrayList= new ArrayList<>();

    public AlbArAdapter(List<Item> itemArrayList, Context context){
        this.itemArrayList=itemArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_albums, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Item myPlaylist= itemArrayList.get(position);
        holder.name.setText(myPlaylist.getName());
        holder.total.setText(myPlaylist.getTotalTracks()+" songs");
        RequestOptions options = new RequestOptions()
                .centerCrop();

        Glide.with(context).load(myPlaylist.getImages().get(0).getUrl()).apply(options).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView name;
        private TextView total;
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            total=itemView.findViewById(R.id.total);
            imageView=itemView.findViewById(R.id.imageView);
            imageView.setClipToOutline(true);
            itemView.setOnClickListener((View.OnClickListener) this);
            itemView.setOnLongClickListener((View.OnLongClickListener) this);
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
    public void setOnItemClickListener(AlbArAdapter.ClickListener clickListener) {
        AlbArAdapter.clickListener = clickListener;
    }


}
