package com.orbital.thegame.spiffitnesspetsimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class firstLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        Button button = (Button) findViewById(R.id.continue_button);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(firstLaunchActivity.this, connectActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
