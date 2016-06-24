package com.orbital.thegame.spiffitnesspetsimulator;

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
        setImage_idle1(R.drawable.pig_baby_idle1);
        setImage_idle2(R.drawable.pig_baby_idle2);
        setImage_happy1(R.drawable.pig_baby_happy1);
        setImage_happy2(R.drawable.pig_baby_happy2);
        setName("pigBaby");
        setRegister(PIG_BABY_REG);
        setMinimumAffinity(MINIMUM);
    }

    @Override
    public Spirits evolve(int affinityLevel){
        return new Pig_Adult();
    }
}
