package com.emerald.api.player

import com.emerald.internal.Plugin


interface PermissibleEntity : Identifiable {
    fun addPermission(permission: String) {
        Plugin.instance.permissionStorage.addPermission(uuid, permission)
    }

    fun removePermission(permission: String) {
        Plugin.instance.permissionStorage.removePermission(uuid, permission)
    }

    fun hasPermission(permission: String): Boolean
}