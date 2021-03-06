package com.example.androidlesson1.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlesson1.R;

public class BaseActivity extends AppCompatActivity {
    private static final String NAME_SHARED_PREFERENCE = "LOGIN";
    private static final String IS_LIGHT_THEME = "IS_LIGHT_THEME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLightTheme()) {
            setTheme(R.style.AppLightTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    protected boolean isLightTheme() {
        SharedPreferences sharedPref = getSharedPreferences(NAME_SHARED_PREFERENCE, MODE_PRIVATE);
        return sharedPref.getBoolean(IS_LIGHT_THEME, true);
    }

    protected void setLightTheme (boolean isLightTheme) {
        SharedPreferences sharedPref = getSharedPreferences(NAME_SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IS_LIGHT_THEME, isLightTheme);
        editor.apply();
    }
}
