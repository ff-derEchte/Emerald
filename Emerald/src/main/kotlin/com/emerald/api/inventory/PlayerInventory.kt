package com.emerald.api.inventory

import com.emerald.api.item.Item
import com.emerald.api.player.Player

sealed interface PlayerInventory : LiveMutableInventoryView {
    val owner: Player

    suspend fun setHotBarItem(slot: Int, item: Item)
    fun getHotBarItem(slot: Int): Item?

    val hotBar: LiveMutableInventoryView
}

class PlayerInventoryWrapper internal constructor(
    inv: BukkitInventory,
    override val owner: Player,
): InventoryWrapper(Dimensions(9, 4), inv), PlayerInventory {
    override suspend fun setHotBarItem(slot: Int, item: Item) {
        set(slot, 3, item)
    }

    override fun getHotBarItem(slot: Int): Item? = get(slot, 3)

    override val hotBar: LiveMutableInventoryView
        get() = slice(0, 3, 8, 3)
}