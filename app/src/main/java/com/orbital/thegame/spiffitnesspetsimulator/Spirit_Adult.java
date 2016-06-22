package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;

/**
 * Created by Qi Wei on 14/6/2016.
 */
public class Spirit_Adult extends Spirits {

    public Spirit_Adult() {
        super();
        isAdult = true;
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_WEEK, 99);
        setEndTime(cal.getTimeInMillis());
    }

    public Spirit_Adult(int stepCount, long startTime, long endTime, int affinityLevel){
        super(stepCount,startTime,endTime,affinityLevel);
        setStartTime(startTime);
        setEndTime(endTime);
        setAffinityLevel(affinityLevel);
        setStepCount(stepCount);
    }

    public Spirits releaseSpirit(){
        return new Egg();
    }
}
