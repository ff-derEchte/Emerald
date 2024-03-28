package com.emerald.internal.event.player

import com.emerald.api.event.player.PlayerChatEvent
import com.emerald.api.player.Player
import com.emerald.api.text.Text
import com.emerald.api.text.text
import com.emerald.internal.player.PlayerWrapper
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChatEventWrapper(
    val event: AsyncPlayerChatEvent
) : PlayerChatEvent {
    override var message: Text
        get() = text(event.message)
        set(value) { event.message = value.toString() }

    override val recipients: Set<Player>
        get() = event.recipients.map { PlayerWrapper(it) }.toSet()
    override val player: Player
        get() = TODO("Not yet implemented")

}