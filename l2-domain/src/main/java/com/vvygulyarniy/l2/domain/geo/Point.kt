package com.vvygulyarniy.l2.domain.geo

import java.lang.Math.round
import java.lang.Math.toIntExact

data class Point(val x: Int = 0, val y: Int = 0, val z: Int = 0) {

    fun distanceTo(other: Point): Double {
        var x1 = x.toDouble()
        var y1 = y.toDouble()
        var z1 = z.toDouble()

        val x2 = other.x.toDouble()
        val y2 = other.y.toDouble()
        val z2 = other.z.toDouble()

        x1 -= x2
        y1 -= y2
        z1 -= z2

        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1)
    }

    fun moveForwardTo(target: Point, distance: Double): Point {
        val yLength = (target.y - this.y).toDouble()
        val xLength = (target.x - this.x).toDouble()
        val zLength = (target.z - this.z).toDouble()

        val angleSin = yLength / this.distanceTo(target)
        val angleCos = xLength / this.distanceTo(target)
        val zAngleSin = zLength / this.distanceTo(target)

        val deltaX = distance * angleCos
        val deltaY = distance * angleSin
        val deltaZ = distance * zAngleSin
        val newX = toIntExact(round(this.x + deltaX))
        val newY = toIntExact(round(this.y + deltaY))
        val newZ = toIntExact(round(this.z + deltaZ))

        return Point(newX, newY, newZ)
    }

    override fun toString(): String {
        return "($x,$y,$z)"
    }
}
