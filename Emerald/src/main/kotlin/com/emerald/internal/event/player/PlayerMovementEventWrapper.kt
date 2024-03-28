package com.emerald.internal.event.player

import com.emerald.api.event.player.PlayerMoveEvent
import com.emerald.api.player.Player
import com.emerald.api.position.Location
import com.emerald.internal.player.PlayerWrapper

typealias BukkitMovementEvent = org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveEventWrapper(val event: BukkitMovementEvent) : PlayerMoveEvent {
    override var from: Location
        get() = TODO("Not yet implemented")
        set(value) {}
    override var to: Location
        get() = TODO("Not yet implemented")
        set(value) {
            event.from.world
        }

    override val player: Player = PlayerWrapper(event.player)
}