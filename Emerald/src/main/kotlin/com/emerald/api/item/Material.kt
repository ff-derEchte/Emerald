package com.emerald.api.item

sealed interface MeleeWeaponMaterial : Material
sealed interface ToolMaterial : Material
sealed interface BlockMaterial : Material
sealed interface RangeWeaponMaterial : Material
sealed interface ArmorMaterial : Material
sealed interface HoeMaterial : Material
sealed interface PickaxeMaterial : Material
sealed interface ShovelMaterial : Material

sealed interface Material {
    // Melee weapon materials
    data object DiamondSword : MeleeWeaponMaterial
    data object IronSword : MeleeWeaponMaterial
    data object GoldSword : MeleeWeaponMaterial
    data object NetheriteSword : MeleeWeaponMaterial
    data object StoneSword : MeleeWeaponMaterial
    data object WoodenSword : MeleeWeaponMaterial

    // Tool materials
    data object DiamondAxe : ToolMaterial, MeleeWeaponMaterial
    data object GoldAxe : ToolMaterial, MeleeWeaponMaterial
    data object IronAxe : ToolMaterial, MeleeWeaponMaterial
    data object StoneAxe : ToolMaterial, MeleeWeaponMaterial
    data object WoodenAxe : ToolMaterial, MeleeWeaponMaterial
    data object NetheriteAxe : ToolMaterial, MeleeWeaponMaterial

    data object DiamondPickaxe : ToolMaterial, PickaxeMaterial
    data object GoldPickaxe : ToolMaterial, PickaxeMaterial
    data object IronPickaxe : ToolMaterial, PickaxeMaterial
    data object StonePickaxe : ToolMaterial, PickaxeMaterial
    data object WoodenPickaxe : ToolMaterial, PickaxeMaterial
    data object NetheritePickaxe : ToolMaterial, PickaxeMaterial

    data object DiamondShovel : ToolMaterial, ShovelMaterial
    data object GoldShovel : ToolMaterial, ShovelMaterial
    data object IronShovel : ToolMaterial, ShovelMaterial
    data object StoneShovel : ToolMaterial, ShovelMaterial
    data object WoodenShovel : ToolMaterial, ShovelMaterial
    data object NetheriteShovel : ToolMaterial, ShovelMaterial

    data object DiamondHoe : ToolMaterial, HoeMaterial
    data object GoldHoe : ToolMaterial, HoeMaterial
    data object IronHoe : ToolMaterial, HoeMaterial
    data object StoneHoe : ToolMaterial, HoeMaterial
    data object WoodenHoe : ToolMaterial, HoeMaterial
    data object NetheriteHoe : ToolMaterial, HoeMaterial

    // Armor materials
    data object DiamondHelmet : ArmorMaterial
    data object DiamondChestplate : ArmorMaterial
    data object DiamondLeggings : ArmorMaterial
    data object DiamondBoots : ArmorMaterial

    data object GoldHelmet : ArmorMaterial
    data object GoldChestplate : ArmorMaterial
    data object GoldLeggings : ArmorMaterial
    data object GoldBoots : ArmorMaterial

    data object IronHelmet : ArmorMaterial
    data object IronChestplate : ArmorMaterial
    data object IronLeggings : ArmorMaterial
    data object IronBoots : ArmorMaterial

    data object ChainmailHelmet : ArmorMaterial
    data object ChainmailChestplate : ArmorMaterial
    data object ChainmailLeggings : ArmorMaterial
    data object ChainmailBoots : ArmorMaterial

    data object LeatherHelmet : ArmorMaterial
    data object LeatherChestplate : ArmorMaterial
    data object LeatherLeggings : ArmorMaterial
    data object LeatherBoots : ArmorMaterial

    data object NetheriteHelmet : ArmorMaterial
    data object NetheriteChestplate : ArmorMaterial
    data object NetheriteLeggings : ArmorMaterial
    data object NetheriteBoots : ArmorMaterial

    // Add other materials as needed
}