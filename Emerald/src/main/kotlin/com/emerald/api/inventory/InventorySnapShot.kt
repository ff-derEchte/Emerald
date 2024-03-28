package com.emerald.api.inventory

import java.time.Instant

interface InventorySnapShot : InventoryView {
    val creationDate: Instant
}