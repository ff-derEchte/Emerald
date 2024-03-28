import com.emerald.api.Command
import com.emerald.api.player.CommandSender
import com.emerald.api.player.Player
import com.emerald.api.text.text

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