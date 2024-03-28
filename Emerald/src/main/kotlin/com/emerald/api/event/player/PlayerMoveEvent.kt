package com.emerald.api.event.player

import com.emerald.api.player.Player
import com.emerald.api.position.Location

interface PlayerMoveEvent : PlayerEvent {
    var from: Location
    var to: Location
}