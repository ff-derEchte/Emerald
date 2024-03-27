package com.emerald.api.commands

import com.emerald.api.player.CommandSender
import com.emerald.api.player.Player
fun commands(builder: CommandBuilder.() -> Unit) {

}

interface CommandBuilder {
    fun command(name: String, executor: CommandExecutor.() -> Unit)

    fun playerCommand(name: String, executor: PlayerCommandExecutor.() -> Unit)
}


interface PlayerCommandExecutor : CommandExecutor {
    val player: Player
}

interface CommandExecutor {
    val sender: CommandSender

    val args: List<String>

    fun<T> nextArg(): T
}