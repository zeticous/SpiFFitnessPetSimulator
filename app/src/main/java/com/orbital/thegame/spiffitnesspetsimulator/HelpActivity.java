package com.orbital.thegame.spiffitnesspetsimulator;


import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        RelativeLayout navigationBtn = (RelativeLayout) findViewById(R.id.btnNavigation);
        RelativeLayout babyStageTutBtn = (RelativeLayout) findViewById(R.id.btnDetails);
        RelativeLayout releaseTutBtn = (RelativeLayout) findViewById(R.id.btnReleseDetails);

        fontSetUp();

        assert navigationBtn != null;
        assert babyStageTutBtn != null;
        assert releaseTutBtn != null;

        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        babyStageTutBtn.setVisibility(View.INVISIBLE);
        releaseTutBtn.setVisibility(View.INVISIBLE);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void fontSetUp(){
        TextView header = (TextView) findViewById(R.id.help_header);
        TextView basics = (TextView) findViewById(R.id.basics);
        TextView firstBaby = (TextView) findViewById(R.id.firstBaby);
        TextView releaseSpirit = (TextView) findViewById(R.id.releaseSpirit);

        Typeface font = Typeface.createFromAsset(getAssets(), "DKBlueSheep.ttf");

        assert header!=null;
        assert basics !=null;
        assert firstBaby !=null;
        assert releaseSpirit!=null;

        header.setTypeface(font);
        basics.setTypeface(font);
        firstBaby.setTypeface(font);
        releaseSpirit.setTypeface(font);
    }
}
