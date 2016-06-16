package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;
import java.util.Date;

public class Spirits {

    public int image_idle1;
    public int image_idle2;
    public int image_happy1;
    public int image_happy2;

    protected int stepCount;
    protected int affinityLevel;
    protected int affinityPoint;
    protected long startTime, endTime;
    protected boolean isAdult = false;

    private JSONSerializer mSerializer;

    protected Calendar cal;

    public Spirits(){
        this.cal = Calendar.getInstance();
        Date now = new Date();
        this.cal.setTime(now);
        setStepCount(0);
        setAffinityPoint(0);
    }

    public int getAffinityPoint() {
        return affinityPoint;
    }

    public void setAffinityPoint(int affinityPoint) {
        this.affinityPoint = affinityPoint;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

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

    public Spirits evolveCheck(int affinityLevel) {
        return null;
    }

    public boolean check(int affinityLevel){
        return false;
    }
}
