package com.emerald.api.inventory

import com.emerald.api.player.Player

sealed interface PlayerInventory : MutableInventoryView {
    val owner: Player
}

class PlayerInventoryWrapper internal constructor(
    inv: BukkitInventory,
    override val owner: Player,
): InventoryWrapper(Dimensions(9, 4), inv), PlayerInventory