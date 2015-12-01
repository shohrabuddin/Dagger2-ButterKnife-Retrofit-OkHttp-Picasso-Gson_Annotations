package com.shohrab.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by shohrab.uddin on 22.11.2015.
 * This class is used as POJO class for mapping with carmudi API data (in general for mapping JSON response from an API)
 */
public class CarmudiModel {

    @SerializedName("success") //@SerializedName is a GSON Annotation. It takes the key name of JSON response as parameter. Here 'success' is a key/filed name
    private boolean success;

    @SerializedName("metadata") //metadata is a nested JSON object within the JSON response. That is why another class is needed to represent it.
    private MetaData metaData;

    // check this URL to see the JSON response. http://www.carmudi.ae/api/cars/

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

}
