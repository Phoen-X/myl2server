package com.vvygulyarniy.l2.gameserver.world.character.info.stat;

import java.util.concurrent.locks.StampedLock;

/**
 * Phoen-X on 18.03.2017.
 */
@SuppressWarnings("Duplicates")
public class Gauge {
    private final StampedLock lock = new StampedLock();
    private boolean canRegen = true;
    private double currValue;
    private double maxValue;
    private double regenPerSecond;

    public Gauge(double currValue, double maxValue, double regenPerSecond) {
        this.currValue = currValue;
        this.maxValue = maxValue;
        this.regenPerSecond = regenPerSecond;
    }

    public void regen(long millisSinceLastRegen) {
        long stamp = lock.writeLock();
        try {
            if (canRegen) {
                double regenAmount = (regenPerSecond / 1000) * millisSinceLastRegen;
                currValue = Math.min(currValue + regenAmount, maxValue);
            }
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public boolean isCanRegen() {
        long stamp = lock.tryOptimisticRead();
        boolean result = canRegen;
        if (lock.validate(stamp)) {
            return result;
        } else {
            stamp = lock.readLock();
            try {
                return canRegen;
            } finally {
                lock.unlockRead(stamp);
            }
        }
    }

    public void setCanRegen(boolean canRegen) {
        long stamp = lock.writeLock();
        try {
            this.canRegen = canRegen;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public double getCurrValue() {
        long stamp = lock.tryOptimisticRead();
        double result = currValue;
        if (lock.validate(stamp)) {
            return result;
        } else {
            stamp = lock.readLock();
            try {
                return currValue;
            } finally {
                lock.unlockWrite(stamp);
            }
        }
    }

    public void setCurrValue(double currValue) {
        long stamp = lock.writeLock();
        try {
            this.currValue = currValue;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public double getMaxValue() {
        long stamp = lock.tryOptimisticRead();
        double result = maxValue;
        if (lock.validate(stamp)) {
            return result;
        } else {
            stamp = lock.readLock();
            try {
                return maxValue;
            } finally {
                lock.unlockRead(stamp);
            }
        }
    }

    public void setMaxValue(double maxValue) {
        long stamp = lock.writeLock();
        try {
            this.maxValue = maxValue;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public double getRegenPerSecond() {
        long stamp = lock.tryOptimisticRead();
        double regen = this.regenPerSecond;
        if (lock.validate(stamp)) {
            return regen;
        } else {
            stamp = lock.readLock();
            try {
                return this.regenPerSecond;
            } finally {
                lock.unlockRead(stamp);
            }
        }
    }

    public void setRegenPerSecond(double regenPerSecond) {
        long stamp = lock.writeLock();
        try {
            this.regenPerSecond = regenPerSecond;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
