package com.orbital.thegame.spiffitnesspetsimulator;

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
        setImage_idle1(R.drawable.penguin_baby_idle1);
        setImage_idle2(R.drawable.penguin_baby_idle2);
        setImage_happy1(R.drawable.penguin_baby_happy1);
        setImage_happy2(R.drawable.penguin_baby_happy2);
        setName("penguinBaby");
        setRegister(PENGUIN_BABY_REG);
        setMinimumAffinity(MINIMUM);
    }

    @Override
    public Spirits evolve(int affinityLevel){
        return new Penguin_Adult();
    }
}
