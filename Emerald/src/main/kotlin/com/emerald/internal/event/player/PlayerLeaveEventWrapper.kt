package com.emerald.internal.event.player

import com.emerald.api.event.player.PlayerLeaveEvent
import com.emerald.api.player.Player
import com.emerald.api.text.Text
import com.emerald.internal.player.PlayerWrapper
import org.bukkit.event.player.PlayerQuitEvent

class PlayerLeaveEventWrapper(
    val event: PlayerQuitEvent
) : PlayerLeaveEvent {
    override var quiteMessage: Text
        get() = Text.from(event.quitMessage())
        set(value) { event.quitMessage(value.toComponent()) }

    override val player: Player = PlayerWrapper(event.player)

}