package com.emerald.api.inventory

data class Dimensions(
    val x: Int,
    val y: Int
)

fun Dimensions.checkBounds(x: Int, y: Int) {
    when {
        x < 0 -> throw InventoryAccessException("x cannot be negative")
        y < 0 -> throw InventoryAccessException("y cannot be negative")
        x >= this.x -> throw InventoryAccessException("x to big ($x) should be [0; ${this.x})")
        y >= this.y -> throw InventoryAccessException("y to big ($y) should be [0; ${this.y})")
    }
}