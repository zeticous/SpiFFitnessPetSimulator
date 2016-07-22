package com.orbital.thegame.spiffitnesspetsimulator;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class firstLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        TextView textView = (TextView) findViewById(R.id.firstLaunch_textview);
        Typeface font = Typeface.createFromAsset(getAssets(), "DKBlueSheep.ttf");
        if (textView != null)
            textView.setTypeface(font);

        RelativeLayout button = (RelativeLayout) findViewById(R.id.continue_button);
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
