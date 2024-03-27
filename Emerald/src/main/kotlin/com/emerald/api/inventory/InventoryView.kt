package com.emerald.api.inventory

import com.emerald.api.item.Item

interface InventoryView {
    val dimensions: Dimensions

    /**
     * @throws InventoryAccessException
     */
    fun get(x: Int, y: Int): Item?
}

interface MutableInventoryView : InventoryView {

    /**
     * @throws InventoryAccessException
     */
    suspend fun set(x: Int, y: Int, item: Item)
}


internal typealias BukkitInventory = org.bukkit.inventory.Inventory
