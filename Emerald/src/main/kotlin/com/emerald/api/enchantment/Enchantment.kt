package com.emerald.api.enchantment

import org.w3c.dom.ranges.Range

sealed interface Enchantment {
    sealed interface ArmorEnchantment : Enchantment
    sealed interface MeleeWeaponsEnchantment : Enchantment
    sealed interface RangedWeaponsEnchantment : Enchantment
    sealed interface ToolsEnchantment : Enchantment
    sealed interface FishingRodEnchantment : Enchantment

    data object Mending : ArmorEnchantment, MeleeWeaponsEnchantment, RangedWeaponsEnchantment, ToolsEnchantment, FishingRodEnchantment
    data class Unbreaking(val level: Byte) : ArmorEnchantment, MeleeWeaponsEnchantment, RangedWeaponsEnchantment, ToolsEnchantment, FishingRodEnchantment
    data object CurseOfVanishing : ArmorEnchantment, MeleeWeaponsEnchantment, RangedWeaponsEnchantment, ToolsEnchantment, FishingRodEnchantment

    //armor

    data object AquaAffinity : ArmorEnchantment
    data class BlastProtection(val level: Byte) : ArmorEnchantment
    data class DepthStrider(val level: Byte) : ArmorEnchantment
    data class FeatherFalling(val level: Byte) : ArmorEnchantment
    data class FireProtection(val level: Byte) : ArmorEnchantment
    data class FrostWalker(val level: Byte) : ArmorEnchantment
    data class ProjectileProtection(val level: Byte) : ArmorEnchantment
    data class Protection(val level: Byte) : ArmorEnchantment
    data class Respiration(val level: Byte) : ArmorEnchantment
    data class SoulSpeed(val level: Byte) : ArmorEnchantment
    data class Thorns(val level: Byte) : ArmorEnchantment
    data class SwiftSneak(val level: Byte) : ArmorEnchantment

    //melee weapons

    data class BaneOfArthropods(val level: Byte): MeleeWeaponsEnchantment
    data class Efficiency(val level: Byte): MeleeWeaponsEnchantment, ToolsEnchantment
    data class FireAspect(val level: Byte): MeleeWeaponsEnchantment
    data class Looting(val level: Byte): MeleeWeaponsEnchantment

    data class Impaling(val level: Byte): MeleeWeaponsEnchantment, RangedWeaponsEnchantment
    data class Knockback(val level: Byte) : MeleeWeaponsEnchantment
    data class Sharpness(val level: Byte): MeleeWeaponsEnchantment
    data class Smite(val level: Byte): MeleeWeaponsEnchantment
    data class SweepingEdge(val level: Byte): MeleeWeaponsEnchantment

    //ranged weapons

    data object Channeling : RangedWeaponsEnchantment
    data object Flame : RangedWeaponsEnchantment
    data object Infinity : RangedWeaponsEnchantment
    data class Loyalty(val level: Byte): RangedWeaponsEnchantment
    data class Riptide(val level: Byte): RangedWeaponsEnchantment
    data class Piercing(val level: Byte): RangedWeaponsEnchantment
    data object MultiShot : RangedWeaponsEnchantment
    data class Power(val level: Byte): RangedWeaponsEnchantment
    data class Punch(val level: Byte): RangedWeaponsEnchantment
    data class QuickCharge(val level: Byte): RangedWeaponsEnchantment

    //tools

    data class Fortune(val level: Byte): ToolsEnchantment
    data class LuckOfTheSea(val level: Byte): FishingRodEnchantment
    data class Lure(val level: Byte): FishingRodEnchantment

    data object SilkTouch : ToolsEnchantment

}