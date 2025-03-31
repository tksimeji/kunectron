package com.tksimeji.kunectron.adapter

import com.tksimeji.kunectron.AdvancementToastGui
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.AnvilMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.event.CraftEventFactory
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

abstract class V1_21_x: Adapter {
    override fun createAdvancementToast(player: Player, type: AdvancementToastGui.AdvancementType, icon: ItemStack, message: Component, plugin: JavaPlugin, onRemoved: () -> Unit) {
        val resourceLocation = ResourceLocation.withDefaultNamespace(UUID.randomUUID().toString())

        val nmsType = when (type) {
            AdvancementToastGui.AdvancementType.TASK -> AdvancementType.TASK
            AdvancementToastGui.AdvancementType.CHALLENGE -> AdvancementType.CHALLENGE
            AdvancementToastGui.AdvancementType.GOAL -> AdvancementType.GOAL
        }
        val nmsIcon = CraftItemStack.asNMSCopy(icon)
        val nmsMessage = PaperAdventure.asVanilla(message)

        val criteria = mapOf(Pair("impossible", Criterion(ImpossibleTrigger(), ImpossibleTrigger.TriggerInstance())))
        val requirements = AdvancementRequirements(listOf(listOf("impossible")))

        val displayInfo = DisplayInfo(nmsIcon, nmsMessage, net.minecraft.network.chat.Component.empty(), Optional.empty(), nmsType, true, false, true)
        val advancement = Advancement(Optional.empty(), Optional.of(displayInfo), AdvancementRewards(0, emptyList(), emptyList(), Optional.empty()), criteria, requirements, false)

        val progress = AdvancementProgress().apply {
            update(requirements)
            getCriterion("impossible")!!.grant()
        }

        val connection = (player as CraftPlayer).handle.connection
        connection.send(ClientboundUpdateAdvancementsPacket(false, listOf(AdvancementHolder(resourceLocation, advancement)), emptySet(), mapOf(Pair(resourceLocation, progress))))

        Bukkit.getScheduler().runTaskLater(plugin, { ->
            connection.send(ClientboundUpdateAdvancementsPacket(false, emptyList(), setOf(resourceLocation), emptyMap()))
            onRemoved()
        }, 1)
    }

    override fun createAnvilInventory(player: Player, title: Component): AnvilInventory {
        val nmsPlayer = (player as CraftPlayer).handle

        CraftEventFactory.handleInventoryCloseEvent(nmsPlayer, InventoryCloseEvent.Reason.UNKNOWN)
        nmsPlayer.containerMenu = nmsPlayer.inventoryMenu

        val id = nmsPlayer.nextContainerCounter()
        val container = AnvilMenu(id, nmsPlayer.inventory, ContainerLevelAccess.create(nmsPlayer.serverLevel(), BlockPos.of(BlockPos.asLong(0, 0, 0))))

        container.checkReachable = false
        container.title = PaperAdventure.asVanilla(title)

        val inventory = container.bukkitView.topInventory

        nmsPlayer.connection.sendPacket(ClientboundOpenScreenPacket(container.containerId, container.type, container.title))
        nmsPlayer.containerMenu = container
        nmsPlayer.initMenu(container)

        return inventory
    }
}