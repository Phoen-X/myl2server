package com.vvygulyarniy.l2.gameserver.world.management.stats;

import java.util.HashMap;
import java.util.Map;

/**
 * Phoen-X on 19.03.2017.
 */
public class PatkStrDependencyTable {
    private final Map<Integer, Double> table = new HashMap<>();


    public PatkStrDependencyTable() {
        table.put(1, 0.30);
        table.put(2, 0.31);
        table.put(3, 0.32);
        table.put(4, 0.34);
        table.put(5, 0.35);
        table.put(6, 0.36);
        table.put(7, 0.37);
        table.put(8, 0.39);
        table.put(9, 0.40);
        table.put(10, 0.42);
        table.put(11, 0.43);
        table.put(12, 0.45);
        table.put(13, 0.46);
        table.put(14, 0.48);
        table.put(15, 0.50);
        table.put(16, 0.51);
        table.put(17, 0.53);
        table.put(18, 0.55);
        table.put(19, 0.57);
        table.put(20, 0.59);
        table.put(21, 0.61);
        table.put(22, 0.63);
        table.put(23, 0.66);
        table.put(24, 0.68);
        table.put(25, 0.71);
        table.put(26, 0.73);
        table.put(27, 0.76);
        table.put(28, 0.78);
        table.put(29, 0.81);
        table.put(30, 0.84);
        table.put(31, 0.87);
        table.put(32, 0.90);
        table.put(33, 0.94);
        table.put(34, 0.97);
        table.put(35, 1.01);
        table.put(36, 1.04);
        table.put(37, 1.08);
        table.put(38, 1.12);
        table.put(39, 1.16);
        table.put(40, 1.20);
        table.put(41, 1.24);
        table.put(42, 1.29);
        table.put(43, 1.33);
        table.put(44, 1.38);
        table.put(45, 1.43);
        table.put(46, 1.48);
        table.put(47, 1.54);
        table.put(48, 1.59);
        table.put(49, 1.65);
        table.put(50, 1.71);
    }

    public double get(int strValue) {
        return table.get(strValue);
    }
}
