package com.example.mymvvmmovieapplicationjava.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymvvmmovieapplicationjava.MoviesDetailActivity;
import com.example.mymvvmmovieapplicationjava.R;
import com.example.mymvvmmovieapplicationjava.model.Movie.JsonResponse;
import com.example.mymvvmmovieapplicationjava.model.Movie.Result;
import com.example.mymvvmmovieapplicationjava.network.APIService;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {

    private Context context;
    private JsonResponse movieResponse;

    public MovieListAdapter(Context context, JsonResponse movieResponse){
        this.context = context;
        this.movieResponse = movieResponse;
    }

    public void setMovieResponse(JsonResponse movieResponse){
        this.movieResponse = movieResponse;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(this.movieResponse.getResults().get(position).getTitle());
        holder.tvTitle.setTextColor(Color.WHITE);
        holder.tvTitle.setShadowLayer(1.6f,1.5f,1.3f,Color.BLACK);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HAZIQ", "onClick: " + holder.getPosition());
                int position = holder.getPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Result movie = movieResponse.getResults().get(position);
                    int movieId = movie.getId();

                    Intent intent = new Intent(context.getApplicationContext(), MoviesDetailActivity.class);
                    intent.putExtra("movieId", movieId);
                    context.startActivity(intent);
                }
            }
        });

        holder.tvRating.setText(String.valueOf(this.movieResponse.getResults().get(position).getVoteAverage()) + " *");
        setTvRatingColor(holder.tvRating, movieResponse.getResults().get(position).getVoteAverage());


        Glide.with(context)
                .load(APIService.baseUrlPoster + this.movieResponse.getResults().get(position).getPosterPath())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);
    }

    private void setTvRatingColor(TextView tvRating, double voteAverage) {
        if(voteAverage >= 8){
            tvRating.setTextColor(Color.GREEN);
            tvRating.setShadowLayer(1.6f,1.5f,1.3f,Color.BLACK);
        } else if (voteAverage >6) {
            tvRating.setTextColor(Color.YELLOW);
            tvRating.setShadowLayer(1.6f,1.5f,1.3f,Color.BLACK);
        }else{
            tvRating.setTextColor(Color.RED);
            tvRating.setShadowLayer(1.6f,1.5f,1.3f,Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        if(this.movieResponse != null){
            return this.movieResponse.getResults().size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvRating;
        ImageView imageView;
        public MyViewHolder(View itemView){
            super(itemView);
            tvRating = itemView.findViewById(R.id.tv_rating_movie_item);
            tvTitle = itemView.findViewById(R.id.tv_movie_item);
            imageView = itemView.findViewById(R.id.iv_movie_item);
        }
    }
}
