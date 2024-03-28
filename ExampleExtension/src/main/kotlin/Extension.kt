import com.emerald.api.Command
import com.emerald.api.Config
import com.emerald.api.Event
import com.emerald.api.OnEnable
import com.emerald.api.config.config
import com.emerald.api.event.EventHandle
import com.emerald.api.event.player.PlayerJoinEvent
import com.emerald.api.player.CommandSender
import com.emerald.api.player.Player
import com.emerald.api.position.plus
import com.emerald.api.position.vec
import com.emerald.api.text.text
import kotlinx.coroutines.delay


@Config
val Config = config {
    name = "Example Extension"
    version = "1.0"
}

@OnEnable
fun onEnable() {
    println("Extension loaded")
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

@Event
fun onJoin(event: PlayerJoinEvent, handle: EventHandle) {
    event.joinMessage = text()
}