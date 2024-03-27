package com.emerald.setup.permissions

import com.emerald.setup.Plugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.permissions.PermissionAttachment
import java.io.File
import java.util.UUID

class PermissionStorage : Listener {



    init {
        Bukkit.getPluginManager().registerEvents(this, Plugin.instance)
        Bukkit.getOnlinePlayers().forEach { permissions[it.uniqueId] = it.addAttachment(Plugin.instance) }
    }

    private fun readPermissionsFile() {
        val emeraldDir = File("./plugins/emerald")
        when {
            !emeraldDir.exists() -> emeraldDir.mkdirs()
        }

        val file = File("./plugins/emerald/permissions.json")
        when {
            !file.exists() -> file.createNewFile()
        }

        file.readText()
    }

    private val permissions: MutableMap<UUID, PermissionAttachment> = mutableMapOf()


    fun addPermission(id: UUID, permission: String) {
        permissions[id]?.setPermission(permission, true)
    }

    fun removePermission(id: UUID, permission: String) {
        permissions[id]?.setPermission(permission, false)
    }

    @EventHandler
    fun onJoin(ev: PlayerJoinEvent) {
        if (ev.player.uniqueId !in permissions)
            permissions[ev.player.uniqueId] = ev.player.addAttachment(Plugin.instance)
    }
}