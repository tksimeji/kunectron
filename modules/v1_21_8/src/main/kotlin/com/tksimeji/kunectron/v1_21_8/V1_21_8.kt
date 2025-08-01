package com.tksimeji.kunectron.v1_21_8

import com.tksimeji.kunectron.AdvancementToastGui
import com.tksimeji.kunectron.SignGui
import com.tksimeji.kunectron.adapter.Adapter
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import io.papermc.paper.adventure.PaperAdventure
import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.AnvilMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.SignBlockEntity
import org.bukkit.DyeColor
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.attribute.AttributeModifier
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.event.CraftEventFactory
import org.bukkit.craftbukkit.inventory.CraftContainer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object V1_21_8 : Adapter {
    override val versions: Array<String> = arrayOf("1.21.8")

    private val fakeSignPositions: MutableMap<Int, BlockPos> = mutableMapOf()

    override fun sendAdvancementGranted(
        player: Player,
        key: Key,
        type: AdvancementToastGui.AdvancementType,
        icon: ItemStack,
        component: Component
    ) {
        val frame = when (type) {
            AdvancementToastGui.AdvancementType.TASK -> AdvancementType.TASK
            AdvancementToastGui.AdvancementType.CHALLENGE -> AdvancementType.CHALLENGE
            AdvancementToastGui.AdvancementType.GOAL -> AdvancementType.GOAL
        }

        val displayInfo = DisplayInfo(
            CraftItemStack.asNMSCopy(icon),
            PaperAdventure.asVanilla(component),
            net.minecraft.network.chat.Component.empty(),
            Optional.empty(),
            frame,
            true,
            false,
            true
        )

        val rewards = AdvancementRewards(0, emptyList(), emptyList(), Optional.empty())
        val criteria = mapOf("impossible" to Criterion(ImpossibleTrigger(), ImpossibleTrigger.TriggerInstance()))
        val requirements = AdvancementRequirements(listOf(listOf("impossible")))

        val progress = AdvancementProgress().apply {
            update(requirements)
            getCriterion("impossible")!!.grant()
        }

        val advancement = Advancement(
            Optional.empty(),
            Optional.of(displayInfo),
            rewards,
            criteria,
            requirements,
            false
        )

        val resourceLocation = ResourceLocation.fromNamespaceAndPath(key.namespace(), key.value())
        val holder = AdvancementHolder(resourceLocation, advancement)

        val packet = ClientboundUpdateAdvancementsPacket(false, setOf(holder), emptySet(), mapOf(resourceLocation to progress), false)
        (player as CraftPlayer).handle.connection.send(packet)
    }

    override fun sendAdvancementCleanup(player: Player, key: Key) {
        val resourceLocation = ResourceLocation.fromNamespaceAndPath(key.namespace(), key.value())
        val packet = ClientboundUpdateAdvancementsPacket(false, emptySet(), setOf(resourceLocation), emptyMap(), false)
        (player as CraftPlayer).handle.connection.send(packet)
    }

    override fun sendInventoryTitleUpdate(view: InventoryView, title: Component) {
        val player = view.player as? CraftPlayer ?: return
        val nmsPlayer = player.handle

        val containerId = nmsPlayer.containerMenu.containerId
        val menuType = CraftContainer.getNotchInventoryType(player.openInventory.topInventory)
        nmsPlayer.connection.send(ClientboundOpenScreenPacket(containerId, menuType, PaperAdventure.asVanilla(title)))
        nmsPlayer.containerMenu.sendAllDataToRemote()
    }

    override fun sendOpenAnvilScreen(player: Player, title: Component): AnvilInventory {
        val nmsPlayer = (player as CraftPlayer).handle

        CraftEventFactory.handleInventoryCloseEvent(nmsPlayer, InventoryCloseEvent.Reason.UNKNOWN)

        nmsPlayer.containerMenu = nmsPlayer.inventoryMenu

        val id = nmsPlayer.nextContainerCounter()
        val levelAccess = ContainerLevelAccess.create(nmsPlayer.level(), BlockPos.of(BlockPos.asLong(0, 0, 0)))
        val container = AnvilMenu(id, nmsPlayer.inventory, levelAccess)

        container.checkReachable = false
        container.title = PaperAdventure.asVanilla(title)

        val packet = ClientboundOpenScreenPacket(container.containerId, container.type, container.title)
        nmsPlayer.connection.send(packet)
        nmsPlayer.containerMenu = container
        nmsPlayer.initMenu(container)

        return container.bukkitView.topInventory
    }

    override fun sendOpenSignEditor(
        player: Player,
        type: SignGui.SignType,
        color: DyeColor,
        texts: Array<String?>,
        isGlowing: Boolean,
        onClose: (Array<String>) -> Unit
    ) {
        val nmsPlayer = (player as CraftPlayer).handle

        val block = when (type) {
            SignGui.SignType.OAK -> Blocks.OAK_SIGN
            SignGui.SignType.SPRUCE -> Blocks.SPRUCE_SIGN
            SignGui.SignType.BIRCH -> Blocks.BIRCH_SIGN
            SignGui.SignType.JUNGLE -> Blocks.JUNGLE_SIGN
            SignGui.SignType.ACACIA -> Blocks.ACACIA_SIGN
            SignGui.SignType.DARK_OAK -> Blocks.DARK_OAK_SIGN
            SignGui.SignType.MANGROVE -> Blocks.MANGROVE_SIGN
            SignGui.SignType.CHERRY -> Blocks.CHERRY_SIGN
            SignGui.SignType.PALE_OAK -> Blocks.OAK_SIGN
            SignGui.SignType.BAMBOO -> Blocks.BAMBOO_SIGN
            SignGui.SignType.CRIMSON -> Blocks.CRIMSON_SIGN
            SignGui.SignType.WAPPED -> Blocks.WARPED_SIGN
        }

        val color = when (color) {
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

        val signPos = nmsPlayer.blockPosition().also {
            fakeSignPositions[player.entityId] = it
        }

        val sign = SignBlockEntity(signPos, block.defaultBlockState())
        var signText = sign.frontText
            .setColor(color)
            .setHasGlowingText(isGlowing)

        for ((index, text) in texts.withIndex()) {
            signText = signText.setMessage(index, net.minecraft.network.chat.Component.literal(text ?: ""))
        }
        sign.setText(signText, true)

        val connection = nmsPlayer.connection

        val clientboundBlockUpdatePacket = ClientboundBlockUpdatePacket(signPos, block.defaultBlockState())
        connection.send(clientboundBlockUpdatePacket)
        connection.send(sign.updatePacket!!)

        val clientboundOpenSignEditorPacket = ClientboundOpenSignEditorPacket(signPos, true)
        connection.send(clientboundOpenSignEditorPacket)

        val channel = connection.connection.channel
        channel.pipeline().addAfter("decoder", "sign_update_listener", object : MessageToMessageDecoder<ServerboundSignUpdatePacket>() {
            override fun decode(context: ChannelHandlerContext, packet: ServerboundSignUpdatePacket, out: MutableList<Any>) {
                if (packet.pos == signPos) {
                    sendCloseSignEditor(player)
                    onClose(packet.lines)
                    channel.pipeline().remove(this)
                }
                out.add(packet)
            }
        })
    }

    override fun sendCloseSignEditor(player: Player) {
        val signPos = fakeSignPositions[player.entityId] ?: return
        val nmsPlayer = (player as CraftPlayer).handle
        val level = nmsPlayer.level()
        val blockState = level.getBlockState(signPos)
        val packet = ClientboundBlockUpdatePacket(signPos, blockState)
        nmsPlayer.connection.send(packet)
        fakeSignPositions -= player.entityId
    }

    override fun enchantmentGlint(itemStack: ItemStack, isOverride: Boolean) {
        itemStack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, isOverride)
    }

    override fun hasEnchantmentGlint(itemStack: ItemStack): Boolean = itemStack.getData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE) == true

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

    override fun hasAdditionalTooltip(itemStack: ItemStack, plugin: JavaPlugin): Boolean =
        itemStack.itemMeta.attributeModifiers?.values()?.all { it.key().namespace() == plugin.name.lowercase() } ?: false
}