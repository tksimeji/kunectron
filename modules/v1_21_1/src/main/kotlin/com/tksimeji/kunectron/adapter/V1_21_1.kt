package com.tksimeji.kunectron.adapter

import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementProgress
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket
import net.minecraft.resources.ResourceLocation
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

object V1_21_1: V1_21_x() {
    override val versions: Array<String>
        get() = arrayOf("1.21.1")

    override fun clientboundUpdateAdvancementsPacket(
        reset: Boolean,
        toAdd: Collection<AdvancementHolder>,
        toRemove: Set<ResourceLocation>,
        toSetProgress: Map<ResourceLocation, AdvancementProgress>
    ): Packet<*> = ClientboundUpdateAdvancementsPacket(reset, toAdd, toRemove, toSetProgress)

    override fun hasAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin): Boolean {
        val itemMeta = itemStack.itemMeta
        return itemMeta.attributeModifiers?.values()?.all { it.key().namespace() == plugin.name.lowercase() } ?: false
    }

    override fun hideAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin) {
        itemStack.addItemFlags(*ItemFlag.entries.toTypedArray())
        val itemMeta = itemStack.itemMeta
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
        itemStack.itemMeta = itemMeta
    }
}