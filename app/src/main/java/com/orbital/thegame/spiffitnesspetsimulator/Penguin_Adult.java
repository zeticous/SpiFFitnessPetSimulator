package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 14/6/2016.
 */
public class Penguin_Adult extends Spirit_Adult{
    public Penguin_Adult() {
        super();
        setImage_idle1(R.drawable.penguin_adult_idle1);
        setImage_idle2(R.drawable.penguin_adult_idle2);
        setImage_happy1(R.drawable.penguin_adult_happy1);
        setImage_happy2(R.drawable.penguin_adult_happy2);
        setRegister(PENGUIN_ADULT_REG);
        setName("penguinAdult");
        setMinimumAffinity(40);
    }

    public Penguin_Adult(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        setRegister(PENGUIN_ADULT_REG);
    }
}
