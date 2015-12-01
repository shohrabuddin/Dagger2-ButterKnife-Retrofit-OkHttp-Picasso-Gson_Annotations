package com.shohrab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shohrab.model.CarmudiModelOffline;
import com.shohrab.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This Adapter class is used when data is taken from SQLite database instead of calling remote API
 * Created by shohrab.uddin on 22.11.2015.
 */
public class CarAdapterOffline extends BaseAdapter{

    private List<CarmudiModelOffline> mAweSomeCars ;
    private Context mContext ;

    public CarAdapterOffline(Context context, List<CarmudiModelOffline> cars) {
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

        CarmudiModelOffline car = mAweSomeCars.get(position);
        txtCarName.setText(car.getCarName());
        txtCarBrnd.setText(car.getCarBrand());
        txtCarPrice.setText(car.getCarPrice());

        Picasso.with(mContext).invalidate(car.getCarImage());
        Picasso.with(mContext).load(car.getCarImage())
                .error(R.drawable.default_image_carmudi)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(100, 100)
                .into(imgCar);

        return v;
    }
}
