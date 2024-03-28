package com.emerald.internal.player

import com.emerald.api.inventory.PlayerInventory
import com.emerald.api.inventory.PlayerInventoryWrapper
import com.emerald.api.player.Player
import com.emerald.api.position.Position
import com.emerald.api.position.toLocation
import com.emerald.api.position.toPosition
import com.emerald.api.text.Text
import com.emerald.util.sync
import com.emerald.util.syncBlocking
import org.bukkit.World
import java.util.*

typealias BukkitPlayer = org.bukkit.entity.Player

internal class PlayerWrapper(private val player: BukkitPlayer) : Player {
    override var health: Double
        get() = player.health
        set(value) {
            player.health = value
        }

    override val uuid: UUID = player.uniqueId

    override val inventory: PlayerInventory = PlayerInventoryWrapper(player.inventory, this)

    override var foodLevel: Int
        get() = player.foodLevel
        set(value) { player.foodLevel = value }

    override var saturation: Double
        get() = player.saturation.toDouble()
        set(value) {
            player.saturation = value.toFloat()
        }

    override val name: String = player.displayName

    override fun respond(text: Text) {
        player.sendMessage(text.toComponent())
    }


    override suspend fun kick(reason: Text) {
        sync { player.kick(reason.toComponent()) }
    }

    override var position: Position
        get() = player.location.toPosition()
        set(value) {
            syncBlocking { player.teleport(value.toLocation(player.world)) }
        }


    override fun asPlayer(): Player = this

    override fun hasPermission(permission: String): Boolean = player.hasPermission(permission)

}