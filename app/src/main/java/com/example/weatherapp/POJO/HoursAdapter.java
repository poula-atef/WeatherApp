package com.example.weatherapp.POJO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.POJO.Classes.Forecast;
import com.example.weatherapp.POJO.Utils.WeatherUtils;
import com.example.weatherapp.R;

import java.util.Date;
import java.util.List;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.HoursViewHolder> {

    private List<Forecast> forecast;
    private Context context;


    @NonNull
    @Override
    public HoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new HoursViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_weather_item, parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull HoursViewHolder holder, int position) {
        holder.weather.setText(WeatherUtils.fahrenheitToCelsius(forecast.get(position).getTemperature()) + "°");
        Date date = WeatherUtils.convertUnixToUTC(forecast.get(position).getTime());
        String t = "am";
        int h = date.getHours();
        int m = date.getMinutes();
        if (h > 12) {
            h -= 12;
            t = "pm";
        }
        else if(h == 0)
            h = 12;
        holder.time.setText(h + ":" + m + " " + t);

        holder.icon.setImageDrawable(context.getDrawable(WeatherUtils.chooseWeatherIcon(forecast.get(position).getIcon())));

        holder.itemView.setBackgroundTintList(ColorStateList.valueOf(WeatherUtils.chooseColorBasedOnTime(WeatherUtils.convertUnixToUTC(forecast.get(position).getTime()).getHours())));

    }

    @Override
    public int getItemCount() {
        if (forecast != null)
            return forecast.size();
        return 0;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

    public class HoursViewHolder extends RecyclerView.ViewHolder {
        TextView weather, time;
        ImageView icon;

        public HoursViewHolder(@NonNull View itemView) {
            super(itemView);
            weather = itemView.findViewById(R.id.weather);
            time = itemView.findViewById(R.id.time);
            icon = itemView.findViewById(R.id.icon);
        }


    }
}
