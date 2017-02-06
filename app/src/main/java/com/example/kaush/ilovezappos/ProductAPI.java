package com.example.kaush.ilovezappos;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kaush on 2/3/2017.
 */

public interface ProductAPI {
    static String BASE_URL = "https://api.zappos.com/";

    @GET("Search")
    retrofit2.Call<com.example.kaush.ilovezappos.Products> getProductResults(@Query("term")String term, @Query("key") String key);

    static class Factory {
        private static ProductAPI service;

        public static ProductAPI getIstance(Context context) {
            if (service == null) {

                OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                builder.readTimeout(15, TimeUnit.SECONDS);
                builder.connectTimeout(10, TimeUnit.SECONDS);
                builder.writeTimeout(10, TimeUnit.SECONDS);

                /*builder.certificatePinner(new CertificatePinner.Builder().add("https://api.zappos.com/", "b743e26728e16b81da139182bb2094357c31d331")
                        .build());*/

                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                    builder.addInterceptor(interceptor);
                    Log.d("BuildConfig.Debug if","interceptor added");

                }

                int cacheSize = 10 * 1024 * 1024; // 10 MiB
                Cache cache = new Cache(context.getCacheDir(), cacheSize);
                builder.cache(cache);

                Retrofit retrofit = new Retrofit.Builder().client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(ProductAPI.class);
                Log.d("Service null","service created");

                return service;
            } else {
                Log.d("Service is not null","Here");
                return service;
            }
        }
    }
}

