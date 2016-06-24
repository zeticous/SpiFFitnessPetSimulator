package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;
import java.util.Date;

public class Spirit_Adult extends Spirits {

    public Spirit_Adult() {
        super();
        isAdult = true;
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        setEndTime(cal.getTimeInMillis());
    }

    public Spirit_Adult(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        setStartTime(startTime);
        setEndTime(endTime);
        setAffinityLevel(affinityLevel);
        setStepCount(stepCount);
    }

    @Override
    public boolean runCheck() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        if (cal.getTimeInMillis() > getEndTime()) {
            return true;
        }
        return false;
    }
}
