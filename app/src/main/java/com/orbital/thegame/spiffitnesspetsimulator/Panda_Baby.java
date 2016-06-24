package com.orbital.thegame.spiffitnesspetsimulator;

import android.util.Log;

/**
 * Created by Esmond on 9/6/2016.
 */
public class Panda_Baby extends Spirit_Baby {
    private final int MINIMUM = 30;

    public Panda_Baby() {
        super();
        startConstructor();
    }

    public Panda_Baby(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount, startTime, endTime, affinityLevel);
        startConstructor();
    }

    private void startConstructor(){
        setAnimation_idle(R.drawable.panda_baby_idle);
        setAnimation_happy(R.drawable.panda_baby_happy);
        setName("pandaBaby");
        setRegister(PANDA_BABY_REG);
        setMinimumAffinity(MINIMUM);
    }

    @Override
    public boolean evolveCheck(int affinityLevel) {
        Log.d("Evolve","Checking");
        Spirits reference = new Panda_Adult();
        if(reference.check(affinityLevel)) {
            return true;
        }
        return false;
    }

    @Override
    public Spirits evolve(int affinityLevel){
        return new Panda_Adult();
    }
}
