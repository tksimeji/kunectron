package com.tksimeji.kunectron.adapter

import com.tksimeji.kunectron.AdvancementToastGui
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

interface Adapter {
    val versions: Array<String>

    fun createAdvancementToast(player: Player, type: AdvancementToastGui.AdvancementType, icon: ItemStack, message: Component, plugin: JavaPlugin, onRemoved: () -> Unit)

    fun createAnvilInventory(player: Player, title: Component): AnvilInventory

    fun hasAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin): Boolean

    fun hideAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin)

    fun showAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin)
}