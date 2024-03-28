package com.emerald.internal.permissions

import com.emerald.internal.Plugin
import com.emerald.internal.player.BukkitPlayer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.permissions.PermissionAttachment
import java.io.File
import java.io.IOException
import java.util.UUID

interface PermissionStorage {
    fun addPermission(id: UUID, permission: String)

    fun removePermission(id: UUID, permission: String)

    /**
     * @throws IOException
     */
    fun close()
}

class PermissionStorageImpl : Listener, PermissionStorage {

    private val permissionAttachments: MutableMap<UUID, PermissionAttachment> = mutableMapOf()
    private val permissions: MutableMap<UUID, MutableList<String>>

    init {
        //register instance as event listener
        //so that new players will be registered
        Bukkit.getPluginManager().registerEvents(this, Plugin.instance)
        //read the permission data from disk
        permissions = readPermissionsFile()
        //register players that are already on the server and create their attachments
        //either with data of previous records or new empty records
        registerOnlinePLayers()
    }


    override fun addPermission(id: UUID, permission: String) {
        synchronized(this) {
            permissionAttachments[id]?.setPermission(permission, true)
            permissions[id]?.add(permission)
        }
    }

    override fun removePermission(id: UUID, permission: String) {
        synchronized(this) {
            permissionAttachments[id]?.setPermission(permission, false)
            permissions[id]?.remove(permission)
        }
    }

    override fun close() {
        val jsonString = Json.encodeToString(permissions.map { it.key.toString() to it.value }.toMap())
        val file = File("./plugins/emerald/permissions.json")
        file.writeText(jsonString)
    }


    private fun registerOnlinePLayers() {
        for (player in Bukkit.getOnlinePlayers()) {
            when {
                //if the player does not have an attachment yet, register them
                player.uniqueId !in permissionAttachments -> registerPlayer(player)
            }
        }
    }

    private fun readPermissionsFile(): MutableMap<UUID, MutableList<String>> {
        //check if root dir exists (if not create it)
        val emeraldDir = File("./plugins/emerald")
        when {
            !emeraldDir.exists() -> emeraldDir.mkdirs()
        }

        //check if permissions file exists (if not create it)
        val file = File("./plugins/emerald/permissions.json")
        when {
            !file.exists() -> file.createNewFile()
        }

        //read and parse permissions file
        val content = file.readText()
        val permissionsMap: Map<String, ArrayList<String>> = if (content != "") Json.decodeFromString(content) else hashMapOf()

        //map from String to UUID
        //(and yes the .toMap().toMutableMap() is necessary since the .toMutableMap method is not accessible from List<>
        return permissionsMap.map { UUID.fromString(it.key) to it.value }.toMap().toMutableMap()
    }


    private fun registerPlayer(player: BukkitPlayer) {
        when {
            //in this case the player already has permission entries but just no package yet
            player.uniqueId in permissions -> {
                //create empty attachment
                val attachment = player.addAttachment(Plugin.instance)
                //fill it with the data tha already exists
                for (permission in permissions[player.uniqueId]!!) {
                    attachment.setPermission(permission, true)
                }
                //store the attachment
                permissionAttachments[player.uniqueId] = attachment
            }
            //in this case we have no previous record of the player
            else -> {
                //create empty attachment
                val attachment = player.addAttachment(Plugin.instance)
                //store it
                permissionAttachments[player.uniqueId] = attachment
                //create empty permission record
                permissions[player.uniqueId] = mutableListOf()

            }
        }
    }

    @EventHandler
    fun onJoin(ev: PlayerJoinEvent) {
        //if the player does not yet have an attachment register them
        if (ev.player.uniqueId !in permissionAttachments) registerPlayer(ev.player)
    }
}