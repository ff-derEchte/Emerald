package com.emerald.internal.extensions

import com.emerald.api.commands.CommandPatternMatchingException
import com.emerald.api.commands.CommandResolver
import com.emerald.internal.commands.processAnnotationCommands
import com.emerald.internal.config.VersionString
import com.emerald.internal.config.loadConfig
import com.emerald.internal.handlers.processOnDisableMethods
import com.emerald.internal.handlers.processOnEnableMethods
import com.emerald.internal.player.CommandSenderWrapper
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.io.File

const val ExtensionPath = "./extensions"


fun Set<Extension>.registerCommands() {
    forEach {
        it.commandExecutor.commands.forEach { command ->
            println("[${it.displayName}] Registering command $command")
            registerCommand(commandObject(command, it.commandExecutor, scope = it.extensionScope))
        }
    }
}

private fun commandObject(command: String, resolver: CommandResolver, scope: CoroutineScope) = object : Command(command.trim()) {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        scope.launch {
            try {
                resolver.resolve(command, CommandSenderWrapper(sender), args)
            } catch (e: CommandPatternMatchingException) {
                sender.sendMessage(e.message)
            } catch (e: RuntimeException) {
                sender.sendMessage("[E] ${e.message}")
                e.printStackTrace()
            }
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
    val onDisable: () -> Unit,
   val extensionScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
)