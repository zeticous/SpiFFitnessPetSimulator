package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;


public class MainActivity extends AppCompatActivity{

    private JSONSerializer mSerializer;
    public static final int FACTOR = 1000;
    public static final int REQUEST_OAUTH = 1337;
    public final static String TAG = "GoogleFitService";
    int stepCount = 0;
    int affinityLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSerializer = new JSONSerializer("Spirits.json", MainActivity.this.getApplicationContext());

        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        if (settings.getBoolean("firstLaunch", true)) {
            Log.d("Settings", "First Launch Detected");
            GameService.UserSpirit = new Egg();

            if (GameService.UserSpirit != null) {
                Log.e("FirstLaunch", "UserSpirit successfully created");
                saveSpirits();
            }

            settings.edit().putBoolean("firstLaunch", false).apply();
        } else {
            loadSpirits();
        }

        Spirits SpiritInstance = GameService.UserSpirit;
        stepCount = SpiritInstance.getStepCount();
        affinityLevel = SpiritInstance.getAffinityLevel();

        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        ImageButton menu = (ImageButton) findViewById(R.id.menu);
        ImageButton help = (ImageButton) findViewById(R.id.help);
        ImageButton record = (ImageButton) findViewById(R.id.record);

        final TextView levelCount = (TextView) findViewById(R.id.level_count);

        updateAffinityPoint(stepCount, affinityLevel);

        changeImage(sprite, SpiritInstance.image_idle1);

        sprite.setOnClickListener(new View.OnClickListener() {
            int stepCount = GameService.UserSpirit.getStepCount();
            int affinityLevel = GameService.UserSpirit.getAffinityLevel();

            public void onClick(View v) {
                if ((stepCount - affinityLevel * FACTOR) > FACTOR) {
                    affinityLevel++;
                }
                GameService.UserSpirit.setAffinityLevel(affinityLevel);
                changeText(levelCount, "" + affinityLevel);

                updateAffinityPoint(stepCount, affinityLevel);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });

        Intent i = new Intent(this, GameService.class);
        startService(i);
    }

    private BroadcastReceiver ResolutionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ResolutionReceiver", "Checking connection:");
            if (intent.hasExtra("REQUEST_RESOLUTION")){
                PendingIntent pendingIntent = intent.getParcelableExtra("PENDING_INTENT");
                int errorCode = intent.getIntExtra("ERROR_CODE", 1337);
                ConnectionResult result = new ConnectionResult(errorCode,pendingIntent);
                handleResolution(result);
            }
        }
    };

    private void handleResolution(ConnectionResult result){
        try{
            result.startResolutionForResult(MainActivity.this, REQUEST_OAUTH);
        }catch(IntentSender.SendIntentException e){
            Log.e(TAG, "Activity thread Google Fit Exception while starting resolution activity", e);
        }
    }

    private void updateAffinityPoint(int stepCount, int affinityLevel){
        int affinityPoint = (stepCount - FACTOR*affinityLevel)/FACTOR;
        TextView txt = (TextView) findViewById(R.id.experience);

        changeText(txt, ""+affinityPoint);
    }

    private void changeImage(ImageView view, int drawable){
        view.setImageResource(drawable);
    }

    private void changeText(TextView view, String string){
        view.setText(string);
    }

    public void saveSpirits(){
        try{
            mSerializer.save(GameService.UserSpirit);
            Log.e("JSON", "Saved successfully");
        }catch(Exception e){
            Log.e("JSON", "Error Saving notes");
        }
    }

    public void loadSpirits(){
        mSerializer = new JSONSerializer("Spirits.json", MainActivity.this.getApplicationContext());
        try{
            GameService.UserSpirit = mSerializer.load();
            if (GameService.UserSpirit != null){
                Log.e("JSON", "Successfully load");
            }
        }catch(Exception e) {
            Log.e("JSON", "ERROR LOADING SPIRITS");
        }
    }
}
