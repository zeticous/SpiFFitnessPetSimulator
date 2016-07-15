package com.orbital.thegame.spiffitnesspetsimulator;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class Egg extends Spirits{

    public Egg(){
        super();
        startConstructor();
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.MINUTE, 5);
        setEndTime(cal.getTimeInMillis());
    }

    public Egg(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        startConstructor();
    }

    private void startConstructor(){
        setAnimation_idle(R.drawable.egg_idle);
        setAnimation_happy(R.drawable.egg_happy);
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
