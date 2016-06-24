package com.orbital.thegame.spiffitnesspetsimulator;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class Egg extends Spirits{

    public Egg(){
        super();
        startConstructor();
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.MINUTE, 1);
        setEndTime(cal.getTimeInMillis());
    }

    public Egg(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        startConstructor();
    }

    private void startConstructor(){
        setImage_idle1(R.drawable.egg_idle1);
        setImage_idle2(R.drawable.egg_idle2);
        setImage_happy1(R.drawable.egg_happy1);
        setImage_happy2(R.drawable.egg_happy2);
        setRegister(EGG_REG);
        setName("Egg");
    }

    @Override
    public boolean evolveCheck(int affinityLevel){
        Log.d("EGG", "RUNNING EVOLVE CHECK");
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        if (cal.getTimeInMillis() > getEndTime()){
            return true;
        }
        return false;
    }

    @Override
    public Spirits evolve(int affinityLevel){
        Spirits reference = new Panda_Baby();
        if (reference.check(affinityLevel)){
            Log.d("Evolve","PandaBaby");
            return new Panda_Baby();
        }
        reference = new Penguin_Baby();
        if (reference.check(affinityLevel)){
            Log.d("Evolve","PenguinBaby");
            return new Penguin_Baby();
        }
        else {
            Log.d("Evolve","PigBaby");
            return new Pig_Baby();
        }
    }
}
