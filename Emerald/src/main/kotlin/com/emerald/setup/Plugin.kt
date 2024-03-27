package com.emerald.setup

import com.emerald.setup.extensions.Extension
import com.emerald.setup.extensions.loadExtensions
import com.emerald.setup.extensions.registerCommands
import com.emerald.setup.permissions.PermissionStorage
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.java.JavaPlugin

class Plugin : JavaPlugin() {
    companion object {
        lateinit var instance: Plugin
    }

    lateinit var storage: PermissionStorage
        private set

    private val extensions = mutableSetOf<Extension>()

    override fun onEnable() {
        instance = this

        storage = PermissionStorage()
        val extensionResults = runBlocking { loadExtensions() }

        for (result in extensionResults) {
            result.fold(
                onSuccess = {
                    println("Successfully loaded ${it.displayName}")
                    extensions += it
                },
                onFailure = {
                    println("Failed to load extension $it")
                }
            )
        }

        //register their command listeners
        extensions.registerCommands()
        //call their on enables
        extensions.forEach {
            try {
                it.onEnable()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to execute onEnable of ${it.displayName}")
            }
        }
    }


    override fun onDisable() {
        extensions.forEach {
            try {
                it.onDisable()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to execute onDisable of ${it.displayName}")
            }
        }
    }

}


