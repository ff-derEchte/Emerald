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

@Command
fun foo(sender: CommandSender) {
    sender.respond("It works")
}

