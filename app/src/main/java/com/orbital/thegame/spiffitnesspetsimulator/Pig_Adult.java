package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 14/6/2016.
 */
public class Pig_Adult extends Spirit_Adult {
    private int minimumAffinity = 20;

    public Pig_Adult(int affinityLevel) {
        super(affinityLevel);
    }

    public boolean check(int affinityLevel){
        return affinityLevel > minimumAffinity;
    }
}
