package com.orbital.thegame.spiffitnesspetsimulator;

public class Panda_Adult extends Spirit_Adult {
    private final int MINIMUM = 20;

    public Panda_Adult() {
        super();
        startConstructor();
    }

    public Panda_Adult(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        startConstructor();
    }

    private void startConstructor(){
        setAnimation_idle(R.drawable.panda_adult_idle);
        setAnimation_happy(R.drawable.panda_adult_happy);
        setName("pandaAdult");
        setRegister(PANDA_ADULT_REG);
        setMinimumAffinity(MINIMUM);
    }


}
