package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Esmond on 9/6/2016.
 */
public class Egg extends Spirits{

    public Egg(){
        super();
        egg_start();
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_WEEK, 3);
        setEndTime(cal.getTimeInMillis());
        setAffinityLevel(0);
    }

    public Egg(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        egg_start();
    }

    private void egg_start(){
        setImage_idle1(R.drawable.egg_idle1);
        setImage_idle2(R.drawable.egg_idle2);
        setImage_happy1(R.drawable.egg_happy1);
        setImage_happy2(R.drawable.egg_happy2);
        setRegister(EGG_REG);
        setName("Egg");
    }

    @Override
    public Spirits evolveCheck(int affinityLevel){
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        if (cal.getTimeInMillis() > getEndTime()){
            return evolve(affinityLevel);
        }
        return null;
    }

    public Spirits evolve(int affinityLevel){
        Spirits reference = new Panda_Baby(0);
        if (reference.check(affinityLevel)){
            return new Panda_Baby(affinityLevel);
        }
        reference = new Penguin_Baby(0);
        if (reference.check(affinityLevel)){
            return new Penguin_Baby(affinityLevel);
        }
        else {
            return new Pig_Baby(affinityLevel);
        }
    }
}
