package com.orbital.thegame.spiffitnesspetsimulator;

import android.util.Log;

/**
 * Created by Qi Wei on 13/6/2016.
 */
public class Penguin_Baby extends Spirit_Baby {
    private final int MINIMUM = 20;

    public Penguin_Baby(){
       super();
        startConstructor();
    }

    public Penguin_Baby(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount, startTime, endTime, affinityLevel);
        startConstructor();
    }

    private void startConstructor(){
        setAnimation_idle(R.drawable.penguin_baby_idle);
        setAnimation_happy(R.drawable.penguin_baby_happy);
        setName("Ica");
        setRegister(PENGUIN_BABY_REG);
        setMinimumAffinity(MINIMUM);
    }

    @Override
    public boolean evolveCheck(int affinityLevel) {
        Log.d("Evolve","Checking");
        Spirits reference = new Penguin_Adult();
        if(reference.check(affinityLevel)) {
            return true;
        }
        return false;
    }

    @Override
    public Spirits evolve(int affinityLevel){
        return new Penguin_Adult();
    }
}
