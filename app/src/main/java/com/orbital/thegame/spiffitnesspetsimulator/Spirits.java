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


    private int animation_idle;
    private int animation_happy;

    public int getAnimation_idle() {
        return animation_idle;
    }

    public void setAnimation_idle(int animation_idle) {
        this.animation_idle = animation_idle;
    }

    public int getAnimation_happy() {
        return animation_happy;
    }

    public void setAnimation_happy(int animation_happy) {
        this.animation_happy = animation_happy;
    }

    protected boolean firstBaby = false;
    protected boolean firstAdult = false;

    public boolean isFirstBaby() {
        return firstBaby;
    }

    public void setFirstBaby(boolean firstBaby) {
        this.firstBaby = firstBaby;
    }

    public boolean isFirstAdult() {
        return firstAdult;
    }

    public void setFirstAdult(boolean firstAdult) {
        this.firstAdult = firstAdult;
    }

    protected boolean justEvolved = false;

    public boolean isJustEvolved() {
        return justEvolved;
    }

    public void setJustEvolved(boolean justEvolved) {
        this.justEvolved = justEvolved;
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
