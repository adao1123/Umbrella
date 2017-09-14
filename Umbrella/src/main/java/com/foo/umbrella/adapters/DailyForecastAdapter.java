package com.foo.umbrella.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.models.WeatherOrigin.Forecast;

import java.util.List;

/**
 * Created by adao1 on 9/13/2017.
 */

public class DailyForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = DailyForecastAdapter.class.getSimpleName();
    private List<List<Forecast>> forecasts;
    private Context context;
    private boolean isFahrenheit;

    public DailyForecastAdapter(List<List<Forecast>> forecasts, boolean isFahrenheit) {
        this.forecasts = forecasts;
        this.isFahrenheit = isFahrenheit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new DailyForecastViewHolder(inflater.inflate(R.layout.item_forecast_daily,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DailyForecastViewHolder viewHolder = (DailyForecastViewHolder) holder;
        setViews(viewHolder,position);
    }

    @Override
    public int getItemCount() {
        return (forecasts == null) ? 0 : forecasts.size();
    }

    public class DailyForecastViewHolder extends RecyclerView.ViewHolder{
        public TextView dayTv;
        public RecyclerView hourlyRv;

        public DailyForecastViewHolder(View itemView) {
            super(itemView);
            dayTv = (TextView) itemView.findViewById(R.id.forecast_item_day_tv);
            hourlyRv = (RecyclerView) itemView.findViewById(R.id.forecast_item_rv);
        }
    }

    public void setIsFahrenheit(boolean isFahrenheit){
        this.isFahrenheit = isFahrenheit;
    }

    /**
     * This method initializes RecyclerView with GridlayoutManager and HourlyForecastAdapter.
     * Hard set grid to have 4 columns.
     * @param recyclerView
     * @param forecastSubList
     * @see GridLayoutManager
     * @see HourlyForecastAdapter
     */
    private void initRecyclerView(RecyclerView recyclerView, List<Forecast> forecastSubList){
        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        recyclerView.setAdapter(new HourlyForecastAdapter(forecastSubList, isFahrenheit));
    }

    private void setViews(DailyForecastViewHolder viewHolder, int position){
        List<Forecast> dailyForecast = forecasts.get(position);
        viewHolder.dayTv.setText(getDayTitle(dailyForecast,position));
        initRecyclerView(viewHolder.hourlyRv, dailyForecast);
    }

    private String getDayTitle(List<Forecast> dailyForecast, int position){
        if (position == 0)
            return  "Today";
        else if (position == 1){
            return  "Tomorrow";
        }else{
            return dailyForecast.get(0).getFCTTIME().getWeekday_name();
        }
    }
}
