package com.example.mymvvmmovieapplicationjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymvvmmovieapplicationjava.model.MovieDetail;
import com.example.mymvvmmovieapplicationjava.network.APIService;
import com.example.mymvvmmovieapplicationjava.viewmodel.MovieDetailViewModel;

import java.text.Format;

public class MoviesDetailActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvDesc;
    TextView tvReleaseDate;
    ImageView ivAdultLogo, ivBackdrop;
    Button btnPopularity, btnVisitWebsite;
    private int movieId;
    private MovieDetailViewModel movieDetailViewModel;
    private MovieDetail movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);

        initLayout();

        getIntents();

        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.getMovieDetailObserver().observe(this, new Observer<MovieDetail>() {
            @Override
            public void onChanged(MovieDetail movieDetail) {
                if(movieId != 0){
                    movieDetails = movieDetail;
                    tvTitle.setText(movieDetails.getTitle());
                    tvDesc.setText(movieDetails.getOverview());
                    tvReleaseDate.setText(movieDetails.getReleaseDate());
                    btnPopularity.setText(String.format("%.2f", movieDetails.getVoteAverage()));

                    setColorBtnPopularity(movieDetails);


                    if(movieDetails.getAdult()){
                        ivAdultLogo.setVisibility(View.VISIBLE);
                    }

                    Glide.with(MoviesDetailActivity.super.getBaseContext())
                            .load(APIService.baseUrlPoster + movieDetails.getBackdropPath())
                            .apply(RequestOptions.centerCropTransform())
                            .into(ivBackdrop);

                    btnVisitWebsite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goToUri(movieDetails.getHomepage());
                        }
                    });
                    ivBackdrop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goToUri(movieDetails.getHomepage());
                        }
                    });


                }else{
                    tvTitle.setText("NOT FOUND");
                }
            }
        });
        movieDetailViewModel.getMovieById(movieId);
    }

    private void setColorBtnPopularity(MovieDetail movieDetails) {
        if(movieDetails.getVoteAverage() >= 8){
            btnPopularity.setBackgroundColor(Color.GREEN);
        } else if (movieDetails.getVoteAverage() >= 6) {
            btnPopularity.setBackgroundColor(Color.YELLOW);
        }else{
            btnPopularity.setBackgroundColor(Color.RED);
        }
    }

    private void getIntents() {
        movieId = getIntent().getIntExtra("movieId", 0);
    }

    public void initLayout(){
        tvTitle = findViewById(R.id.tv_movie_title);
        tvDesc = findViewById(R.id.tv_movie_desc);
        tvReleaseDate = findViewById(R.id.tv_movie_release_date);
        ivAdultLogo = findViewById(R.id.iv_movie_adult);
        ivBackdrop = findViewById(R.id.iv_backdrop);
        btnPopularity = findViewById(R.id.btn_popularity);
        btnVisitWebsite = findViewById(R.id.btn_visit_website);
    }

    private void goToUri(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}