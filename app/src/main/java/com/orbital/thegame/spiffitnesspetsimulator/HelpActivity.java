package com.orbital.thegame.spiffitnesspetsimulator;


import android.app.FragmentManager;
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
        Button gameDetailsBtn = (Button) findViewById(R.id.btnDetails);
        Button releaseDetailsBtn = (Button) findViewById(R.id.btnReleseDetails);

        assert navigationBtn != null;
        navigationBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                NavigationTutorial navFrag = new NavigationTutorial();
                navFrag.setRetainInstance(true);
                navFrag.show(fm, "fragment_name");
            }
        });
        assert gameDetailsBtn != null;
        gameDetailsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                GameDetailsTutorial gameDetailFrag = new GameDetailsTutorial();
                gameDetailFrag.setRetainInstance(true);
                gameDetailFrag.show(fm, "fragment_name");
            }
        });
        assert releaseDetailsBtn != null;
        releaseDetailsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ReleaseTutorial releaseDetailFrag = new ReleaseTutorial();
                releaseDetailFrag.setRetainInstance(true);
                releaseDetailFrag.show(fm, "fragment_name");
            }
        });
    }
}
