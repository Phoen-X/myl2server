package com.vvygulyarniy.l2.domain.geo;

import lombok.Data;

/**
 * Created by Phoen-X on 16.02.2017.
 */
@Data
public class Position {
    private final Point point;
    private final int heading;

    public Position(int x, int y, int z) {
        this(x, y, z, 0);
    }

    public Position(int x, int y, int z, int heading) {
        this(new Point(x, y, z), heading);
    }

    public Position(final Point point, int heading) {
        this.point = point;
        this.heading = heading;
    }

    public double distanceTo(Position position) {
        return getPoint().distanceTo(position.getPoint());
    }
}
