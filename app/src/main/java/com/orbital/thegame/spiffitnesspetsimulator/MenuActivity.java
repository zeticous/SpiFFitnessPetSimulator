package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView expCount = (TextView) findViewById(R.id.maxExp);
        TextView speciesName = (TextView) findViewById(R.id.species_name);
        TextView releaseButton = (TextView) findViewById(R.id.release_button);
        TextView collections = (TextView) findViewById(R.id.collectionsHeader);

        Typeface font = Typeface.createFromAsset(getAssets(), "DKBlueSheep.ttf");

        handleCollections();

        assert expCount != null;
        assert speciesName != null;
        assert releaseButton != null;
        assert collections != null;

        expCount.setTypeface(font);
        speciesName.setTypeface(font);
        collections.setTypeface(font);

        if (!GameService.UserSpirit.isAdult){
            releaseButton.setVisibility(View.GONE);
        }

        releaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                releaseButtonDialog releaseDialog = new releaseButtonDialog();
                releaseDialog.setRetainInstance(true);
                releaseDialog.show(fm, "fragment_name");
            }
        });

        String TEXT = ""+ GameService.UserSpirit.getStepCount();
        expCount.setText(TEXT);

        speciesName.setText(GameService.UserSpirit.getName());
    }

    @Override
    public void onResume(){
        super.onResume();
        startIdleAnimation();

        TextView expCount = (TextView) findViewById(R.id.maxExp);

        String TEXT = ""+ GameService.UserSpirit.getStepCount();
        expCount.setText(TEXT);
    }

    private void startIdleAnimation(){
        ImageView menu_icon = (ImageView) findViewById(R.id.spiritIcon);
        assert menu_icon != null;
        menu_icon.setImageResource(GameService.UserSpirit.getAnimation_idle());

        AnimationDrawable animation_idle = (AnimationDrawable) menu_icon.getDrawable();
        animation_idle.start();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void handleCollections(){
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
            babyPig.setImageResource(R.drawable.unknown);
        }
        if (!settings.getBoolean("penguinBaby", false)){
            babyPenguin.setImageResource(R.drawable.unknown);
        }
        if (!settings.getBoolean("pandaBaby", false)){
            babyPanda.setImageResource(R.drawable.unknown);
        }
        if (!settings.getBoolean("pigAdult", false)){
            adultPig.setImageResource(R.drawable.unknown);
        }
        if (!settings.getBoolean("penguinAdult", false)){
            adultPenguin.setImageResource(R.drawable.unknown);
        }
        if (!settings.getBoolean("pandaAdult", false)){
            adultPanda.setImageResource(R.drawable.unknown);
        }
    }
}
