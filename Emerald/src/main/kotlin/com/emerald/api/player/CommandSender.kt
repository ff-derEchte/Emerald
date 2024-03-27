package com.emerald.api.player

import com.emerald.setup.Plugin
import com.emerald.util.sync
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


interface CommandSender : PermissibleEntity, Identifiable {

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

    override val uuid: UUID = (commandSender as? BukkitPlayer)?.uniqueId ?: UUID.fromString("c576ec25-d731-40a0-9d2c-2e846916b614")

    override fun hasPermission(permission: String): Boolean = commandSender.hasPermission(permission)
    override fun addPermission(permission: String) {
        commandSender.addAttachment(Plugin.instance)
        TODO("Not yet implemented")
    }


    override fun respond(message: String) {
        commandSender.sendMessage(message)
    }


}