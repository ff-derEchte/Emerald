import com.emerald.api.Command
import com.emerald.api.Config
import com.emerald.api.OnEnable
import com.emerald.api.config.config
import com.emerald.api.player.CommandSender
import com.emerald.api.player.Player


@Config
fun extensionConfig() = config {
    name = "Example Extension"
    version = "1.0"
}

@OnEnable
fun onEnable() {
    println("Extension loaded")
}

@Command(permissions = ["heal.self"])
fun heal(player: Player) {
    player.health = 20.0
    player.foodLevel = 20
    player.respond("You have been healed")
}

@Command(permissions = ["heal.others"])
fun heal(sender: CommandSender, target: Player) {
    target.health = 20.0
    target.foodLevel = 20
    target.respond("You have been healed by ${sender.asPlayer()?.name ?: "Console"}")
    sender.respond("You have healed ${target.name}")
}

@Command(permissions = ["kick.others"])
fun kick(sender: CommandSender, player: Player, reason: String) {
    player.kickBlocking(reason)
    sender.respond("Kicked ${player.name} for `$reason`")
}

@Command
fun op(player: Player) {
    player.addPermission("heal.self")
    player.respond("Congrats you opd yourself")
}
