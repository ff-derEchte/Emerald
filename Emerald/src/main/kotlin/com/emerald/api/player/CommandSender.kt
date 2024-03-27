package com.emerald.api.player

import com.emerald.api.Command

interface CommandSender {
    fun asPlayer(): Player?

    fun hasPermission(permission: String): Boolean

    fun respond(message: String)
}

private typealias BukkitCommandSender = org.bukkit.command.CommandSender

internal class CommandSenderWrapper(private val commandSender: BukkitCommandSender) : CommandSender {
    override fun asPlayer(): Player? = when(val bukkitPlayer = commandSender as? BukkitPlayer) {
        null -> null
        else -> PlayerWrapper(bukkitPlayer)
    }


    override fun hasPermission(permission: String): Boolean = commandSender.hasPermission(permission)


    override fun respond(message: String) {
        commandSender.sendMessage(message)
    }

}