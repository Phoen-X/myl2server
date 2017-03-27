package com.vvygulyarniy.l2.gameserver.world.character.info.stat;

/**
 * Phoen-X on 19.03.2017.
 */
public class CombatStat {
    private final double baseValue;
    private Type type;
    private double multiplier = 1;
    private double adder;

    public CombatStat(Type type, double baseValue) {
        this.baseValue = baseValue;
        this.type = type;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public Type getType() {
        return type;
    }

    public void addMultiplier(double value) {
        this.multiplier *= value;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void applyAdder(int value) {
        this.adder += value;
    }

    public double getAdder() {
        return adder;
    }

    public enum Type {
        PATK, MATK;
    }
}
