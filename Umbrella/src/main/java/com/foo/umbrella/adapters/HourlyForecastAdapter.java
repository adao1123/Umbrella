package com.foo.umbrella.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;

import java.util.List;

/**
 * Created by adao1 on 9/13/2017.
 */

public class HourlyForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Forecast> forecasts;
    private Context context;

    public HourlyForecastAdapter(List<Forecast> forecasts) {
        this.forecasts = forecasts;
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
        viewHolder.timeTV.setText(forecast.getFCTTIME().getCivil());
        viewHolder.tempTV.setText(forecast.getTemp().getEnglish()+(char)0x00B0);
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
}
