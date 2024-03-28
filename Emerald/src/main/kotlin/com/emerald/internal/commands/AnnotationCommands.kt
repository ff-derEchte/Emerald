package com.emerald.internal.commands

import com.emerald.api.Command
import com.emerald.api.player.CommandSender
import com.emerald.api.player.Player
import com.emerald.internal.extensions.ExtensionClassLoader
import com.emerald.api.commands.CommandPatternMatchingException
import com.emerald.api.commands.CommandResolver
import com.emerald.api.commands.PatternType
import com.emerald.api.commands.toDisplayString
import com.emerald.internal.player.PlayerWrapper
import com.emerald.util.getKFunctionsWithAnnotation
import org.bukkit.Bukkit
import java.lang.reflect.Method
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

fun processAnnotationCommands(classLoader: ExtensionClassLoader) : CommandResolver {
    val commands = getKFunctionsWithAnnotation<Command>(classLoader)

    val commandRegistry = CommandRegistry()

    commands.forEach { (annotation, method) ->
        val name = annotation.name.takeUnless { it == "" } ?: method.name
        commandRegistry.register(name, method, annotation.permissions.toList())
    }

    return commandRegistry
}


class CommandRegistry : CommandResolver {
    private val registry: MutableMap<String, CommandPatternExecutor> = mutableMapOf()

    override val commands: Set<String>
        get() = registry.keys

    private fun getOrInsert(name: String): CommandPatternExecutor = registry[name] ?: CommandPatternExecutor(name).apply { registry[name] = this }

    fun register(name: String, function: KCallable<*>, permissions: List<String>) {
        getOrInsert(name).register(function, permissions)
    }

    override suspend fun resolve(name: String, player: CommandSender, args: Array<String>) {
        registry[name]?.resolve(player, args)
    }
}


class CommandPatternExecutor(private val name: String) {

    enum class User {
        Player,
        Sender
    }

    data class Variant(
        val user: User,
        val patterns: List<PatternType>,
        val permissions: List<String>
    )

    private val variants: MutableMap<Variant, KCallable<*>> = hashMapOf()

    fun register(function: KCallable<*>, permissions: List<String>) {
        if (function.parameters.isEmpty()) {
            throw CommandPatternMatchingException("Command listener must accept Player or Sender as first argument: ${function.name}")
        }
        val commandUser = when(function.parameters.first().type) {
            Player::class.createType() -> User.Player
            CommandSender::class.createType() -> User.Sender
            else -> throw CommandPatternMatchingException("Command listener must accept Player or Sender as first argument: ${function.name}")
        }
        val patterns = function.parameters
            .slice(1..<function.parameters.size)
            .map { it.type.jvmErasure.java.toPatternType() }


        variants[Variant(commandUser, patterns, permissions)] = function
    }

    suspend fun resolve(sender: CommandSender, args: Array<String>) {
        //try to find a variant that matches
        val result = variants
            .filter { it.key.patterns.size == args.size }
            .firstNotNullOfOrNull { (variant, method) -> variant.patterns.match(args)?.let { Triple(it, method, variant) } }

        when(result) {
            //if variant doesn't exist show usage and error out
            null -> {
                printUsage(sender)
                throw CommandPatternMatchingException("No variant found with ${args.contentToString()}")
            }
            else -> {
                val (finalArgs, method, variant) = result

                //check if user has sufficient permissions
                if (variant.permissions.any { !sender.hasPermission(it) }) {
                    throw CommandPatternMatchingException("You dont have permission to use this variant")
                }

                //check if the variant accepts a player or sender
                when(variant.user) {
                    User.Player -> {
                        val player = sender.asPlayer() ?: throw CommandPatternMatchingException("Only Players can use this variant")
                        //invoke variant with player
                        method.callSuspend(player, *finalArgs.toTypedArray())
                    }
                    //invoke with sender
                    User.Sender -> method.callSuspend(sender, *finalArgs.toTypedArray())
                }

            }
        }

    }

    private fun printUsage(sender: CommandSender) {
        sender.respond("Invalid Variants supplied. Use:")
        variants
            .filter { (variant, _) -> //remove variants the user doesn't have permission for
                variant.permissions.none { !sender.hasPermission(it) }
            }
            .forEach {  (variant, method) ->
                val args = variant.patterns
                    .mapIndexed { index, pattern -> //associate the arg with its type
                        "<${method.parameters[index].name}: ${pattern.toDisplayString()}>"
                    }
                    .joinToString(" ") //collect to a string
                sender.respond("/$name $args")
            }
    }
}

fun List<PatternType>.match(strings: Array<String>): List<Any>? {
    return this.mapIndexed { i, pattern -> pattern.match(strings[i]) ?: return null }
}

fun Class<*>.toPatternType(): PatternType {
    return when(this) {
        Int::class.java -> PatternType.Int
        String::class.java -> PatternType.String
        Double::class.java -> PatternType.Double
        Boolean::class.java -> PatternType.Boolean
        Player::class.java -> PatternType.Player
        else -> throw CommandPatternMatchingException("Invalid argument")
    }
}

fun PatternType.match(string: String): Any? {
    return when(this) {
        is PatternType.Boolean -> string.toBooleanStrictOrNull()
        is PatternType.Double -> string.toDoubleOrNull()
        is PatternType.Int -> string.toIntOrNull()
        is PatternType.String -> string
        is PatternType.Player -> Bukkit.getPlayer(string)?.let { PlayerWrapper(it) }
        is PatternType.Complex -> {
            conversionMethod.invoke(null, string)
        }
    }
}