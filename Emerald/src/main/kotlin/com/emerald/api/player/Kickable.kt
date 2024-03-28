package com.emerald.api.player

import com.emerald.api.text.Text
import com.emerald.api.text.text
import kotlinx.coroutines.runBlocking

interface Kickable {
    suspend fun kick(reason: Text)

    suspend fun kick(reason: String): Unit = kick(text(reason))

    fun kickBlocking(reason: Text) = runBlocking { kick(reason) }

    fun kickBlocking(reason: String) = runBlocking { kick(reason) }
}