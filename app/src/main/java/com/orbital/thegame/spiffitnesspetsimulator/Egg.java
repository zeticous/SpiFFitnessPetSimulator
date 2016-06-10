package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Esmond on 9/6/2016.
 */
public class Egg extends Spirits{

    public int image_idle1 = R.drawable.egg_idle1;
    public int image_idle2 = R.drawable.egg_idle2;
    public int image_happy1 = R.drawable.egg_happy1;
    public int image_happy2 = R.drawable.egg_happy2;

    public Egg(Calendar cal){
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_WEEK, 3);
        setEndTime(cal.getTimeInMillis());
        setAffinityLevel(0);
    }

    public void evolveCheck(){
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        if (cal.getTimeInMillis() > getEndTime()){
            evolveBranch(getAffinityLevel());
        }
    }

    public void evolveBranch(int affinityLevel){
        if (Panda_Baby.check(affinityLevel)){
            MainActivity.UserSpirit = new Panda_Baby(affinityLevel);
        }
        else if (Penguin_Baby.check(affinityLevel)){
            MainActivity.UserSpirit = new Penguin_Baby(affinityLevel);
        }
        else {
            MainActivity.UserSpirit = new Pig_Baby(affinityLevel);
        }
    }
}
