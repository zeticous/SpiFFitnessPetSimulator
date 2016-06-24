package com.orbital.thegame.spiffitnesspetsimulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView expCount = (TextView) findViewById(R.id.maxExp);
        TextView speciesName = (TextView) findViewById(R.id.species_name);
        ImageView menu_icon = (ImageView) findViewById(R.id.spiritIcon);
        Button releaseButton = (Button) findViewById(R.id.release_button);


        assert expCount != null;
        assert speciesName != null;
        assert menu_icon != null;
        assert releaseButton != null;

        if (!GameService.UserSpirit.isAdult){
            releaseButton.setVisibility(View.GONE);
        }

        releaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameService.UserSpirit = GameService.UserSpirit.initialise();
            }
        });

        String TEXT = ""+ GameService.UserSpirit.getStepCount();
        expCount.setText(TEXT);

        speciesName.setText(GameService.UserSpirit.getName());
        menu_icon.setImageResource(GameService.UserSpirit.getImage_idle1());
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
