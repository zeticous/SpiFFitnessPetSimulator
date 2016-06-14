package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 13/6/2016.
 */
public class Pig_Baby extends Spirit_Baby{
    private int image_idle1 = R.drawable.pig_baby_idle1;
    private int image_idle2 = R.drawable.pig_baby_idle2;
    private int image_happy1 = R.drawable.pig_baby_happy1;
    private int image_happy2 = R.drawable.pig_baby_happy2;

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

    private int minimumAffinity = 0;


    public Pig_Baby(int affinityLevel){
        super(affinityLevel);
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
