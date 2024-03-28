package com.emerald.api.player

import com.emerald.api.text.Text
import com.emerald.api.text.text

interface Messageable {

    fun respond(text: Text)

    fun respond(text: String): Unit = respond(text(text))
}