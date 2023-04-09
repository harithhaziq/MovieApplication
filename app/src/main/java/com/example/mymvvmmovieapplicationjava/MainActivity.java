package com.example.mymvvmmovieapplicationjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mymvvmmovieapplicationjava.adapter.MovieListAdapter;
import com.example.mymvvmmovieapplicationjava.model.Movie.JsonResponse;
import com.example.mymvvmmovieapplicationjava.viewmodel.MovieListViewModel;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private MovieListAdapter adapter;
    private JsonResponse movieResponse;
    private MovieListViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_no_result = findViewById(R.id.tv_no_result_ma);

        recyclerView = findViewById(R.id.rv_movie_list_ma);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieListAdapter(this, movieResponse);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    isLoading = true;
                    viewModel.loadNextPage();
                }
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.refreshMovie());


        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        viewModel.getMoviesListObserver().observe(this, new Observer<JsonResponse>() {
            @Override
            public void onChanged(JsonResponse jsonResponse) {
                    movieResponse = jsonResponse;
                    adapter.setMovieResponse(jsonResponse);
                    tv_no_result.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    isLoading = false;

//                    tv_no_result.setVisibility(View.VISIBLE);
            }
        });

        viewModel.fetchMovie();
    }
}