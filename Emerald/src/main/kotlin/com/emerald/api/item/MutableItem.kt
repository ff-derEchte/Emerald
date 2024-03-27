package com.emerald.api.item

import com.emerald.api.enchantment.Enchantment
import com.emerald.api.text.Text

sealed interface MutableItem : Item {
    override var material: Material
    override var name: Text
    override var lore: Text
}

sealed interface MutableStackableItem : StackableItem, MutableItem {
    override var amount: Short
}

sealed interface MutableDurableItem : DurableItem, MutableItem {
    override var durability: Int
}

sealed interface MutableEnchantableItem : EnchantableItem, MutableItem {
    override var enchantments: Set<Enchantment>
}

sealed interface MutableToolItem : ToolItem {
    override var material: ToolMaterial
    override var durability: Int
    override var enchantments: Set<Enchantment.ToolsEnchantment>
}

sealed interface MutableMeleeWeaponItem : MeleeWeaponItem {
    override var material: MeleeWeaponMaterial
    override var durability: Int
    override var enchantments: Set<Enchantment.MeleeWeaponsEnchantment>
}

sealed interface MutableRangeWeaponItem : RangeWeaponItem {
    override var material: RangeWeaponMaterial
    override var durability: Int
    override var enchantments: Set<Enchantment.RangedWeaponsEnchantment>
}

sealed interface MutableFishingRodItem : FishingRodItem {
    override var material: ToolMaterial
    override var durability: Int
    override var enchantments: Set<Enchantment.FishingRodEnchantment>
}