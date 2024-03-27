package com.emerald.api.inventory

import com.emerald.api.item.Item
import com.emerald.util.sync
import org.bukkit.inventory.ItemStack


private fun Item.toBukkitItem(): ItemStack {
    TODO()
}

sealed class InventoryWrapper(
    final override val dimensions: Dimensions,
    protected open val inv: BukkitInventory,
): MutableInventoryView {

    private val inventory: Array<Array<Item?>> = Array(dimensions.x) { Array(dimensions.y) { null } }

    override fun get(x: Int, y: Int): Item? {
        checkInBounds(x, y)
        return inventory[x][y]
    }


    override suspend fun set(x: Int, y: Int, item: Item) {
        inventory[x][y] = item
        sync { inv.setItem(x+dimensions.x*y, item.toBukkitItem()) }
    }

    private fun checkInBounds(x: Int, y: Int) {
        when {
            x < 0 || x >= dimensions.x -> throw InventoryAccessException("x must be between 0 and ${dimensions.x} but was $x")
            y < 0 || y >= dimensions.y -> throw InventoryAccessException("y must be between 0 and ${dimensions.y} but was $y")
        }
    }
    internal fun bukkitInventory(): BukkitInventory = inv
}