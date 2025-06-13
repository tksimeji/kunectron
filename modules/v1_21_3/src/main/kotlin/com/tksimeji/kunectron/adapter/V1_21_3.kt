package com.tksimeji.kunectron.adapter

import io.papermc.paper.datacomponent.DataComponentTypes
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

object V1_21_3: V1_21_x() {
    override val versions: Array<String>
        get() = arrayOf("1.21.3", "1.21.4")

    override fun clientboundUpdateAdvancementsPacket(
        reset: Boolean,
        toAdd: Collection<AdvancementHolder>,
        toRemove: Set<ResourceLocation>,
        toSetProgress: Map<ResourceLocation, AdvancementProgress>
    ): Packet<*> = ClientboundUpdateAdvancementsPacket(reset, toAdd, toRemove, toSetProgress)

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