package com.emerald.api.position


data class Vector(val x: Double, val y: Double, val z: Double)

fun vec(x: Double, y: Double, z: Double) = Vector(x, y, z)

