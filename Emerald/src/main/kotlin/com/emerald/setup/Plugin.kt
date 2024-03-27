package com.emerald.setup

import com.emerald.setup.extensions.Extension
import com.emerald.setup.extensions.loadExtensions
import com.emerald.setup.extensions.registerCommands
import com.emerald.setup.permissions.PermissionStorage
import com.emerald.setup.permissions.PermissionStorageImpl
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class Plugin : JavaPlugin() {
    companion object {
        lateinit var instance: Plugin
    }

    lateinit var permissionStorage: PermissionStorage
        private set

    private val extensions = mutableSetOf<Extension>()

    override fun onEnable() {
        instance = this

        try {
            //try to initialize the Permissions storage (this will fetch the file)
            permissionStorage = PermissionStorageImpl()
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "An Error occurred when restoring the permissions")
            e.printStackTrace()
            terminate()
        }

        //load the extensions
        val extensionResults = runBlocking { loadExtensions() }

        //display whether the extensions were successful and add them to the global extension storage
        for (result in extensionResults) {
            result.fold(
                onSuccess = {
                    logger.log(Level.INFO, "Successfully loaded ${it.displayName}")
                    extensions += it
                },
                onFailure = {
                    logger.log(Level.SEVERE, "Failed to load extension $it")
                }
            )
        }

        //call their on enables
        extensions.forEach {
            try {
                it.onEnable()
            } catch (e: Exception) {
                e.printStackTrace()
                logger.log(Level.WARNING, "Failed to execute onEnable of ${it.displayName}")
                logger.log(Level.INFO, "Disabling `${it.displayName}` due to exceptions in the onEnable")
                shutDownPreRunningExtension(it)
            }
        }
        //register their command listeners
        extensions.registerCommands()
    }

    /**
     * Shuts down an extension that hasn't fully loaded yet
     * (For example didn't successfully execute onEnable)
     * This means it will **NOT**  call [Extension.onDisable]
     */
    private fun shutDownPreRunningExtension(extension: Extension) {
        extensions.remove(extension)
        logger.log(Level.WARNING, "Extension `${extension.displayName}` shut down")
    }

    /**
     * Terminates the plugin
     */
    private fun terminate() {
        logger.log(Level.INFO, "Emerald will now shut down due to errors in the initialization stage in order to prevent potential data corruption or other errors")
        Bukkit.getPluginManager().disablePlugin(this)
    }

    override fun onDisable() {
        //call the onDisable method of every extension
        extensions.forEach {
            try {
                it.onDisable()
            } catch (e: Exception) {
                e.printStackTrace()
                logger.log(Level.SEVERE, "Failed to execute onDisable of `${it.displayName}`")
            }
        }
        //save permissions to disk
        try {
            if (::permissionStorage.isInitialized)
                permissionStorage.close()
        } catch (e: Exception) {
            e.printStackTrace()
            logger.log(Level.SEVERE, "Failed to store permissions")
            logger.log(Level.WARNING, "Permission may be lost")
        }
    }

}


