package com.emerald.api.position

import org.bukkit.Location
import org.bukkit.World

data class Position(val x: Double, val y: Double, val z: Double)

internal fun Position.toLocation(world: World) = Location(world, x, y, z)

internal fun Location.toPosition() = Position(x, y, z)

operator fun Position.plus(vec: Vector) = Position(vec.x+x, vec.y+y, vec.z+z)