package com.tksimeji.kunectron.adapter

import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin

object V1_21_1: V1_21_x() {
    override val versions: Array<String>
        get() = arrayOf("1.21.1")

    override fun resetAttributeModifiers(itemStack: ItemStack, itemMeta: ItemMeta, plugin: JavaPlugin) {
        for (attribute in Registry.ATTRIBUTE) {
            itemMeta.removeAttributeModifier(attribute)
            itemMeta.addAttributeModifier(attribute, AttributeModifier(NamespacedKey(plugin, attribute.key.key), 0.0, AttributeModifier.Operation.ADD_NUMBER))
        }
    }
}