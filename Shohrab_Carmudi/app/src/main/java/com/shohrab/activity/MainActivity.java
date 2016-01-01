package com.shohrab.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.shohrab.API.CarmudiAPI;
import com.shohrab.MyApplication;
import com.shohrab.R;
import com.shohrab.adapter.CarAdapter;
import com.shohrab.adapter.CarAdapterOffline;
import com.shohrab.model.CarmudiModel;
import com.shohrab.model.CarmudiModelOffline;
import com.shohrab.model.MetaData;
import com.shohrab.utility.SharedPref;
import com.shohrab.utility.SqlLiteDatabaseHandler;
import com.shohrab.utility.StaticValue;
import com.shohrab.utility.Utility;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 *  Created by shohrab.uddin on 22.11.2015.
 */
public class MainActivity extends AppCompatActivity {
    //Injecting view using ButterKnife
    @InjectView(R.id.activity_main_listViewCar) ListView listView_Car;
    @InjectView(R.id.activity_main_txtErrorMsg) TextView txtErrorMsg;
    @InjectView(R.id.activity_main_txtTitle) TextView txtTitle;

    //Injecting Dependencies using Dagger 2
    @Inject Utility mUtility;
    @Inject SharedPref mSharedPref;

    private Retrofit mRetrofit;
    private Call call;
    private CarmudiAPI carmudiAPI;
    private List<MetaData.Result> awesomeCars;
    private List<CarmudiModelOffline> awesomeOfflineCars;
    private CarAdapter carAdapter;
    private CarAdapterOffline carAdapterOffline;
    private ProgressDialog dialog;
    private SqlLiteDatabaseHandler dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.app_icon);

        dbHelper = new SqlLiteDatabaseHandler(getApplicationContext());

        //creating an Object of class Utility and SharedPref using Dagger 2
        mUtility = ((MyApplication) getApplication()).getComponent().provideUtility();
        mSharedPref = ((MyApplication) getApplication()).getComponent().provideSharedPref();
        //End of creating an Object of type CarmudiModel using Dagger 2

        mRetrofit = mUtility.provideRetrofit((MyApplication) getApplication(), this);
        carmudiAPI = mRetrofit.create(CarmudiAPI.class);

        //initialize shared preference
        mSharedPref.setSharedPref(getApplicationContext(), StaticValue.CACHE_PREF);

        callAPI_or_Cache(StaticValue.DEFAULT_SORTING_CRITERIA);

    }

    /**
     * This method is used to show data from remote server using API call
     * @param sortingCriteria
     */
    private void apiCall(final String sortingCriteria){
        dialog = ProgressDialog.show(this, "Wait", "Awesome cars are loading for you...");

        call = carmudiAPI.getCarmudiModel(sortingCriteria); //call Remote REST API using Retrofit. (MAGIC!!)

        call.enqueue(new Callback<CarmudiModel>() {
            @Override
            public void onResponse(Response <CarmudiModel> response, Retrofit retrofit) {
                dialog.dismiss();
                txtTitle.setText("Awesome Cars ("+sortingCriteria+")");
                Log.d("MainActivity", "Status Code = " + response.code());
                if (response.isSuccess()) {
                    txtErrorMsg.setVisibility(View.GONE);
                    CarmudiModel result = response.body(); //Get the JSON response which is mapped to CarmudiModel POJO class (MAGIC!!). No JSON parsing is needed at all!!
                    Log.d("MainActivity", "response = " + new Gson().toJson(result));
                    awesomeCars = result.getMetaData().getResults(); //Now you can access all fields of CarmudiModel class in familiar OOP Style
                    Log.d("MainActivity", "Items = " + awesomeCars.size());
                    carAdapter = new CarAdapter(MainActivity.this, awesomeCars);
                    listView_Car.setAdapter(carAdapter);

                    //Caching data into SQLite
                    dbHelper.addCachedCars(awesomeCars, sortingCriteria); //Traditional Way

                    //Caching timestamp. This timestamp is used to update/snychronize local SQLite DB with remote DB
                    mSharedPref.setCached_time_stamp(mUtility.getUnixTimeStamp());

                }else{  //If response is other than 200 (Ok)
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody != null) {
                        try {
                            txtErrorMsg.setText("responseBody = " + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        txtErrorMsg.setText("responseBody = null");
                    }
                }

            }

            @Override

            public void onFailure(Throwable t) {


                dialog.dismiss();
                txtErrorMsg.setText("t = " + t.getMessage());
            }

        });
    }

    /**
     * This method is used to show data from cache
     * @param sortingTag
     */
    private void showCarsFromCache(String sortingTag){
        txtErrorMsg.setVisibility(View.GONE);
        awesomeOfflineCars = dbHelper.getOfflineCars(sortingTag); //get data from SQLite

        if(awesomeOfflineCars.size()>0){
            carAdapterOffline = new CarAdapterOffline(MainActivity.this, awesomeOfflineCars);
            listView_Car.setAdapter(carAdapterOffline);
            txtTitle.setText("Awesome Cars (Cached)");
        }else{ //No data is available in cache
            if(mUtility.isNetworkAvailable(getApplicationContext())){ // If network is available then fetch data from API
                apiCall(sortingTag);
            }else{ // otherwise show
                txtErrorMsg.setVisibility(View.VISIBLE);
                txtErrorMsg.setText("Sorry!! No data is available in cache.");
                listView_Car.deferNotifyDataSetChanged();
            }

        }
    }

    /**
     * This method is used to take decision for API call or Cache call
     * @param sortingTag
     */
    private void callAPI_or_Cache(String sortingTag){
        Long timeSharePref = SharedPref.sharedPreferences.getLong(StaticValue.TAG_TIMESTAMP, 0); //retrieve the timeStamp value from SharedPreferences
        if (mUtility.isNetworkAvailable(getApplicationContext())){ //if internet is available
            if (mUtility.getUnixTimeStamp() - timeSharePref > 600) { //if cached is older than 600 secs / 10 minutes
                apiCall(sortingTag); //then call API again for new data
            } else { //else if cached data is not more than 10 minutes old then take data from SQLite
                showCarsFromCache(sortingTag);
            }
        }else{ //If network is not available
            if(timeSharePref != 0){ //timeSharePref could be 0 if you first open the app without internet connection.
                showCarsFromCache(sortingTag);
                txtTitle.setText("Awesome Cars (Cached)");

            }else //Whenever new data is inserted into SQLite a timeSamp is cached into timeSharePref. So timeSharePref=0 means there is no data available in SQLite.
                txtErrorMsg.setText("Sorry!!You need internet connection.");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shorting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //You can sort the result by passing sorting parameter to the API URL. (see Line 96 of this class and CarmudiAPI class)
        switch(item.getItemId()) {
            case (R.id.sorting_menu_newest): {
                callAPI_or_Cache(StaticValue.SORTING_CRITERIA_NEWEST);
                break;
            }
            case (R.id.sorting_menu_oldest): {
                callAPI_or_Cache(StaticValue.SORTING_CRITERIA_OLDEST);
                break;
            }
            case (R.id.sorting_menu_pricehigh): {
                callAPI_or_Cache(StaticValue.SORTING_CRITERIA_PRICE_HIGH);
                break;
            }
            case (R.id.sorting_menu_pricelow): {
                callAPI_or_Cache(StaticValue.SORTING_CRITERIA_PRICE_LOW);

                break;
            }
            case (R.id.sorting_menu_mileagehigh): {
                callAPI_or_Cache(StaticValue.SORTING_CRITERIA_MILEAGE_HIGH);
                break;
            }
            case (R.id.sorting_menu_mileagelow): {
                callAPI_or_Cache(StaticValue.SORTING_CRITERIA_MILEAGE_LOW);
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
