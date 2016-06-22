package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 13/6/2016.
 */

public class Panda_Adult extends Spirit_Adult {

    private int minimumAffinity = 50;

    public Panda_Adult(int affinityLevel) {
        super(affinityLevel);
        setImage_idle1(R.drawable.panda_adult_idle1);
        setImage_idle2(R.drawable.panda_adult_idle2);
        setImage_happy1(R.drawable.panda_adult_happy1);
        setImage_happy2(R.drawable.panda_adult_happy2);
        setRegister(PANDA_ADULT_REG);
    }

    public Panda_Adult(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        setRegister(PANDA_ADULT_REG);
    }

    public boolean check(int affinityLevel){
        return affinityLevel > minimumAffinity;
    }
}
