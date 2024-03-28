package com.emerald.internal.player

import com.emerald.api.player.CommandSender
import com.emerald.api.player.Player
import com.emerald.api.text.Text
import java.util.*

private typealias BukkitCommandSender = org.bukkit.command.CommandSender

internal class CommandSenderWrapper(private val commandSender: BukkitCommandSender) : CommandSender {
    override fun asPlayer(): Player? = when(val bukkitPlayer = commandSender as? BukkitPlayer) {
        null -> null
        else -> PlayerWrapper(bukkitPlayer)
    }

    override val uuid: UUID = (commandSender as? BukkitPlayer)?.uniqueId ?: UUID.fromString("c576ec25-d731-40a0-9d2c-2e846916b614")

    override fun hasPermission(permission: String): Boolean = commandSender.hasPermission(permission)

    override fun respond(text: Text) {
        commandSender.sendMessage(text.toComponent())
    }


}