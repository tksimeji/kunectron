package com.tksimeji.kunectron.adapter

import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

object V1_21_3: V1_21_x() {
    override val versions: Array<String>
        get() = arrayOf("1.21.3", "1.21.4")

    override fun hasAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin): Boolean {
        return !itemStack.hasData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
    }

    override fun hideAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin) {
        itemStack.addItemFlags(*ItemFlag.entries.toTypedArray())
        itemStack.setData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
    }

    override fun showAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin) {
        itemStack.removeItemFlags(*ItemFlag.entries.toTypedArray())
        itemStack.unsetData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
    }
}