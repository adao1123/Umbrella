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

    public DailyForecastAdapter(List<List<Forecast>> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new DailyForecastViewHolder(inflater.inflate(R.layout.item_forecast_daily,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        List<Forecast> dailyForcast = forecasts.get(position);
        DailyForecastViewHolder viewHolder = (DailyForecastViewHolder) holder;
        viewHolder.dayTv.setText(dailyForcast.get(0).getFCTTIME().getWeekday_name());
        initRecyclerView(viewHolder.hourlyRv, dailyForcast);
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
        recyclerView.setAdapter(new HourlyForecastAdapter(forecastSubList));
    }
}
