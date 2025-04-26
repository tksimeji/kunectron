package com.tksimeji.kunectron.adapter

import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

object V1_21_3: V1_21_x() {
    override val versions: Array<String>
        get() = arrayOf("1.21.3", "1.21.4", "1.21.5")

    override fun hasAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin): Boolean {
        return !itemStack.hasData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
    }

    override fun hideAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin) {
        val itemMeta = itemStack.itemMeta
        itemMeta.addItemFlags(*ItemFlag.entries.toTypedArray())
        for (attribute in Registry.ATTRIBUTE) {
            itemMeta.removeAttributeModifier(attribute)
            itemMeta.addAttributeModifier(attribute, AttributeModifier(NamespacedKey(plugin, attribute.key.key), 0.0, AttributeModifier.Operation.ADD_NUMBER))
        }
        itemStack.itemMeta = itemMeta
    }

    override fun showAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin) {
        itemStack.removeItemFlags(*ItemFlag.entries.toTypedArray())
        val itemMeta = itemStack.itemMeta
        for (attribute in Registry.ATTRIBUTE) {
            itemMeta.removeAttributeModifier(attribute)
        }
        itemStack.itemMeta = itemMeta;
    }
}