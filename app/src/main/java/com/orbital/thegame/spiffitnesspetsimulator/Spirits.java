package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;
import java.util.Date;

public class Spirits {

    public int image_idle1;
    public int image_idle2;
    public int image_happy1;
    public int image_happy2;

    private int affinityLevel;
    private long startTime, endTime;
    private static int minimumAffinity;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getAffinityLevel() {
        return affinityLevel;
    }

    public void setAffinityLevel(int affinityLevel) {
        this.affinityLevel = affinityLevel;
    }

    public static boolean check(int affinityLevel){
        return affinityLevel > minimumAffinity;
    }

    public static void initialise(){
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        MainActivity.UserSpirit = new Egg(cal);
    }
}
