package com.example.weatherapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.weatherapp.POJO.Classes.Datum;
import com.example.weatherapp.POJO.Classes.Forecast;
import com.example.weatherapp.POJO.Classes.ForecastRoot;
import com.example.weatherapp.POJO.Classes.GeocoderRoot;
import com.example.weatherapp.POJO.HoursAdapter;
import com.example.weatherapp.POJO.MVVM.WeatherViewModel;
import com.example.weatherapp.POJO.Room.WeatherDatabase;
import com.example.weatherapp.POJO.Utils.WeatherUtils;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWeatherData();


    }

    private void getCurrentPlaceName(String query) {
        WeatherViewModel model = ViewModelProviders.of(this).get(WeatherViewModel.class);
        model.getGeocoderData(query);
        model.getGeocoderLiveData().observe(this, new Observer<GeocoderRoot>() {
            @Override
            public void onChanged(GeocoderRoot geocoderRoot) {
                Datum datum = geocoderRoot.getData().get(0);
                String place = datum.getCounty() + "," + datum.getRegion() + "," + datum.getCountry();
                binding.location.setText(place);
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("location", place).apply();
            }
        });
    }


    /**
     * getCurrentLocation use FusedLocationProviderClient to get current location longitude and latitude
     * then use it to get get current place name using Position Stack Api and get weather data from
     * Ambee Api or Room DB
     */
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    getCurrentPlaceName(location.getLatitude() + "," + location.getLongitude());
                    getWeatherDataFromApi(location.getLatitude(), location.getLongitude());
                } else {
                    Toast.makeText(MainActivity.this, "Can't find your location now, please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * getWeatherData on the upstream we see if the data we cache in Room DB is up to date or not,
     * if it has the data of today we use it directly otherwise we clear the DB and request the data
     * from Ambee Api and cache it in the DB
     */
    private void getWeatherData() {
        WeatherDatabase db = WeatherDatabase.getInstance(MainActivity.this);
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
                emitter.onNext(WeatherUtils.getTodaysWeather(db.weatherDao().getAllWeather()).size() <= 1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Throwable {
                        if (aBoolean) {
                            removeAllDataFromDatabase(db);
                            getCurrentLocation();
                        } else {
                            getWeatherDataFromDatabase();
                        }
                    }
                });
    }

    private void removeAllDataFromDatabase(WeatherDatabase db) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                db.weatherDao().removeAllWeather();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private void getWeatherDataFromDatabase() {
        HoursAdapter adapter = new HoursAdapter();
        WeatherDatabase db = WeatherDatabase.getInstance(MainActivity.this);
        Observable.create(new ObservableOnSubscribe<List<Forecast>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Forecast>> emitter) throws Throwable {
                emitter.onNext(db.weatherDao().getAllWeather());
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Forecast>>() {
                    @Override
                    public void accept(List<Forecast> forecasts) throws Throwable {
                        setMainData(WeatherUtils.getNowForecast(forecasts, Calendar.getInstance().getTime()));
                        adapter.setForecast(WeatherUtils.getTodaysWeather(forecasts));
                        setRecyclerViewAdapter(adapter);
                    }
                });

        binding.location.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("location","?"));

    }

    private void getWeatherDataFromApi(double lat, double lng) {
        WeatherViewModel model = ViewModelProviders.of(this).get(WeatherViewModel.class);
        model.getWeatherData(lat, lng, "hourly");
        model.getWeatherLiveData().observe(MainActivity.this, new Observer<ForecastRoot>() {
            @Override
            public void onChanged(ForecastRoot forecastRoot) {

                insertDataInDatabase(forecastRoot);

            }
        });
    }

    private void insertDataInDatabase(ForecastRoot forecastRoot) {

        WeatherDatabase db = WeatherDatabase.getInstance(MainActivity.this);
        HoursAdapter adapter = new HoursAdapter();

        Observable.create(new ObservableOnSubscribe<List<Forecast>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Forecast>> emitter) throws Throwable {
                emitter.onNext(forecastRoot.getData().getForecast());
            }
        }).observeOn(Schedulers.io()).subscribe(new Consumer<List<Forecast>>() {
            @Override
            public void accept(List<Forecast> forecasts) throws Throwable {
                db.weatherDao().insertWeather(forecasts);
                adapter.setForecast(WeatherUtils.getTodaysWeather(forecasts));
                setRecyclerViewAdapter(adapter);
                setMainData(WeatherUtils.getNowForecast(forecasts, Calendar.getInstance().getTime()));
            }
        });
    }

    private void setRecyclerViewAdapter(HoursAdapter adapter) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                binding.rec.setAdapter(adapter);
                binding.rec.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                binding.rec.setHasFixedSize(true);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void setMainData(Forecast data) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                binding.iconImg.setImageDrawable(getDrawable(WeatherUtils.chooseWeatherIcon(data.getIcon())));
                binding.weather.setText(WeatherUtils.fahrenheitToCelsius(data.getTemperature()) + "??");
                binding.summary.setText(data.getSummary());
                binding.date.setText(WeatherUtils.getDateFormat(WeatherUtils.convertUnixToUTC(data.getTime())));
                binding.wind.setText((int) data.getWindSpeed() + " km/h");
                binding.humidity.setText((int) (data.getHumidity() * 100) + "%");
                binding.rain.setText((int) (data.getPrecipProbability() * 100) + "%");
                binding.container.setBackgroundTintList(ColorStateList.valueOf(WeatherUtils.chooseColorBasedOnTime(WeatherUtils.convertUnixToUTC(data.getTime()).getHours())));
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length != 0) {
            getCurrentLocation();
        } else {
            Toast.makeText(MainActivity.this, "You should grant this permissions !!", Toast.LENGTH_SHORT).show();
        }
    }


}