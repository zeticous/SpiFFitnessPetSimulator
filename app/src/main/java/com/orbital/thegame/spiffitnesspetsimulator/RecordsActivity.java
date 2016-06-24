package com.orbital.thegame.spiffitnesspetsimulator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        ImageView babyPig = (ImageView) findViewById(R.id.baby_gallery_pig);
        ImageView babyPenguin = (ImageView) findViewById(R.id.baby_gallery_penguin);
        ImageView babyPanda = (ImageView) findViewById(R.id.baby_gallery_panda);
        ImageView adultPig = (ImageView) findViewById(R.id.adult_gallery_pig);
        ImageView adultPenguin = (ImageView) findViewById(R.id.adult_gallery_penguin);
        ImageView adultPanda = (ImageView) findViewById(R.id.adult_gallery_panda);

        assert babyPig != null;
        assert babyPenguin != null;
        assert babyPanda != null;
        assert adultPig != null;
        assert adultPenguin != null;
        assert adultPanda != null;

        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        if (!settings.getBoolean("pigBaby", false)){
            babyPig.setVisibility(View.GONE);
        }
        if (!settings.getBoolean("penguinBaby", false)){
            babyPenguin.setVisibility(View.GONE);
        }
        if (!settings.getBoolean("pandaBaby", false)){
            babyPanda.setVisibility(View.GONE);
        }
        if (!settings.getBoolean("pigAdult", false)){
            adultPig.setVisibility(View.GONE);
        }
        if (!settings.getBoolean("penguinAdult", false)){
            adultPenguin.setVisibility(View.GONE);
        }
        if (!settings.getBoolean("pandaAdult", false)){
            adultPanda.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
