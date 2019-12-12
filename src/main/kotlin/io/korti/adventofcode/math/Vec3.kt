package io.korti.adventofcode.math

import io.korti.adventofcode.math.Axis.*

operator fun Number.plus(other: Number): Number {
    return when (this) {
        is Long   -> this.toLong() + other.toLong()
        is Int    -> this.toInt()  + other.toInt()
        is Short  -> this.toShort() + other.toShort()
        is Byte   -> this.toByte() + other.toByte()
        is Double -> this.toDouble() + other.toDouble()
        is Float  -> this.toFloat() + other.toFloat()
        else      -> throw RuntimeException("Unknown numeric type")
    }
}

operator fun Number.minus(other: Number): Number {
    return when (this) {
        is Long   -> this.toLong() - other.toLong()
        is Int    -> this.toInt()  - other.toInt()
        is Short  -> this.toShort() - other.toShort()
        is Byte   -> this.toByte() - other.toByte()
        is Double -> this.toDouble() - other.toDouble()
        is Float  -> this.toFloat() - other.toFloat()
        else      -> throw RuntimeException("Unknown numeric type")
    }
}

fun Number.compareTo(other: Number): Int {
    return when (this) {
        is Long   -> this.toLong().compareTo(other.toLong())
        is Int    -> this.toInt().compareTo(other.toInt())
        is Short  -> this.toShort().compareTo(other.toShort())
        is Byte   -> this.toByte().compareTo(other.toByte())
        is Double -> this.toDouble().compareTo(other.toDouble())
        is Float  -> this.toFloat().compareTo(other.toFloat())
        else      -> throw RuntimeException("Unknown numeric type")
    }
}

data class Vec3(val x: Number, val y: Number, val z: Number) {

    operator fun plus(vec: Vec3): Vec3 {
        return Vec3(x + vec.x, y + vec.y, z + vec.z)
    }

    fun compareX(vec: Vec3): Int {
        return x.compareTo(vec.x)
    }

    fun compareY(vec: Vec3): Int {
        return y.compareTo(vec.y)
    }

    fun compareZ(vec: Vec3): Int {
        return z.compareTo(vec.z)
    }

    fun get(axis: Axis): Number {
        return when (axis) {
            X -> x
            Y -> y
            Z -> z
        }
    }

    fun move(axis: Axis, steps: Number = 1): Vec3 {
        return when (axis) {
            X -> Vec3(x + steps, y, z)
            Y -> Vec3(x, y + steps, z)
            Z -> Vec3(x, y, z + steps)
        }
    }

}