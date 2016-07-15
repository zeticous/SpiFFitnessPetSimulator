package com.orbital.thegame.spiffitnesspetsimulator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class welcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        settings.edit().putBoolean("firstLaunch", false).apply();

        Button button = (Button) findViewById(R.id.start_button);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
