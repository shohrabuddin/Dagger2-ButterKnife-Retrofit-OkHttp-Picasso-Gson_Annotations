package com.shohrab.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class is used as POJO class for mapping with carmudi API data (in general for mapping JSON response from an API)
 * Created by shohrab.uddin on 22.11.2015.
 */
public class MetaData {
    /**
    @SerializedName is a GSON Annotation. It takes the key name of JSON response as parameter. Here 'results' is a key/filed name. Note that
    results is a JSON Array that is why List<Result> is being used.
    */
    @SerializedName("results")
    List<Result> results;


    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     * POJO to map results JSON array. 'results' JSON array contains two fields: data and images.
     * 'data' is a JSON Object and images is a JSON array.
     */
    public class Result{
        @SerializedName("data")  //data is a JSON Object within 'results' JSON array.
        Data data;

        @SerializedName("images") //images is a nested JSON array within 'results' JSON array.
        List<CarImages> images;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public List<CarImages> getImages() {
            return images;
        }

        public void setImages(List<CarImages> images) {
            this.images = images;
        }
    }

    /**
     * POJO to map data json object. It contains three fields: name, brandh and price.
     */

    public class Data {
        @SerializedName("name")
        String name;

        @SerializedName("brand")
        String brand;

        @SerializedName("price")
        String price;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

    }



    /**
     * POJO to map images json array. It contains one field: url.
     */

    public class CarImages{
        @SerializedName("url")
        String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
