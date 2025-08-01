package com.tksimeji.kunectron.adapter

import com.tksimeji.kunectron.AdvancementToastGui
import com.tksimeji.kunectron.SignGui
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.DyeColor
import org.bukkit.entity.Player
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

interface Adapter {
    val versions: Array<String>

    fun sendAdvancementGranted(player: Player, key: Key, type: AdvancementToastGui.AdvancementType, icon: ItemStack, component: Component)

    fun sendAdvancementCleanup(player: Player, key: Key)

    fun sendInventoryTitleUpdate(view: InventoryView, title: Component)

    fun sendOpenAnvilScreen(player: Player, title: Component): AnvilInventory

    fun sendOpenSignEditor(
        player: Player,
        type: SignGui.SignType,
        color: DyeColor,
        texts: Array<String?>,
        isGlowing: Boolean,
        onClose: (lines: Array<String>) -> Unit
    )

    fun sendCloseSignEditor(player: Player)

    fun enchantmentGlint(itemStack: ItemStack, isOverride: Boolean)

    fun hasEnchantmentGlint(itemStack: ItemStack): Boolean

    fun hideAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin)

    fun showAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin)

    fun hasAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin): Boolean
}