package com.orbital.thegame.spiffitnesspetsimulator;

import java.util.Calendar;
import java.util.Date;

public class Spirits {

    public static final int EGG_REG = 10000;

    public static final int PIG_BABY_REG = 10001;
    public static final int PIG_ADULT_REG = 10002;

    public static final int PENGUIN_BABY_REG = 10003;
    public static final int PENGUIN_ADULT_REG = 10004;

    public static final int PANDA_BABY_REG = 10005;
    public static final int PANDA_ADULT_REG = 10006;

    private int image_idle1;
    private int image_idle2;
    private int image_happy1;
    private int image_happy2;
    
    public int getImage_happy1() {
        return image_happy1;
    }

    public void setImage_happy1(int image_happy1) {
        this.image_happy1 = image_happy1;
    }

    public int getImage_idle2() {
        return image_idle2;
    }

    public void setImage_idle2(int image_idle2) {
        this.image_idle2 = image_idle2;
    }

    public int getImage_idle1() {
        return image_idle1;
    }

    public void setImage_idle1(int image_idle1) {
        this.image_idle1 = image_idle1;
    }

    public int getImage_happy2() {
        return image_happy2;
    }

    public void setImage_happy2(int image_happy2) {
        this.image_happy2 = image_happy2;
    }

    protected int stepCount;
    protected int affinityLevel;
    protected int affinityPoint;
    protected long startTime, endTime;
    protected boolean isAdult = false;
    private int register = 99999;
    private int minimumAffinity;

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    protected String name;

    public Calendar cal;

    public Spirits(){
        setStepCount(0);
        setAffinityLevel(0);
        setAffinityPoint(0);
        this.cal = Calendar.getInstance();
        Date now = new Date();
        this.cal.setTime(now);
    }

    public Spirits(int stepCount, long startTime, long endTime, int affinityLevel){
        setStepCount(stepCount);
        setAffinityLevel(affinityLevel);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getMinimumAffinity() {
        return minimumAffinity;
    }

    public void setMinimumAffinity(int minimumAffinity) {
        this.minimumAffinity = minimumAffinity;
    }

    public boolean evolveCheck(int affinityLevel) {
        return false;
    }

    public Spirits evolve(int affinityLevel){
        return null;
    }

    public boolean check(int affinityLevel){
        return affinityLevel > getMinimumAffinity();
    }

    public Spirits initialise(){
        return new Egg();
    }

    public boolean runCheck(){
        return false;
    }
}
