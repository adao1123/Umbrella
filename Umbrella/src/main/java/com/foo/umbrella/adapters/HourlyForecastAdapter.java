package com.foo.umbrella.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by adao1 on 9/13/2017.
 */

public class HourlyForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Forecast> forecasts;
    private Context context;
    private boolean isFahrenheit;

    public HourlyForecastAdapter(List<Forecast> forecasts, boolean isFahrenheit) {
        this.forecasts = forecasts;
        this.isFahrenheit = isFahrenheit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new HourlyForcastViewHolder(inflater.inflate(R.layout.item_forecast_hourly,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Forecast forecast = forecasts.get(position);
        HourlyForcastViewHolder viewHolder = (HourlyForcastViewHolder)holder;
        setViews(viewHolder,forecast);
    }

    @Override
    public int getItemCount() {
        return (forecasts == null) ? 0 : forecasts.size();
    }

    public class HourlyForcastViewHolder extends RecyclerView.ViewHolder{
        public TextView timeTV;
        public TextView tempTV;
        public ImageView iconIV;

        public HourlyForcastViewHolder(View itemView) {
            super(itemView);
            timeTV = (TextView) itemView.findViewById(R.id.hourly_item_time_tv);
            tempTV = (TextView) itemView.findViewById(R.id.hourly_item_temp_tv);
            iconIV = (ImageView) itemView.findViewById(R.id.hourly_item_icon_iv);
        }
    }

    private void setViews(HourlyForcastViewHolder viewHolder, Forecast forecast){
        viewHolder.timeTV.setText(forecast.getFCTTIME().getCivil());
        if (isFahrenheit) {
            viewHolder.tempTV.setText(forecast.getTemp().getEnglish()+(char)0x00B0);
        }else{
            viewHolder.tempTV.setText(forecast.getTemp().getMetric()+(char)0x00B0);
        }
        Picasso.with(context).load(forecast.getIcon_url()).into(viewHolder.iconIV);
        setColdHotColor(viewHolder,forecast);
    }

    private void setColdHotColor(HourlyForcastViewHolder viewHolder, Forecast forecast){
        if (forecast.isHottest()&&forecast.isColdest()) return;
        if (forecast.isColdest()) {
            int color = ContextCompat.getColor(context,R.color.weather_cool);
            viewHolder.timeTV.setTextColor(color);
            viewHolder.tempTV.setTextColor(color);
            viewHolder.iconIV.setColorFilter(color);
        }
        if (forecast.isHottest()) {
            int color = ContextCompat.getColor(context,R.color.weather_warm);
            viewHolder.timeTV.setTextColor(color);
            viewHolder.tempTV.setTextColor(color);
            viewHolder.iconIV.setColorFilter(color);
        }
    }
}
