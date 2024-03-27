package com.emerald.api.item

import com.emerald.api.enchantment.Enchantment
import com.emerald.api.text.Text
import org.bukkit.inventory.ItemStack

sealed interface Item {
    val material: Material
    val name: Text
    val lore: Text

    fun toBukkitItem(): ItemStack

    fun toMutableItem(): MutableItem
}

sealed interface StackableItem : Item {
    val amount: Short

}

sealed interface DurableItem : Item {
    val durability: Int


}

sealed interface EnchantableItem : Item {
    val enchantments: Set<Enchantment>
}

sealed interface ToolItem : DurableItem, EnchantableItem {
    override val material: ToolMaterial
    override val durability: Int
    override val enchantments: Set<Enchantment.ToolsEnchantment>
}

sealed interface MeleeWeaponItem : DurableItem, EnchantableItem {
    override val material: MeleeWeaponMaterial
    override val durability: Int
    override val enchantments: Set<Enchantment.MeleeWeaponsEnchantment>
}

sealed interface RangeWeaponItem : DurableItem, EnchantableItem {
    override val material: RangeWeaponMaterial
    override val durability: Int
    override val enchantments: Set<Enchantment.RangedWeaponsEnchantment>
}

sealed interface FishingRodItem : DurableItem, EnchantableItem {
    override val material: ToolMaterial
    override val durability: Int
    override val enchantments: Set<Enchantment.FishingRodEnchantment>
}

