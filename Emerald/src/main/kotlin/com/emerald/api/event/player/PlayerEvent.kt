package com.emerald.api.event.player

import com.emerald.api.event.Event
import com.emerald.api.player.Player

interface PlayerEvent : Event {
    val player: Player
}