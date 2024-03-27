package com.emerald.setup.extensions

import com.emerald.api.commands.CommandPatternMatchingException
import com.emerald.api.commands.CommandResolver
import com.emerald.api.player.CommandSenderWrapper
import com.emerald.setup.commands.processAnnotationCommands
import com.emerald.setup.config.VersionString
import com.emerald.setup.config.loadConfig
import com.emerald.setup.handlers.processOnDisableMethods
import com.emerald.setup.handlers.processOnEnableMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.io.File

const val ExtensionPath = "./extensions"


fun Set<Extension>.registerCommands() {
    forEach {
        it.commandExecutor.commands.forEach { command ->
            println("[${it.displayName}] Registering command $command")
            registerCommand(commandObject(command, it.commandExecutor))
        }
    }
}

private fun commandObject(command: String, resolver: CommandResolver) = object : Command(command.trim()) {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        try {
            resolver.resolve(command, CommandSenderWrapper(sender), args)
        } catch (e: CommandPatternMatchingException) {
            sender.sendMessage(e.message)
        } catch (e: RuntimeException) {
            sender.sendMessage("[E] ${e.message}")
        }
        return true
    }
}

fun registerCommand(command: Command) {
    Bukkit.getCommandMap().register(command.name, command)
}

suspend fun loadExtensions(): Set<Result<Extension>> = coroutineScope {
    val directory = File(ExtensionPath)

    when {
        !directory.exists() -> directory.mkdirs()
        !directory.isDirectory -> error("extensions directory must be directory")
    }

    val deferredExtensions = directory
        .listFiles()
        ?.filter { it.name.endsWith(".jar") }
        ?.map { file -> async(Dispatchers.IO) { runCatching { createExtension(file.name) } } }
        ?: error("")

    val extensions = deferredExtensions.awaitAll()

    extensions.toSet()
}

fun createExtension(name: String): Extension {
    val classLoader = ExtensionClassLoader("$ExtensionPath/$name", Extension::class.java.classLoader)

    val config = loadConfig(classLoader)

    val commandExecutor = processAnnotationCommands(classLoader)
    val onEnable = processOnEnableMethods(classLoader)
    val onDisable = processOnDisableMethods(classLoader)

    return Extension(
        name.removeSuffix(".jar"),
        config.name,
        config.version,
        commandExecutor,
        onEnable,
        onDisable
    )
}

data class Extension(
    val name: String,
    val displayName: String,
    val version: VersionString,
    val commandExecutor: CommandResolver,
    val onEnable: () -> Unit,
    val onDisable: () -> Unit
)