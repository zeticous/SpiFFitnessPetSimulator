package com.orbital.thegame.spiffitnesspetsimulator;


import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Button navigationBtn = (Button) findViewById(R.id.btnNavigation);
        Button babyStageTutBtn = (Button) findViewById(R.id.btnDetails);
        Button releaseTutBtn = (Button) findViewById(R.id.btnReleseDetails);

        assert navigationBtn != null;
        assert babyStageTutBtn != null;
        assert releaseTutBtn != null;

        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        babyStageTutBtn.setVisibility(View.GONE);
        releaseTutBtn.setVisibility(View.GONE);

        if(settings.getBoolean("tutorial2", false)){
            babyStageTutBtn.setVisibility(View.VISIBLE);
        }
        if(settings.getBoolean("tutorial3", false)){
            releaseTutBtn.setVisibility(View.VISIBLE);
        }

        navigationBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                NavigationTutorial navFrag = new NavigationTutorial();
                navFrag.setRetainInstance(true);
                navFrag.show(fm, "fragment_name");
            }
        });

        babyStageTutBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                BabyStageTutorial babyStageTutorial = new BabyStageTutorial();
                babyStageTutorial.setRetainInstance(true);
                babyStageTutorial.show(fm, "fragment_name");
            }
        });

        releaseTutBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ReleaseTutorial releaseTutorial = new ReleaseTutorial();
                releaseTutorial.setRetainInstance(true);
                releaseTutorial.show(fm, "fragment_name");
            }
        });
    }
}
