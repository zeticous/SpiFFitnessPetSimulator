package com.orbital.thegame.spiffitnesspetsimulator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class launcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);

        if (settings.getBoolean("firstLaunch", true)) {
            Log.d("Settings", "First Launch Detected");
            GameService.UserSpirit = new Egg();

            if (GameService.UserSpirit.getRegister() == Spirits.EGG_REG) {
                Log.e("FirstLaunch", "UserSpirit successfully created");
                saveSpirits();
            }
            Intent intent = new Intent(launcherActivity.this, firstLaunchActivity.class);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(launcherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public static final String MY_PREFS_NAME = "GameSaveFile";
    public final static String PTAG = "SharedPref";

    public void saveSpirits() {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("spiritName", GameService.UserSpirit.getName());
        editor.putInt("stepCount", GameService.UserSpirit.getStepCount());

        editor.putLong("startTime", GameService.UserSpirit.getStartTime());
        editor.putLong("endTime", GameService.UserSpirit.getEndTime());

        editor.apply();
        Log.d(PTAG, "Shared Preference saved");
    }
}
