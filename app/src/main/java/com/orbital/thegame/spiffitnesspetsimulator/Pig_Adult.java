package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 14/6/2016.
 */
public class Pig_Adult extends Spirit_Adult {

    private int minimumAffinity = 20;

    public Pig_Adult(int affinityLevel) {
        super(affinityLevel);
        setImage_idle1(R.drawable.pig_adult_idle1);
        setImage_idle2(R.drawable.pig_adult_idle2);
        setImage_happy1(R.drawable.pig_adult_happy1);
        setImage_happy2(R.drawable.pig_adult_happy2);
        setRegister(PIG_ADULT_REG);
    }
    public Pig_Adult(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        setRegister(PIG_ADULT_REG);
    }

    public boolean check(int affinityLevel){
        return affinityLevel > minimumAffinity;
    }
}
