package com.shohrab;

/**
 *This class provides Application level settings. For Example, if you assign int a=2, then you can access the value of a as 2 throughout your application
 * Created by shohrab.uddin on 20.11.2015.
 */

import android.app.Application;

import com.shohrab.dependency_injection.CarmudiComponent;
import com.shohrab.dependency_injection.DaggerCarmudiComponent;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

//com.orm.SugarApp extends Application so MyApplication class also extends it.
public class MyApplication extends Application {
    CarmudiComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerCarmudiComponent.builder().build();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }

    public CarmudiComponent getComponent() {
        return component;
    }


}
