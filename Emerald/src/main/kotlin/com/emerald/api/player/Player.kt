package com.emerald.api.player

interface Player {
}

typealias BukkitPlayer = org.bukkit.entity.Player

internal class PlayerWrapper(val player: BukkitPlayer) : Player {

}