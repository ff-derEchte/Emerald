package com.emerald.api.event.player

import com.emerald.api.text.Text

interface PlayerLeaveEvent: PlayerEvent {
    var quiteMessage: Text
}