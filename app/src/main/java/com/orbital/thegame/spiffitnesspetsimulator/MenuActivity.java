package com.orbital.thegame.spiffitnesspetsimulator;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

public class MenuActivity extends AppCompatActivity {
    public final static String TAG = "GoogleFitService";
    private static final int REQUEST_BLUETOOTH = 1001;
    GoogleApiClient mBluetoothClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView expCount = (TextView) findViewById(R.id.maxExp);
        TextView speciesName = (TextView) findViewById(R.id.species_name);
        Button releaseButton = (Button) findViewById(R.id.release_button);
        Switch bluetoothSwitch = (Switch) findViewById(R.id.bluetooth_switch);

        assert expCount != null;
        assert speciesName != null;
        assert releaseButton != null;
        assert bluetoothSwitch != null;

        if (!GameService.UserSpirit.isAdult){
            releaseButton.setVisibility(View.GONE);
        }

        releaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameService.UserSpirit = GameService.UserSpirit.initialise();
                finish();
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
    }
}
