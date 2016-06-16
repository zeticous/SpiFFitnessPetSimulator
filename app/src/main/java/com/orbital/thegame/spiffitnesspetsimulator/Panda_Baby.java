package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Esmond on 9/6/2016.
 */
public class Panda_Baby extends Spirit_Baby {

    private int image_idle1 = R.drawable.panda_baby_idle1;
    private int image_idle2 = R.drawable.panda_baby_idle2;
    private int image_happy1 = R.drawable.panda_baby_happy1;
    private int image_happy2 = R.drawable.panda_baby_happy2;

    private int minimumAffinity = 30;

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

    public Panda_Baby(int affinityLevel) {
        super(affinityLevel);
    }

    @Override
    public Spirits evolveCheck(int affinityLevel) {
    //    Panda_Adult reference = new Panda_Adult(0);
    //    if (reference.check(affinityLevel)) {
    //        return new Panda_Adult(affinityLevel);
    //    }
        if (check(affinityLevel)) {
            return new Panda_Adult(affinityLevel);
        }
        return null;
    }

    public boolean check(int affinityLevel){
        return affinityLevel > minimumAffinity;
    }
}
