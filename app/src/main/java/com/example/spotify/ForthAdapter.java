package com.example.spotify;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ForthAdapter extends RecyclerView.Adapter<ForthAdapter.MyViewHolder> {
    private static ClickListener clickListener;

    Context context;
    private List<Item> itemArrayList= new ArrayList<>();

    public ForthAdapter(List<Item> itemArrayList, Context context){
        this.itemArrayList=itemArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.genre, parent, false);
            return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            Item newRea= itemArrayList.get(position);
            holder.title.setText(newRea.getName());

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            Random rnd = new Random();
            itemView.setOnClickListener((View.OnClickListener) this);
            itemView.setOnLongClickListener((View.OnLongClickListener) this);
            int color1 = Color.argb(230, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            int color2 =Color.argb(230, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {color1,color2});
            drawable.setCornerRadius(12f);
            title.setBackground(drawable);
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

    public void setOnItemClickListener(ForthAdapter.ClickListener clickListener) {
        ForthAdapter.clickListener = clickListener;
    }


}
