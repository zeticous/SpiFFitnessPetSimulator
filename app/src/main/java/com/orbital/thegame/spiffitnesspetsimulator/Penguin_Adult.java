package com.orbital.thegame.spiffitnesspetsimulator;

public class Penguin_Adult extends Spirit_Adult{
    private final int MINIMUM = 20;

    public Penguin_Adult() {
        super();
        startConstructor();
    }

    public Penguin_Adult(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        startConstructor();
    }

    private void startConstructor(){
        setAnimation_idle(R.drawable.penguin_adult_idle);
        setAnimation_happy(R.drawable.penguin_adult_happy);
        setName("Ica Wysp");
        setRegister(PENGUIN_ADULT_REG);
        setMinimumAffinity(MINIMUM);
    }
}
