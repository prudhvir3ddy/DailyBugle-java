package com.root.dailybugle.adapters;

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
import com.root.dailybugle.models.Model;
import com.root.dailybugle.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

 List<Model> list;
 Context context;

 public SearchAdapter(List<Model> list, Context context){
     this.list=list;
     this.context=context;
 }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.smodel,parent,false);
      SearchViewHolder searchViewHolder = new SearchViewHolder(view);
      return  searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int position) {
            final Model model = list.get(position);
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
                intent.putExtra(Constants.URLTOIMAGE, model.getImage());
                intent.putExtra(Constants.DESCRIPTION, model.getDesc());
                intent.putExtra(Constants.TITLE, model.getTitle());
                intent.putExtra(Constants.AUTHOR, model.getAuthor());
                intent.putExtra(Constants.SOURCE, model.getSname());
                intent.putExtra(Constants.URL, model.getUrl());
                intent.putExtra(Constants.PUBLISHEDAT, model.getDate());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
     ImageView imageView;
     ProgressBar progressBar;
     Button b2;
     CardView cardView;
        public SearchViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.home_imgview);
            progressBar=itemView.findViewById(R.id.progress);
            b2=itemView.findViewById(R.id.b2);
            cardView=itemView.findViewById(R.id.c1);
        }
    }
}
