package com.example.mymvvmmovieapplicationjava.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static RetrofitBuilder instance = null;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static APIService movieService;

    public static synchronized APIService getMovieService(){
        if(movieService == null){
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    .headers(Headers.of(headers))
                                    .addHeader(APIService.AUTHORIZATION_HEADER_KEY, APIService.AUTHORIZATION_HEADER_VALUE)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitBuilder.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            movieService = retrofit.create(APIService.class);
        }
        return movieService;

    }
}
