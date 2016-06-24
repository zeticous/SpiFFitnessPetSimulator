package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 14/6/2016.
 */
public class Pig_Adult extends Spirit_Adult {

    private int MINIMUM = 20;

    public Pig_Adult() {
        super();
        startConstructor();
    }
    public Pig_Adult(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        startConstructor();
    }

    private void startConstructor(){
        setImage_idle1(R.drawable.pig_adult_idle1);
        setImage_idle2(R.drawable.pig_adult_idle2);
        setImage_happy1(R.drawable.pig_adult_happy1);
        setImage_happy2(R.drawable.pig_adult_happy2);
        setName("pigAdult");
        setRegister(PIG_ADULT_REG);
        setMinimumAffinity(MINIMUM);
    }
}
