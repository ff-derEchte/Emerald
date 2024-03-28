package com.emerald.api.player

import com.emerald.api.inventory.PlayerInventory
import com.emerald.api.inventory.PlayerInventoryWrapper
import com.emerald.api.text.Text
import com.emerald.api.text.text
import com.emerald.util.sync
import kotlinx.coroutines.runBlocking
import java.util.*

interface Player : CommandSender, PermissibleEntity, Identifiable, Messageable, Kickable, Movable {
    var health: Double
    var saturation: Double
    var foodLevel: Int
    val inventory: PlayerInventory
    val name: String
}

