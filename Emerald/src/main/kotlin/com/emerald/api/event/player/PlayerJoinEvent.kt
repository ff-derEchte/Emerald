package com.emerald.api.event.player

import com.emerald.api.text.Text

interface PlayerJoinEvent : PlayerEvent {

    var joinMessage: Text
}
