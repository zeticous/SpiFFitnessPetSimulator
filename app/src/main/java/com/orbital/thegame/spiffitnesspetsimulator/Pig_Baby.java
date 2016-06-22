package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 13/6/2016.
 */
public class Pig_Baby extends Spirit_Baby{

    private int minimumAffinity = 0;

    public Pig_Baby(int affinityLevel){
        super(affinityLevel);
        setImage_idle1(R.drawable.pig_baby_idle1);
        setImage_idle2(R.drawable.pig_baby_idle2);
        setImage_happy1(R.drawable.pig_baby_happy1);
        setImage_happy2(R.drawable.pig_baby_happy2);
        setName("pigBaby");
        setRegister(PIG_BABY_REG);
    }

    public Pig_Baby(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount, startTime, endTime, affinityLevel);
        setRegister(PIG_BABY_REG);
    }

    public Spirits evolveCheck(int affinityLevel) {
        if (check(affinityLevel)) {
            return new Pig_Adult(affinityLevel);
        }
        return null;
    }

    public boolean check(int affinityLevel){
        return affinityLevel > minimumAffinity;
    }

}
