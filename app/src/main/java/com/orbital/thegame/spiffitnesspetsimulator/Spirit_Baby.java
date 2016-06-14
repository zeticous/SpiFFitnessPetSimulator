package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Qi Wei on 14/6/2016.
 */
public class Spirit_Baby extends Spirits {
    public Spirit_Baby(int affinityLevel) {
        super();
        setAffinityLevel(affinityLevel);
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

    public Spirits runAway() {
        return new Egg();
    }
}
