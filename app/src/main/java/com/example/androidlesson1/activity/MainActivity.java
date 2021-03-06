package com.example.androidlesson1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.androidlesson1.Constants;
import com.example.androidlesson1.R;
import com.example.androidlesson1.singletons.SingletonForHistoryOfCities;
import com.example.androidlesson1.workingWithFragments.FragmentBottom;
import com.example.androidlesson1.workingWithFragments.FragmentTop;
import com.example.androidlesson1.workingWithFragments.Publisher;
import com.example.androidlesson1.workingWithFragments.PublisherGetter;


public class MainActivity extends BaseActivity implements Constants, PublisherGetter {

    private final int REQUEST_SETTINGS_ACTIVITY = 1;
    private final int REQUEST_HISTORY_OF_CITIES_ACTIVITY = 1;

    private FragmentTop fragmentTop;
    private FragmentBottom fragmentBottom;
    private int orientation;
    private SharedPreferences sharedPreferences;
    private Publisher publisher = new Publisher();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_SHORT).show();

        if (savedInstanceState != null) {
            fragmentTop = (FragmentTop) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "FragmentTop");
            fragmentBottom = (FragmentBottom) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "FragmentBottom");
        } else {
            fragmentTop = FragmentTop.create(); //фрагменты создаются заново, в них подкачиваются новые данные
//            fragmentTop = new FragmentTop();
            fragmentBottom = FragmentBottom.create();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.add(R.id.container_top, fragmentTop);
            fragmentTransaction.add(R.id.container_bottom, fragmentBottom);
            fragmentTransaction.commit();
        }
        publisher.addSubscriber(fragmentTop);
        orientationDetermination(); //определение ориентации экрана
        setBackgroundImage(orientation); //установка фона экрана
    }


    @Override
    public Publisher getPublisher() {
        return publisher;
    }

    public void orientationDetermination() {
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            orientation = 1;
        }
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = 2;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //обработка нажатий на пункты меню
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case (R.id.menu_settings):
                intent = new Intent(getApplicationContext(), SettingsActivity.class); //создание интента для окна настроек
                startActivityForResult(intent, REQUEST_SETTINGS_ACTIVITY); //отправить интент и указать константу окна-отрпавителя
                break;
            case (R.id.menu_history_of_cities):
                intent = new Intent(getApplicationContext(), HistoryOfCitiesActivity.class); //создание интента для окна настроек
                startActivityForResult(intent, REQUEST_HISTORY_OF_CITIES_ACTIVITY); //отправить интент и указать константу окна-отрпавителя
                break;
        }

/*        if (id == R.id.menu_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class); //создание интента для окна настроек
            startActivityForResult(intent, REQUEST_HISTORY_OF_CITIES_ACTIVITY); //отправить интент и указать константу окна-отрпавителя
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) { //вызывается при повроте экрана
        super.onConfigurationChanged(newConfig);

        orientation = newConfig.orientation;
        setBackgroundImage(newConfig.orientation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //сюда приходит результат из настроек

        if (requestCode == REQUEST_SETTINGS_ACTIVITY && resultCode == RESULT_OK) { //проверка, что окно отработало нормально

            String newCity = data.getStringExtra(CITY);
            if (newCity.replaceAll("\\s+", "").equals("")) { //если в настройках не был выбран город или введна пустота
                newCity = getString(R.string.city_1); //по умолчанию ставится Москва
            }
            fragmentTop.updateCity(newCity);
            fragmentTop.setDataUpdateRequired(true); //выбран новый город, поэтому требуется обнолвение данных

            fragmentBottom.updateCity(newCity);
            fragmentBottom.setDataUpdateRequired(true); //выбран новый город, поэтому требуется обнолвение данных

            SingletonForHistoryOfCities.getInstance().addCityInHistory(newCity); //добавление города в историю просмотренных городов

            recreate();
        }

        super.onActivityResult(requestCode, resultCode, data);
        return;
    }


    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
//        setBackgroundImage(orientation);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "FragmentTop", fragmentTop);
        getSupportFragmentManager().putFragment(outState, "FragmentBottom", fragmentBottom);

    }

    private void setBackgroundImage(final int orientation) { //установка фона активити
/*        ConstraintLayout constraintLayout;
        constraintLayout = findViewById(R.id.constraint_layout);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            constraintLayout.setBackgroundResource(R.drawable.fon_portrait);
            constraintLayout.setBackgroundResource(R.drawable.test_fon3);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintLayout.setBackgroundResource(R.drawable.fon_landscape);
        }*/
    }

    private void init() {

    }
}

