package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 13/6/2016.
 */

public class Panda_Adult extends Spirit_Adult {
    private int image_idle1 = R.drawable.panda_adult_idle1;
    private int image_idle2 = R.drawable.panda_adult_idle2;
    private int image_happy1 = R.drawable.panda_adult_happy1;
    private int image_happy2 = R.drawable.panda_adult_happy2;

    private int minimumAffinity = 50;

    public int getImage_idle1() {
        return image_idle1;
    }

    public int getImage_idle2() {
        return image_idle2;
    }

    public int getImage_happy2() {
        return image_happy2;
    }

    public int getImage_happy1() {
        return image_happy1;
    }


    public Panda_Adult(int affinityLevel) {
        super(affinityLevel);
    }

    public boolean check(int affinityLevel){
        return affinityLevel > minimumAffinity;
    }
}
