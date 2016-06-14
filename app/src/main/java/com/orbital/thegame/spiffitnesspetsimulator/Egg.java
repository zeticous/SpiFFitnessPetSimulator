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

    public Egg(){
        super();
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_WEEK, 3);
        setEndTime(cal.getTimeInMillis());
        setAffinityLevel(0);
    }

    public Spirits evolveCheck(){
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        if (cal.getTimeInMillis() > getEndTime()){
            return evolve(getAffinityLevel());
        }
        return null;
    }

    public Spirits evolve(int affinityLevel){
        if (Panda_Baby.check(affinityLevel)){
            return new Panda_Baby(affinityLevel);
        }
        else if (Penguin_Baby.check(affinityLevel)){
            return new Penguin_Baby(affinityLevel);
        }
        else {
            return new Pig_Baby(affinityLevel);
        }
    }
}
