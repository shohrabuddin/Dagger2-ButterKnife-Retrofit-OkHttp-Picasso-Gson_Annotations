package com.shohrab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shohrab.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This the the initial activity of the app
 *  Created by shohrab.uddin on 22.11.2015.
 */
public class InitialActivity extends AppCompatActivity {
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.app_icon);


    }

    /**
     * Injecting action of button click
     */
    @OnClick(R.id.activity_initial_BtnStart)
    public void butonStartClick(){
        Intent mainIntent = new Intent(InitialActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    /**
     * This method is used to open AndroidDatabaseManager activity for easy working with SQLite database. Currently R.id.activity_initial_BtnShowDB is set to
     * Invisible. If you are interested to see how it works simply make it visible from activity_initial.xml file
     */
    @OnClick(R.id.activity_initial_BtnShowDB)
    public void butonDBClick(){
        Intent mainIntent = new Intent(InitialActivity.this, AndroidDatabaseManager.class);
        startActivity(mainIntent);
    }
}
