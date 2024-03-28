import com.emerald.api.Command
import com.emerald.api.Config
import com.emerald.api.OnEnable
import com.emerald.api.config.config
import com.emerald.api.player.CommandSender
import com.emerald.api.player.Player
import com.emerald.api.position.plus
import com.emerald.api.position.vec
import com.emerald.api.text.text
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


@Config
val Config = config {
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
    text(text("Hello World", color = 0x123456), text("Hello World"))
}

@Command(permissions = ["heal.others"])
fun heal(sender: CommandSender, target: Player, amount: Int) {
    target.health += amount
    target.foodLevel += amount
    target.respond("You have been healed $amount points by ${sender.asPlayer()?.name ?: "Console"}")
    sender.respond("You have healed ${target.name}")
    text(text("Hello World", color = 0x123456), text("Hello World"))
}

@Command(permissions = ["kick.others"])
suspend fun kick(sender: CommandSender, player: Player, reason: String) {
    player.kick(reason)
    sender.respond("Kicked ${player.name} for `$reason`")
}

@Command
fun echo(sender: CommandSender, message: String) {
    sender.respond(message)
}

@Command
fun move(player: Player, x: Double, y: Double, z: Double) {
    player.position += vec(x, y, z)
}

@Command
suspend fun countDown(sender: CommandSender, n: Int, delay: Int) {
    for (i in 0..n) {
        sender.respond("${n-i}")
        delay(delay.toLong())
    }
}