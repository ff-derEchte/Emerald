package com.emerald.internal.event.player

import com.emerald.api.event.player.PlayerJoinEvent
import com.emerald.api.player.Player
import com.emerald.api.text.Text
import com.emerald.internal.player.PlayerWrapper

typealias BukkitPlayerJoinEvent = org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinEventWrapper(
    private val event: BukkitPlayerJoinEvent
) : PlayerJoinEvent {
    override var joinMessage: Text
        get() = Text.from(event.joinMessage())
        set(value) { event.joinMessage(value.toComponent()) }

    override val player: Player = PlayerWrapper(event.player)
}