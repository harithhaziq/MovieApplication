package com.example.mymvvmmovieapplicationjava.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
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
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                moviesResponse.postValue(null);
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
                    }
                }

                @Override
                public void onFailure(Call<JsonResponse> call, Throwable t) {
                    moviesResponse.postValue(null);
                }
            });


        }

    }

    public void refreshMovie(Context context) {
        Call<JsonResponse> call = movieService.getMovieList(Utils.getHeadersMap(), APIService.AUTHORIZATION_HEADER_VALUE, Utils.getLanguage(), 1);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if(response.isSuccessful()){
                    moviesResponse.setValue(response.body());
                    Toast.makeText(context, "Refreshed !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                moviesResponse.postValue(null);
                Toast.makeText(context, "Oops !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
