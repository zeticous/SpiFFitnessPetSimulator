package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Esmond on 9/6/2016.
 */
public class Panda_Baby extends Spirits {

    private static int minimumAffinity = 30;

    private int image_idle1 = R.drawable.panda_baby_idle1;
    private int image_idle2 = R.drawable.panda_baby_idle2;
    private int image_happy1 = R.drawable.panda_baby_happy1;
    private int image_happy2 = R.drawable.panda_baby_happy2;


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
        setAffinityLevel(affinityLevel);
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        setEndTime(cal.getTimeInMillis());
    }

    public void runCheck() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        if (cal.getTimeInMillis() > getEndTime()) {
            runAway();
        }
    }

    public void runAway() {
        Spirits.initialise();
    }

    public void evolve(int affinityLevel) {
        if (Panda_Adult.check(affinityLevel)) {
            MainActivity.UserSpirit = new Panda_Baby(affinityLevel);
        }
    }
}
