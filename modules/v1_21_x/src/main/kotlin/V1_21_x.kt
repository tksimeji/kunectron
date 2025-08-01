package com.tksimeji.kunectron.adapter

import com.tksimeji.kunectron.AdvancementToastGui
import com.tksimeji.kunectron.SignGui
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.AnvilMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.SignBlockEntity
import org.bukkit.Bukkit
import org.bukkit.DyeColor
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.event.CraftEventFactory
import org.bukkit.craftbukkit.inventory.CraftContainer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

abstract class V1_21_x: Adapter {
    private val signPositions: MutableMap<UUID, BlockPos> = mutableMapOf()

    override fun advancementToast(
        player: Player,
        type: AdvancementToastGui.AdvancementType,
        icon: ItemStack,
        component: Component,
        plugin: JavaPlugin,
        then: () -> Unit
    ) {
        val nmsResourceLocation = ResourceLocation.withDefaultNamespace(UUID.randomUUID().toString())

        val nmsType = when (type) {
            AdvancementToastGui.AdvancementType.TASK -> AdvancementType.TASK
            AdvancementToastGui.AdvancementType.CHALLENGE -> AdvancementType.CHALLENGE
            AdvancementToastGui.AdvancementType.GOAL -> AdvancementType.GOAL
        }
        val nmsIcon = CraftItemStack.asNMSCopy(icon)
        val nmsMessage = PaperAdventure.asVanilla(component)

        val nmsCriteria = mapOf("impossible" to Criterion(ImpossibleTrigger(), ImpossibleTrigger.TriggerInstance()))
        val nmsRequirements = AdvancementRequirements(listOf(listOf("impossible")))

        val nmsDisplayInfo = DisplayInfo(
            nmsIcon,
            nmsMessage,
            net.minecraft.network.chat.Component.empty(),
            Optional.empty(),
            nmsType,
            true,
            false,
            true
        )
        val nmsAdvancement = Advancement(
            Optional.empty(),
            Optional.of(nmsDisplayInfo),
            AdvancementRewards(0, emptyList(), emptyList(), Optional.empty()),
            nmsCriteria,
            nmsRequirements,
            false
        )

        val nmsProgress = AdvancementProgress().apply {
            update(nmsRequirements)
            getCriterion("impossible")!!.grant()
        }

        val nmsConnection = (player as CraftPlayer).handle.connection
        nmsConnection.send(clientboundUpdateAdvancementsPacket(
            false,
            listOf(AdvancementHolder(nmsResourceLocation, nmsAdvancement)),
            emptySet(),
            mapOf(nmsResourceLocation to nmsProgress))
        )

        Bukkit.getScheduler().runTaskLater(plugin, { ->
            nmsConnection.send(clientboundUpdateAdvancementsPacket(false, emptyList(), setOf(nmsResourceLocation), emptyMap()))
            then()
        }, 1)
    }

    override fun sendOpenAnvilScreen(player: Player, title: Component): AnvilInventory {
        val nmsPlayer = (player as CraftPlayer).handle

        CraftEventFactory.handleInventoryCloseEvent(nmsPlayer, InventoryCloseEvent.Reason.UNKNOWN)
        nmsPlayer.containerMenu = nmsPlayer.inventoryMenu

        val id = nmsPlayer.nextContainerCounter()
        val container = AnvilMenu(id, nmsPlayer.inventory, ContainerLevelAccess.create(nmsPlayer.serverLevel(), BlockPos.of(BlockPos.asLong(0, 0, 0))))

        container.checkReachable = false
        container.title = PaperAdventure.asVanilla(title)

        val inventory = container.bukkitView.topInventory

        nmsPlayer.connection.send(ClientboundOpenScreenPacket(container.containerId, container.type, container.title))
        nmsPlayer.containerMenu = container
        nmsPlayer.initMenu(container)

        return inventory
    }

    override fun sendInventoryTitleUpdate(view: InventoryView, title: Component) {
        val player = view.player as? Player ?: return
        val nmsPlayer = (player as CraftPlayer).handle
        val containerId = nmsPlayer.containerMenu.containerId
        val menuType = CraftContainer.getNotchInventoryType(player.openInventory.topInventory)
        nmsPlayer.connection.send(ClientboundOpenScreenPacket(containerId, menuType, PaperAdventure.asVanilla(title)))
        player.updateInventory()
    }

    override fun sendOpenSignEditor(
        player: Player,
        signType: SignGui.SignType,
        textColor: DyeColor,
        lines: Array<String?>,
        isGlowing: Boolean,
        onClose: (Array<String>) -> Unit
    ) {
        val nmsPlayer = (player as CraftPlayer).handle

        val playerLocation = player.location
        val blockPos = BlockPos(playerLocation.blockX, playerLocation.blockY, playerLocation.blockZ)

        signPositions[player.uniqueId] = blockPos

        val block = when (signType) {
            SignGui.SignType.OAK -> Blocks.OAK_SIGN
            SignGui.SignType.SPRUCE -> Blocks.SPRUCE_SIGN
            SignGui.SignType.BIRCH -> Blocks.BIRCH_SIGN
            SignGui.SignType.JUNGLE -> Blocks.JUNGLE_SIGN
            SignGui.SignType.ACACIA -> Blocks.ACACIA_SIGN
            SignGui.SignType.DARK_OAK -> Blocks.DARK_OAK_SIGN
            SignGui.SignType.MANGROVE -> Blocks.MANGROVE_SIGN
            SignGui.SignType.CHERRY -> Blocks.CHERRY_SIGN
            SignGui.SignType.PALE_OAK -> Blocks.PALE_OAK_SIGN
            SignGui.SignType.BAMBOO -> Blocks.BAMBOO_SIGN
            SignGui.SignType.CRIMSON -> Blocks.CRIMSON_SIGN
            SignGui.SignType.WAPPED -> Blocks.WARPED_SIGN
        }

        val color = when (textColor) {
            DyeColor.WHITE -> net.minecraft.world.item.DyeColor.WHITE
            DyeColor.ORANGE -> net.minecraft.world.item.DyeColor.ORANGE
            DyeColor.MAGENTA -> net.minecraft.world.item.DyeColor.MAGENTA
            DyeColor.LIGHT_BLUE -> net.minecraft.world.item.DyeColor.LIGHT_BLUE
            DyeColor.YELLOW -> net.minecraft.world.item.DyeColor.YELLOW
            DyeColor.LIME -> net.minecraft.world.item.DyeColor.LIME
            DyeColor.PINK -> net.minecraft.world.item.DyeColor.PINK
            DyeColor.GRAY -> net.minecraft.world.item.DyeColor.GRAY
            DyeColor.LIGHT_GRAY -> net.minecraft.world.item.DyeColor.LIGHT_GRAY
            DyeColor.CYAN -> net.minecraft.world.item.DyeColor.CYAN
            DyeColor.PURPLE -> net.minecraft.world.item.DyeColor.PURPLE
            DyeColor.BLUE -> net.minecraft.world.item.DyeColor.BLUE
            DyeColor.BROWN -> net.minecraft.world.item.DyeColor.BROWN
            DyeColor.GREEN -> net.minecraft.world.item.DyeColor.GREEN
            DyeColor.RED -> net.minecraft.world.item.DyeColor.RED
            DyeColor.BLACK -> net.minecraft.world.item.DyeColor.BLACK
        }

        val sign = SignBlockEntity(blockPos, block.defaultBlockState())
        var signText = sign.frontText
            .setColor(color)
            .setHasGlowingText(isGlowing)

        for ((index, line) in lines.withIndex()) {
            signText = signText.setMessage(index, net.minecraft.network.chat.Component.literal(line ?: ""))
        }

        val connection = nmsPlayer.connection

        sign.setText(signText, true)
        player.sendBlockChange(playerLocation, block.defaultBlockState().createCraftBlockData())
        sign.level = nmsPlayer.level()
        connection.send(sign.updatePacket!!)
        sign.level = null
        connection.send(ClientboundOpenSignEditorPacket(blockPos, true))

        val channel = connection.connection.channel

        channel.pipeline().addAfter("decoder", "sign_update_listener", object : MessageToMessageDecoder<ServerboundSignUpdatePacket>() {
            override fun decode(ctx: ChannelHandlerContext, packet: ServerboundSignUpdatePacket, out: MutableList<Any>) {
                if (packet.pos == blockPos) {
                    closeSign(player)
                    onClose(packet.lines)
                    channel.pipeline().remove(this)
                }
                out.add(packet)
            }
        })
    }

    override fun closeSign(player: Player) {
        val blockPos = signPositions[player.uniqueId] ?: return
        val nmsPlayer = (player as CraftPlayer).handle
        val serverLevel = nmsPlayer.level()
        val blockState = serverLevel.getBlockState(blockPos)
        nmsPlayer.connection.send(ClientboundBlockUpdatePacket(blockPos, blockState))
    }

    abstract fun clientboundUpdateAdvancementsPacket(
        reset: Boolean,
        toAdd: Collection<AdvancementHolder>,
        toRemove: Set<ResourceLocation>,
        toSetProgress: Map<ResourceLocation, AdvancementProgress>
    ): Packet<*>
}