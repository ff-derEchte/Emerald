package com.emerald.api.inventory

import com.emerald.api.item.Item
import com.emerald.util.sync
import org.bukkit.inventory.ItemStack
import java.time.Instant
import java.util.*

internal typealias BukkitInventory = org.bukkit.inventory.Inventory


sealed class InventoryWrapper(
    final override val dimensions: Dimensions,
    protected open val inv: BukkitInventory,
): LiveMutableInventoryView {

    private val inventory: Array<Array<Item?>> = Array(dimensions.x) { Array(dimensions.y) { null } }

    override fun get(x: Int, y: Int): Item? {
        dimensions.checkBounds(x, y)
        return inventory[x][y]
    }


    override suspend fun set(x: Int, y: Int, item: Item?) {
        dimensions.checkBounds(x, y)
        inventory[x][y] = item
        sync { inv.setItem(x+dimensions.x*y, item?.toBukkitItem()) }
    }


    override suspend fun clear() {
        sync { inv.contents = Array<ItemStack?>(dimensions.x*dimensions.y) { null } }
    }

    suspend fun clearRange(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int) {
        for (x in xStart..<xEnd) {
            for (y in yStart..<yEnd) {
                set(x, y, null)
            }
        }
    }

    override fun slice(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): LiveMutableInventoryView {
        dimensions.checkBounds(xStart, yStart)
        dimensions.checkBounds(xEnd-1, yEnd-1)
        return MutableInventorySliceImpl(
            this,
            xStart,
            yStart,
            Dimensions(xEnd- yStart, yEnd - yStart)
        )
    }

    override suspend fun snapShot(): InventorySnapShot {
        return InventorySnapShotImpl(inventory, dimensions)
    }


    internal fun bukkitInventory(): BukkitInventory = inv
}

class MutableInventorySliceImpl(
    private val parent: InventoryWrapper,
    private val offsetX: Int,
    private val offsetY: Int,
    override val dimensions: Dimensions
) : LiveMutableInventoryView {

    override suspend fun set(x: Int, y: Int, item: Item?) {
        dimensions.checkBounds(x, y)
        parent.set(x+offsetX, y+offsetY, item)
    }

    override fun get(x: Int, y: Int): Item? {
        dimensions.checkBounds(x, y)
        return parent.get(x+offsetX, y+offsetY)
    }

    override suspend fun snapShot(): InventorySnapShot {
        val contents = Array(dimensions.x) { Array<Item?>(dimensions.y) { null } }

        forEachPosition { x, y, item -> contents[x][y] = item }
        return InventorySnapShotImpl(contents, dimensions, contentsCopied = true)
    }


    override suspend fun clear() {
        parent.clearRange(offsetX, offsetY, offsetX+dimensions.x, offsetY+dimensions.y)
    }

    override fun slice(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): LiveMutableInventoryView {
        dimensions.checkBounds(xStart, yStart)
        dimensions.checkBounds(xEnd-1, yEnd-1)
        return MutableInventorySliceImpl(
            parent,
            offsetX+xStart,
            offsetX+yStart,
            Dimensions(xEnd- yStart, yEnd - yStart)
        )
    }

}

class InventorySliceImpl(
    private val parent: InventoryView,
    private val offsetX: Int,
    private val offsetY: Int,
    override val dimensions: Dimensions
) : InventoryView {
    override fun get(x: Int, y: Int): Item? {
        dimensions.checkBounds(x, y)
        return parent.get(x+offsetX, y+offsetY)
    }

    override fun slice(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): InventoryView {
        dimensions.checkBounds(xStart, yStart)
        dimensions.checkBounds(xEnd-1, yEnd-1)
        return InventorySliceImpl(parent, offsetX+xStart, offsetY+yStart, Dimensions(xEnd- xStart, yEnd- yStart))
    }

}

class InventorySnapShotImpl(contents: Array<Array<Item?>>, override val dimensions: Dimensions, contentsCopied: Boolean = false): InventorySnapShot {
    private val contents: Array<Array<Item?>> = if (contentsCopied) contents else Array(dimensions.x) { i -> contents[i].copyOf() }
    override val creationDate: Instant = Instant.now()

    override fun get(x: Int, y: Int): Item? {
        dimensions.checkBounds(x, y)
        return contents[x][y]
    }

    override fun slice(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): InventoryView {
        dimensions.checkBounds(xStart, yStart)
        dimensions.checkBounds(xEnd-1, yEnd-1)
        return InventorySliceImpl(this, xStart, yStart, Dimensions(xEnd- xStart, yEnd- yStart))
    }

}