package com.orbital.thegame.spiffitnesspetsimulator;

/**
 * Created by Qi Wei on 14/6/2016.
 */
public class Penguin_Adult extends Spirit_Adult{
    private int minimumAffinity = 40;

    public Penguin_Adult(int affinityLevel) {
        super(affinityLevel);
    }

    public boolean check(int affinityLevel){
        return affinityLevel > minimumAffinity;
    }
}
