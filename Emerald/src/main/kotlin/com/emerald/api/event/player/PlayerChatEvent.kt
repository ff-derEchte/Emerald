package com.emerald.api.event.player

import com.emerald.api.player.Player
import com.emerald.api.text.Text


interface PlayerChatEvent : PlayerEvent {
    var message: Text
    val recipients: Set<Player>
}