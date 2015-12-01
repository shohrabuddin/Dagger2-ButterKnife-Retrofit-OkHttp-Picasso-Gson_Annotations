package com.shohrab.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shohrab.model.MetaData;
import com.shohrab.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shohrab.uddin on 22.11.2015.
 */
public class CarAdapter extends BaseAdapter{

    private List<MetaData.Result> mAweSomeCars ;
    private Context mContext ;

    public CarAdapter(Context context, List<MetaData.Result> cars) {
        super();
        this.mContext = context ;
        this.mAweSomeCars = cars ;
    }

    @Override
    public int getCount() {
        return mAweSomeCars.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.layout_list_view_car, parent, false);
        TextView txtCarName = (TextView) v.findViewById(R.id.layout_list_view_car_txtViewCarName);
        TextView txtCarBrnd = (TextView) v.findViewById(R.id.layout_list_view_car_txtViewBrannd);
        TextView txtCarPrice = (TextView) v.findViewById(R.id.layout_list_view_car_txtViewPrice);
        ImageView imgCar = (ImageView) v.findViewById((R.id.layout_list_view_car_imgViewCar));

        MetaData.Result car = mAweSomeCars.get(position);
        txtCarName.setText(car.getData().getName());
        txtCarBrnd.setText(car.getData().getBrand());
        txtCarPrice.setText(car.getData().getPrice());


        Picasso.with(mContext).load(car.getImages().get(0).getUrl())
                .error(R.drawable.default_image_carmudi)
                .resize(100, 100)
                .into(imgCar);

        Log.d("Adapter", car.getData().getName());
        return v;
    }
}
