package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 13/6/2016.
 */
public class Penguin_Baby extends Spirit_Baby {

    public Penguin_Baby(){
       super();
        setImage_idle1(R.drawable.penguin_baby_idle1);
        setImage_idle2(R.drawable.penguin_baby_idle2);
        setImage_happy1(R.drawable.penguin_baby_happy1);
        setImage_happy2(R.drawable.penguin_baby_happy2);
        setName("penguinBaby");
        setRegister(PENGUIN_BABY_REG);
        setMinimumAffinity(20);
    }
    public Penguin_Baby(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount, startTime, endTime, affinityLevel);
        setRegister(PENGUIN_BABY_REG);
    }

    public Spirits evolveCheck(int affinityLevel) {
        if (check(affinityLevel)) {
            return new Penguin_Adult();
        }
        return null;
    }
}
