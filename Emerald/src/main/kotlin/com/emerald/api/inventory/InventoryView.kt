package com.emerald.api.inventory

import com.emerald.api.item.Item

interface InventoryView {
    val dimensions: Dimensions

    /**
     * @throws InventoryAccessException
     */
    fun get(x: Int, y: Int): Item?

    fun slice(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): InventoryView

    suspend fun forEachPosition(closure: suspend (x: Int, y: Int, item: Item?) -> Unit){
        for (x in 0..<dimensions.x)
            for(y in 0..<dimensions.y)
                closure(x, y, get(x, y))
    }
}

interface LiveInventoryView : InventoryView {
    suspend fun snapShot(): InventorySnapShot

    override fun slice(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): LiveInventoryView
}

interface MutableInventoryView : InventoryView {

    /**
     * @throws InventoryAccessException
     */
    suspend fun set(x: Int, y: Int, item: Item?)

    suspend fun clear()

    override fun slice(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): MutableInventoryView

}

interface LiveMutableInventoryView : MutableInventoryView, LiveInventoryView {
    suspend fun loadSnapShot(snapShot: InventorySnapShot) {
        snapShot.forEachPosition { x, y, item -> set(x, y, item) }
    }

    override fun slice(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): LiveMutableInventoryView
}

