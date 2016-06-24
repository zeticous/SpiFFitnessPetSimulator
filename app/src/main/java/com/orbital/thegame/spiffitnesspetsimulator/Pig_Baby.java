package com.orbital.thegame.spiffitnesspetsimulator;

import android.util.Log;

/**
 * Created by Qi Wei on 13/6/2016.
 */
public class Pig_Baby extends Spirit_Baby{

    private int MINIMUM = 0;

    public Pig_Baby(){
        super();
        startConstructor();
    }

    public Pig_Baby(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount, startTime, endTime, affinityLevel);
        startConstructor();
    }

    private void startConstructor(){
        setAnimation_idle(R.drawable.pig_baby_idle);
        setAnimation_happy(R.drawable.pig_baby_happy);
        setName("pigBaby");
        setRegister(PIG_BABY_REG);
        setMinimumAffinity(MINIMUM);
    }

    @Override
    public boolean evolveCheck(int affinityLevel) {
        Log.d("Evolve","Checking");
        Spirits reference = new Pig_Adult();
        if(reference.check(affinityLevel)) {
            return true;
        }
        return false;
    }

    @Override
    public Spirits evolve(int affinityLevel){
        return new Pig_Adult();
    }
}
