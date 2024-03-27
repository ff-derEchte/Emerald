package com.emerald.api.player

import com.emerald.api.inventory.PlayerInventory
import com.emerald.api.inventory.PlayerInventoryWrapper
import com.emerald.api.text.Text
import com.emerald.api.text.text
import com.emerald.util.sync
import kotlinx.coroutines.runBlocking
import java.util.*

interface Player : CommandSender, PermissibleEntity, Identifiable {
    var health: Double
    var saturation: Double
    var foodLevel: Int

    val inventory: PlayerInventory

    val name: String

    suspend fun kick(reason: Text)
    suspend fun kick(reason: String): Unit = kick(text(reason))

    fun kickBlocking(reason: Text) = runBlocking { kick(reason) }

    fun kickBlocking(reason: String) = runBlocking { kick(reason) }


}

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

    override fun respond(message: String) {
        player.sendMessage(message)
    }

    override suspend fun kick(reason: Text) {
        sync { player.kick(reason.toComponent()) }
    }

    override fun asPlayer(): Player = this

    override fun hasPermission(permission: String): Boolean = player.hasPermission(permission)

}