package com.shohrab.API;

import com.shohrab.model.CarmudiModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * Created by shohrab.uddin on 22.11.2015.
 * Retrofit Interface for API calls like GET/POST etc.
 */
public interface CarmudiAPI {
    @Headers("Cache-Control: max-age=640000, s-maxage=640000 , max-stale=2419200") // This is optional
    @GET("/api/cars/") //the complete URL is https://www.carmudi.ae/api/cars/ (https://www.carmudi.ae is the base URL which is available in StaticValue class)
   /*
    Retrofit interface 'Call<T>' causes the API call (https://www.carmudi.ae/api/cars). The URL takes a query parameter called 'sort'.
    Including the paramter the URL looks like https://www.carmudi.ae/api/cars/sort:[value] (value could be newest, oldest etc). You can add query paramter by
    using @Query(parametername) annotation.
    */
    Call<CarmudiModel> getCarmudiModel(@Query("sort") String sortingCriteria);

}
