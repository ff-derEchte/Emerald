# Emerald
Emerald is a framework for minecraft plugin development which offers expressive and easy to use apis
that make plugin development easier

```kotlin
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
}
```