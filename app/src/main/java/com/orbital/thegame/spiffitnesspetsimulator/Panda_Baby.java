package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Esmond on 9/6/2016.
 */
public class Panda_Baby extends Spirit_Baby {

    private int minimumAffinity = 30;

    public Panda_Baby() {
        super();
        setImage_idle1(R.drawable.panda_baby_idle1);
        setImage_idle2(R.drawable.panda_baby_idle2);
        setImage_happy1(R.drawable.panda_baby_happy1);
        setImage_happy2(R.drawable.panda_baby_happy2);
        setName("pandaBaby");
        setRegister(PANDA_BABY_REG);
    }

    public Panda_Baby(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount, startTime, endTime, affinityLevel);
        setRegister(PANDA_BABY_REG);
    }

    @Override
    public Spirits evolveCheck(int affinityLevel) {
        if (check(affinityLevel)) {
            return new Panda_Adult();
        }
        return null;
    }
}
