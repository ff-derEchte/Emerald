# Emerald

Emerald is a framework for minecraft plugin development which offers expressive and easy to use thread safe apis
that make plugin development easier

***

## Table of Contents

- [What is Emerald](#what-is-emerald)
- [Emerald Example Code](#emerald-example-code)
***

# Disclaimer
> Emerald is still in extremely early development so do **NOT** use it!

## What is Emerald

Emerald is an API specifically tailored to make minecraft plugin development easier. The API is a standalone api that works 
separate from any Spigot or Bukkit based APIs and therefore has a few benefits:

### Benefits

> - The Emerald API is thread safe which means that any operation can be performed by any thread.
> - Emerald offers a declarative way to define command and event handlers
> - Emerald directly embeds coroutines and suspend functions to streamline development
> - Emerald extensions are more flexible and require little to no boilerplate

## How can emerald extensions be executed?

Emerald extensions only use the custom emerald api and are therefore independent of Spigot.
Eventually the goal will be to have a custom minecraft server for emerald extensions, but for now, a spigot plugin acts
as the bridge between emerald and spigot by which emerald extensions can run on a spigot server.

You can compile an emerald extension to a jar and simply drag it into the `extensions` folder.
The emerald bridge plugin (or theoretically the emerald server) will then simply execute it

## Emerald Example Code

The following code example (which btw is already fully functioning) 
showcases some of Emerald's features such as 
>- command pattern matching and parsing
>- Automatic registering of commands
>- Config inside source files

### Note:
> Command handlers in Emerald can be `suspend` (and don't run on the main thread)


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
    target.respond("You have been healed by ${sender.asPlayer()?.name ?: "A foreign power"}")
    sender.respond("You have healed ${target.name}")
}
```

### Command Handlers

Emerald allows you to define 'normal' function signatures and 
automatically translates them to command variants.
```kotlin
@Command(permissions = ["foo"])
fun foo(sender: CommandSender, argument1: String, anotherArg: Int, somePlayer: Player)
```
In this case the command name would be `foo` and it would require the permission `"foo"`.
Since the first argument is a `CommandSender` command blocks, the console or players can 
execute this command. If the first argument were `Player` then only players could execute this variant.
The next arguments directly correspond to the Arguments provided by the caller. 

Such arguments are parsed automatically so for example the argument 
- `String` simply stays the same
- `Int` parses an Int
- `Double` parses Double
- `Player` retrieves player by name

So in this case the command `foo` cold be called like that
```
/foo someArg 42 SomePlayerName
```
The Arguments are then parsed and pattern matched against all handler variants. (There can be multiple handler variants per command).

## Command Pattern matching
When the extension is loaded all command handler functions are acquired
using reflection and then registered.
The emerald engine then performs analysis of the patterns (function signatures) in order
to later match them. When a command is executed the arguments are pattern matched against
the existing handler variants (function signatures) in order to call the correct handler.

## Lifecycle Methods

Lifecycle Methods like `onEnable` or `onDisable` will execute at their corresponding events. And there can be multiple of both.

## Note:
>The order at which handlers for the same lifecycle event are executed is not defined therefore if 
> there are multiple handlers they should be independent

## Event handling

Emerald already has way more features than that including 
>- Event Handling
>- Inventory Management
>- etc

However, they are not documented yet. Feel free to give the page a visit
some time in the future when there is more documentation