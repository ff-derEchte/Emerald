package com.emerald.api.player

import com.emerald.setup.Plugin


interface PermissibleEntity : Identifiable {
    fun addPermission(permission: String) {
        Plugin.instance.storage.addPermission(uuid, permission)
    }

    fun removePermission(permission: String) {
        Plugin.instance.storage.removePermission(uuid, permission)
    }
}