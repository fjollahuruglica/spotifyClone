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

public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.MyViewHolder> {
    private static ClickListener clickListener;
    Context context;
    private List<Item> itemArrayList= new ArrayList<>();

    public SecondAdapter(List<Item> itemArrayList, Context context){
        this.itemArrayList=itemArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Item recPlayed= itemArrayList.get(position);
        holder.title.setText(recPlayed.getTrack().getName());

        Log.i("responseee", recPlayed.getTrack().getAlbum().getImages().get(0).getUrl()+"uneee");

        RequestOptions options = new RequestOptions()
                .centerCrop();



        Glide.with(context).load(recPlayed.getTrack().getAlbum().getImages().get(0).getUrl()).apply(options).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView title;
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener((View.OnClickListener) this);
            itemView.setOnLongClickListener((View.OnLongClickListener) this);
            title=itemView.findViewById(R.id.title);
            imageView=itemView.findViewById(R.id.imageView);
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
    public void setOnItemClickListener(SecondAdapter.ClickListener clickListener) {
        SecondAdapter.clickListener = clickListener;
    }


}
