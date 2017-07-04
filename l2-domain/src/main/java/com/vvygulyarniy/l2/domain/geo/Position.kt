package com.vvygulyarniy.l2.domain.geo

data class Position(val point: Point, val heading: Int) {

    @JvmOverloads constructor(x: Int, y: Int, z: Int, heading: Int = 0) : this(Point(x, y, z), heading)

    fun distanceTo(position: Position): Double {
        return point.distanceTo(position.point)
    }
}
