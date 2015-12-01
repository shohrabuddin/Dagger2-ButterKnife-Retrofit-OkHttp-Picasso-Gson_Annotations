package com.shohrab.utility;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shohrab.uddin on 22.11.2015.
 */
public  class Utility {
    ProgressDialog progressDlg;
    Context context;
    boolean isNetworkAvailable;

    public void showProgressDlg(){

        this.progressDlg.setMessage("Please wait...");
        this.progressDlg.setCancelable(false);
        this.progressDlg.show();
    }

    public void hideProgressDlg(){
        this.progressDlg.dismiss();
    }

    //Getter and Setter
    public ProgressDialog getProgressDlg() {
        return progressDlg;
    }

    public void setProgressDlg(Context context) {
        this.progressDlg = new ProgressDialog(context);
    }



    public Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(new File(application.getCacheDir(), "carmudicached"), cacheSize);
        return cache;
    }


    public OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);



        client.networkInterceptors().add(mCacheControlInterceptor);
        /*
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });
        */
        return client;
    }


    private Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            // Add Cache Control only for GET methods
            if (request.method().equals("GET")) {
                if (isNetworkAvailable) {
                    // 1 day
                    request.newBuilder()
                            .header("Cache-Control", "only-if-cached")
                            .build();
                } else {
                    // 4 weeks stale
                    request.newBuilder()
                            .header("Cache-Control", "public, max-stale=2419200")
                            .build();
                }
            }

            Response response = chain.proceed(request);

            // Re-write response CC header to force use of cache
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=86400") // 1 day
                    .build();
        }
    };


    public Retrofit provideRetrofit(Application app, Context context) {

        isNetworkAvailable=isNetworkAvailable(context);
        Cache cache = provideOkHttpCache(app);
        OkHttpClient okHttpClient = provideOkHttpClient(cache);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticValue.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
       return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    public long getUnixTimeStamp(){
        return System.currentTimeMillis() / 1000L;
    }

}
