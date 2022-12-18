package utils

data class Coord2D(val x: Long, val y: Long) {
    operator fun plus(other: Coord2D) = Coord2D(this.x + other.x, this.y + other.y)
    operator fun minus(other: Coord2D) = Coord2D(this.x - other.x, this.y - other.y)

    operator fun times(scale: Long) = Coord2D(this.x * scale, this.y * scale)
    operator fun div(scale: Long) = Coord2D(this.x / scale, this.y / scale)
}

data class Coord3D(val x: Long, val y: Long, val z: Long) {
    operator fun plus(other: Coord3D) = Coord3D(
        this.x + other.x,
        this.y + other.y,
        this.z + other.z
    )
}

data class BBox3D(val x: LongRange, val y: LongRange, val z: LongRange) {
    operator fun contains(coord: Coord3D) = coord.x in this.x && coord.y in this.y && coord.z in this.z
}