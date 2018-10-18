package com.root.dailybugle.adapters;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.root.dailybugle.R;
import com.root.dailybugle.activities.NewsDetailActivity;
import com.root.dailybugle.database.NewsModel;
import com.root.dailybugle.models.Model;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    List<NewsModel> list;
    Context context;

    public FavouriteAdapter(List<NewsModel> list,Context context){
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.smodel,parent,false);
        FavouriteViewHolder favouriteViewHolder = new FavouriteViewHolder(view);
        return  favouriteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouriteViewHolder holder, int position) {
        final NewsModel model = list.get(position);
        String url = model.getImage();
        Picasso.with(context)
                .load(url)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.imageView.setImageResource(R.drawable.news);
                    }


                });
        holder.b2.setText(model.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NewsDetailActivity.class);
                intent.putExtra("image", model.getImage());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("title", model.getTitle());
                intent.putExtra("author", model.getAuthor());
                intent.putExtra("source", model.getSname());
                intent.putExtra("url", model.getUrl());
                intent.putExtra("publishedAt", model.getDate());
                intent.putExtra("ButtonText",model.isThere());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        Button b2;
        CardView cardView;
        public FavouriteViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.home_imgview);
            progressBar=itemView.findViewById(R.id.progress);
            b2=itemView.findViewById(R.id.b2);
            cardView=itemView.findViewById(R.id.c1);
        }
    }
}
