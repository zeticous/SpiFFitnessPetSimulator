package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Esmond on 8/7/2016.
 */
public class WatchSpirit {

    public WatchSpirit(){
        setAffinityPoint(0);
        setAffinityLevel(0);
    }

    private int affinityLevel;
    private int affinityPoint;
    private int register;
    private int animation_happy;
    private int animation_idle;

    public int getAffinityLevel() {
        return affinityLevel;
    }

    public void setAffinityLevel(int affinityLevel) {
        this.affinityLevel = affinityLevel;
    }

    public int getAffinityPoint() {
        return affinityPoint;
    }

    public void setAffinityPoint(int affinityPoint) {
        this.affinityPoint = affinityPoint;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public int getAnimation_happy() {
        return animation_happy;
    }

    public void setAnimation_happy(int animation_happy) {
        this.animation_happy = animation_happy;
    }

    public int getAnimation_idle() {
        return animation_idle;
    }

    public void setAnimation_idle(int animation_idle) {
        this.animation_idle = animation_idle;
    }
}
