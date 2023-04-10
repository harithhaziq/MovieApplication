package com.example.mymvvmmovieapplicationjava.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.mymvvmmovieapplicationjava.Utils;
import com.example.mymvvmmovieapplicationjava.model.Movie.JsonResponse;
import com.example.mymvvmmovieapplicationjava.model.MovieDetail;
import com.example.mymvvmmovieapplicationjava.network.APIService;
import com.example.mymvvmmovieapplicationjava.network.RetrofitBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailViewModel extends ViewModel {

    private MutableLiveData<MovieDetail> movieDetail;
    private int movieId;

    private static APIService movieService = RetrofitBuilder.getMovieService();

    public MovieDetailViewModel(){
        movieDetail = new MutableLiveData<>();
    }

    public MutableLiveData<MovieDetail> getMovieDetailObserver(){
        return movieDetail;
    }

    public void getMovieById(int movieId){
        Call<MovieDetail> call = movieService.getMovieDetailById(movieId ,APIService.AUTHORIZATION_HEADER_VALUE);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if(response.isSuccessful()){
                    movieDetail.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                movieDetail.postValue(null);
            }
        });
    }
}
