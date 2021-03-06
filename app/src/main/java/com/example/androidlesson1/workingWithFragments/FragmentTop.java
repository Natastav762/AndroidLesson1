package com.example.androidlesson1.workingWithFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidlesson1.Constants;
import com.example.androidlesson1.R;
import com.example.androidlesson1.singletons.SingletonForImage;
import com.example.androidlesson1.weatherModel.WeatherRequest;
import com.example.androidlesson1.workingWithWeatherData.WeatherData;
import com.example.androidlesson1.workingWithWeatherData.WeatherFromInternet;


import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentTop extends Fragment implements Constants, Subscriber, WeatherFromInternet {

    private ImageView weatherPictureView;
    private TextView cityTextView;
    private TextView dayTextView;
    private TextView temperatureTextView;
    private TextView descriptionTextView;
    private TextView windTextView;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private View view;
    private SharedPreferences sharedPreferences;
    private boolean isDataUpdateRequired = true;
    private String cityText;
    private Handler handler;


    public void setDataUpdateRequired(boolean dataUpdateRequired) {
        isDataUpdateRequired = dataUpdateRequired;
    }

    public static FragmentTop create() { //фабричный метод
        FragmentTop fragmentTop = new FragmentTop();
        Bundle args = new Bundle();
        fragmentTop.setArguments(args);
        return fragmentTop;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_top, container, false);
        init();


        if (savedInstanceState != null) {
            isDataUpdateRequired = savedInstanceState.getBoolean("IsDataUpdateRequired");
            cityText = savedInstanceState.getString(CITY); //название говорода, для которого будут скачиваться данные
        }

        if (savedInstanceState != null && !isDataUpdateRequired) { //если есть сохраненные данные и нет необходимости их обновлять
            cityTextView.setText(savedInstanceState.getString(CITY));
            temperatureTextView.setText(savedInstanceState.getString("temperature"));
            dayTextView.setText(savedInstanceState.getString("day"));
            descriptionTextView.setText(savedInstanceState.getString("description"));
            windTextView.setText(savedInstanceState.getString("wind"));
            pressureTextView.setText(savedInstanceState.getString("pressure"));
            humidityTextView.setText(savedInstanceState.getString("humidity"));
            weatherPictureView = SingletonForImage.getInstance().getWeatherPicture();
        } else { //в противном случае загрузить данные из интернета
            WeatherData weatherData = new WeatherData(this,
                    cityText);
            weatherData.getWeatherData();
            cityTextView.setText(cityText);
            isDataUpdateRequired = false; //это нужно, чтобы при смене ориентации не скачивались новые данные
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void updateData(String newDay, String newTemperature, Drawable newWeatherPicture, String newWind, //после нажатия на какой-то день в нижнем фрагменте
                           String newPressure, String newHumidity, String newDescription) {
        dayTextView.setText(newDay);
        temperatureTextView.setText(newTemperature);
        weatherPictureView.setImageDrawable(newWeatherPicture);
        windTextView.setText(newWind);
        pressureTextView.setText(newPressure);
        humidityTextView.setText(newHumidity);
        descriptionTextView.setText(newDescription.toUpperCase());
    }

    public void updateCity(String newCity) { //то, что меняется в настройках приложения
        cityTextView.setText(newCity);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CITY, cityTextView.getText().toString());
        outState.putString("temperature", temperatureTextView.getText().toString());
        outState.putString("day", dayTextView.getText().toString());
        outState.putString("description", descriptionTextView.getText().toString());
        outState.putString("wind", windTextView.getText().toString());
        outState.putString("pressure", pressureTextView.getText().toString());
        outState.putString("humidity", humidityTextView.getText().toString());
        outState.putBoolean("IsDataUpdateRequired", isDataUpdateRequired);
        SingletonForImage.getInstance().setWeatherPicture(weatherPictureView);

//        Snackbar.make(view, "Сохранялка",
//                Snackbar.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), "сохранялка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setWeatherFromInternet(String description, String temp, String wind, //сюда приходят данные из интернета
                                       String pressure, String humidity, int weatherPicture,
                                       String dayText) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                temperatureTextView.setText(temp);
                windTextView.setText(wind);
                pressureTextView.setText(pressure);
                humidityTextView.setText(humidity);
                weatherPictureView.setImageResource(weatherPicture);
                dayTextView.setText(dayText);
            }
        });

    }

    private void init() {
        cityTextView = view.findViewById(R.id.city_0);
        temperatureTextView = view.findViewById(R.id.temperature_0);
        dayTextView = view.findViewById(R.id.date_0);
        weatherPictureView = view.findViewById(R.id.weather_picture_0);
        windTextView = view.findViewById(R.id.wind_0);
        pressureTextView = view.findViewById(R.id.pressure_0);
        humidityTextView = view.findViewById(R.id.humidity_0);
        descriptionTextView = view.findViewById(R.id.description_0);
        cityText = cityTextView.getText().toString();

        handler = new Handler();
    }
}