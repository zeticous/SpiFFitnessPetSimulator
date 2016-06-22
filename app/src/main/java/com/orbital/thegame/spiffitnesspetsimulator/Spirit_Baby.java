package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;
import java.util.Date;

public class Spirit_Baby extends Spirits {
    public Spirit_Baby() {
        super();
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        setEndTime(cal.getTimeInMillis());
    }

    public Spirit_Baby(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        setStartTime(startTime);
        setEndTime(endTime);
        setAffinityLevel(affinityLevel);
        setStepCount(stepCount);
    }

    public void runCheck() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        if (cal.getTimeInMillis() > getEndTime()) {
            runAway();
        }
    }
    public Spirits runAway() {
        return new Egg();
    }
}
