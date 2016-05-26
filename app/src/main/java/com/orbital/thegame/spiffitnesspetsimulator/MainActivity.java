package com.orbital.thegame.spiffitnesspetsimulator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    public static int stepCount=0;
    public static int affinityPoint = 5;
    public static int affinityLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateAffinityImage(affinityPoint);

        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        ImageButton menu = (ImageButton) findViewById(R.id.menu);
        ImageButton help = (ImageButton) findViewById(R.id.help);
        ImageButton record = (ImageButton) findViewById(R.id.record);

        final TextView levelCount = (TextView) findViewById(R.id.level_count);

        sprite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (affinityPoint > 0) {
                    affinityLevel++;
                    affinityPoint--;
                }
                updateAffinityImage(affinityPoint);
                changeText(levelCount, "" + affinityLevel);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RecordsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateAffinityImage(int affinityPoint){
        ImageView heart01 = (ImageView) findViewById(R.id.heart01);
        ImageView heart02 = (ImageView) findViewById(R.id.heart02);
        ImageView heart03 = (ImageView) findViewById(R.id.heart03);
        ImageView heart04 = (ImageView) findViewById(R.id.heart04);
        ImageView heart05 = (ImageView) findViewById(R.id.heart05);

        switch(affinityPoint){
            case 5:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_fullheart);
                changeImage(heart03,R.drawable.view_fullheart);
                changeImage(heart04,R.drawable.view_fullheart);
                changeImage(heart05,R.drawable.view_fullheart);
                break;
            case 4:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_fullheart);
                changeImage(heart03,R.drawable.view_fullheart);
                changeImage(heart04,R.drawable.view_fullheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
            case 3:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_fullheart);
                changeImage(heart03,R.drawable.view_fullheart);
                changeImage(heart04,R.drawable.view_emptyheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
            case 2:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_fullheart);
                changeImage(heart03,R.drawable.view_emptyheart);
                changeImage(heart04,R.drawable.view_emptyheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
            case 1:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_emptyheart);
                changeImage(heart03,R.drawable.view_emptyheart);
                changeImage(heart04,R.drawable.view_emptyheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
            case 0:
                changeImage(heart01,R.drawable.view_emptyheart);
                changeImage(heart02,R.drawable.view_emptyheart);
                changeImage(heart03,R.drawable.view_emptyheart);
                changeImage(heart04,R.drawable.view_emptyheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
        }
    }

    private void changeImage(ImageView view, int drawable){
        view.setImageResource(drawable);
    }

    private void changeText(TextView view, String string){
        view.setText(string);
    }
}
