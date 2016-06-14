package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;

/**
 * Created by Qi Wei on 14/6/2016.
 */
public class Spirit_Adult extends Spirits {

    public Spirit_Adult(int affinityLevel) {
        super();
        isAdult = true;
        setAffinityLevel(affinityLevel);
        setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_WEEK, 99);
        setEndTime(cal.getTimeInMillis());
    }

    public Spirits releaseSpirit(){
        return new Egg();
    }
}
