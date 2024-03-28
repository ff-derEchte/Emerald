package com.emerald.api.player


interface CommandSender : PermissibleEntity, Identifiable, Messageable {

    fun asPlayer(): Player?

}

