package com.vvygulyarniy.l2.domain.geo;

import lombok.Data;

/**
 * Created by Phoen-X on 16.02.2017.
 */
@Data
public class Position {
    private final int x;
    private final int y;
    private final int z;
    private final int heading;

    public Position(int x, int y, int z) {
        this(x, y, z, 0);
    }

    public Position(int x, int y, int z, int heading) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.heading = heading;
    }

    public double distanceTo(Position other) {
        double x1 = x;
        double y1 = y;
        double z1 = z;

        double x2 = other.getX();
        double y2 = other.getY();
        double z2 = other.getZ();

        x1 -= x2;
        y1 -= y2;
        z1 -= z2;

        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    }
}
