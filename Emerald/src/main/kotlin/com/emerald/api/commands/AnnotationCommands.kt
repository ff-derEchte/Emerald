package com.emerald.api.commands

import com.emerald.api.player.CommandSender
import java.lang.reflect.Method


interface CommandResolver {
    suspend fun resolve(name: String, player: CommandSender, args: Array<String>)

    val commands: Set<String>
}


class CommandPatternMatchingException(override val message: String) : Exception(message)


sealed interface PatternType {
    data object Int : PatternType

    data object String : PatternType

    data object Double : PatternType

    data object Boolean : PatternType

    data object Player : PatternType

    data class Complex(val clazz: Class<*>, val conversionMethod: Method) : PatternType


}

fun PatternType.toDisplayString(): String = when(this) {
    PatternType.Boolean -> "Boolean"
    is PatternType.Complex -> clazz.name
    PatternType.Double -> "Decimal"
    PatternType.Int -> "Integer"
    PatternType.Player -> "Player"
    PatternType.String -> "String"
}

