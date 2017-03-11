package com.vvygulyarniy.l2.domain.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

import static java.lang.Math.round;
import static java.lang.Math.toIntExact;

/**
 * Phoen-X on 11.03.2017.
 */
@AllArgsConstructor
@Getter
public final class Point {
    private final int x;
    private final int y;
    private final int z;

    public double distanceTo(final Point other) {
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

    public Point moveForwardTo(Point target, double distance) {
        double yLength = target.getY() - this.getY();
        double xLength = target.getX() - this.getX();
        double zLength = target.getZ() - this.getZ();

        double angleSin = yLength / this.distanceTo(target);
        double angleCos = xLength / this.distanceTo(target);
        double zAngleSin = zLength / this.distanceTo(target);

        double deltaX = distance * angleCos;
        double deltaY = distance * angleSin;
        double deltaZ = distance * zAngleSin;
        int newX = toIntExact(round(this.getX() + deltaX));
        int newY = toIntExact(round(this.getY() + deltaY));
        int newZ = toIntExact(round(this.getZ() + deltaZ));

        return new Point(newX, newY, newZ);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y && z == point.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
