package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 13/6/2016.
 */
public class Penguin_Baby extends Spirit_Baby {
    private int image_idle1 = R.drawable.penguin_baby_idle1;
    private int image_idle2 = R.drawable.penguin_baby_idle2;
    private int image_happy1 = R.drawable.penguin_baby_happy1;
    private int image_happy2 = R.drawable.penguin_baby_happy2;

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

    private int minimumAffinity = 20;


    public Penguin_Baby(int affinityLevel){
       super(affinityLevel);
    }

    public Spirits evolveCheck(int affinityLevel) {
        if (check(affinityLevel)) {
            return new Penguin_Adult(affinityLevel);
        }
        return null;
    }

    public boolean check(int affinityLevel){
        return affinityLevel > minimumAffinity;
    }




}
