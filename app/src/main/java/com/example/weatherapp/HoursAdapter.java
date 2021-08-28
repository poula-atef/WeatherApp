package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Classes.Forecast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.HoursViewHolder> {

    private List<Boolean> selected;
    private List<Forecast> forecast;
    private Context context;
    private static final String TAG = "tag";


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


    }

    @Override
    public int getItemCount() {
        if (forecast != null)
            return forecast.size();
        return 0;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
        selected = new ArrayList<>();
        for (int i = 0; i < forecast.size(); i++){
            selected.add(false);
        }


    }

    public class HoursViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView weather, time;
        ImageView icon;

        public HoursViewHolder(@NonNull View itemView) {
            super(itemView);
            weather = itemView.findViewById(R.id.weather);
            time = itemView.findViewById(R.id.time);
            icon = itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (selected.get(position)) {
                selected.set(position, false);
                itemView.setBackground(context.getDrawable(R.drawable.item_back));
                for (int i = 0; i < forecast.size(); i++)
                    if (i != position)
                        selected.set(i, false);
                notifyDataSetChanged();
            } else {
                selected.set(position, true);
                itemView.setBackground(context.getDrawable(R.drawable.item_back_selected));
                for (int i = 0; i < forecast.size(); i++)
                    if (i != position)
                        selected.set(i, false);
                notifyDataSetChanged();
            }
        }
    }
}
