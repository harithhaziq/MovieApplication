package com.example.mymvvmmovieapplicationjava.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymvvmmovieapplicationjava.Utils;
import com.example.mymvvmmovieapplicationjava.model.Movie.JsonResponse;
import com.example.mymvvmmovieapplicationjava.model.Movie.Result;
import com.example.mymvvmmovieapplicationjava.network.APIService;
import com.example.mymvvmmovieapplicationjava.network.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListViewModel extends ViewModel {

    private MutableLiveData<JsonResponse> moviesResponse;
    int pageNo = 1; //default

    private static APIService movieService = RetrofitBuilder.getMovieService();

    public MovieListViewModel(){
        moviesResponse = new MutableLiveData<>();
    }

    public MutableLiveData<JsonResponse> getMoviesListObserver(){
        return moviesResponse;
    }


    public void fetchMovie(){

        Call<JsonResponse> call = movieService.getMovieList(Utils.getHeadersMap(), APIService.AUTHORIZATION_HEADER_VALUE, Utils.getLanguage(), pageNo);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if(response.isSuccessful()){
                    moviesResponse.postValue(response.body());
                    Log.d("HAZIQ", "onResponse: " + response.isSuccessful());
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                moviesResponse.postValue(null);
                Log.d("HAZIQ", "onFailure: " + t.getMessage());
            }
        });
    }

    public void loadNextPage(){
        JsonResponse currentData = moviesResponse.getValue();

        if (currentData != null) {
            pageNo++;
            Call<JsonResponse> call = movieService.getMovieList(Utils.getHeadersMap(), APIService.AUTHORIZATION_HEADER_VALUE, Utils.getLanguage(), pageNo);
            call.enqueue(new Callback<JsonResponse>() {
                @Override
                public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                    if(response.isSuccessful() && response.body().getResults()!= null){
                        currentData.getResults().addAll(response.body().getResults());
                        moviesResponse.setValue(currentData);
                        Log.d("HAZIQ", "onResponse: " + response.isSuccessful());
                    }
                }

                @Override
                public void onFailure(Call<JsonResponse> call, Throwable t) {
                    moviesResponse.postValue(null);
                    Log.d("HAZIQ", "onFailure: " + t.getMessage());
                }
            });


        }

    }

    public void refreshMovie() {
        Call<JsonResponse> call = movieService.getMovieList(Utils.getHeadersMap(), APIService.AUTHORIZATION_HEADER_VALUE, Utils.getLanguage(), 3);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if(response.isSuccessful()){
                    moviesResponse.setValue(response.body());
                    Log.d("HAZIQ", "onResponse: " + response.isSuccessful());
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                moviesResponse.postValue(null);
                Log.d("HAZIQ", "onFailure: " + t.getMessage());
            }
        });
    }
}
