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
}
