package utils

data class Coord2DLong(var x: Long, var y: Long) {
    operator fun plus(other: Coord2DLong) = Coord2DLong(this.x + other.x, this.y + other.y)
    operator fun minus(other: Coord2DLong) = Coord2DLong(this.x - other.x, this.y - other.y)

    operator fun times(scale: Long) = Coord2DLong(this.x * scale, this.y * scale)
    operator fun div(scale: Long) = Coord2DLong(this.x / scale, this.y / scale)
}

data class Coord2DInt(var x: Int, var y: Int) {
    operator fun plus(other: Coord2DInt) = Coord2DInt(this.x + other.x, this.y + other.y)
    operator fun minus(other: Coord2DInt) = Coord2DInt(this.x - other.x, this.y - other.y)

    operator fun times(scale: Int) = Coord2DInt(this.x * scale, this.y * scale)
    operator fun div(scale: Int) = Coord2DInt(this.x / scale, this.y / scale)
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