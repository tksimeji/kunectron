package com.tksimeji.kunectron.element;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.Preconditions;
import com.tksimeji.mojango.Mojango;
import com.tksimeji.mojango.texture.Skin;
import com.tksimeji.kunectron.Kunectron;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class PlayerHeadElementImpl extends ItemElementImpl implements PlayerHeadElement {
    private static final Map<String, UUID> UUID_CACHE = new HashMap<>();

    private @Nullable URL url;

    public PlayerHeadElementImpl() {
        super(ItemType.PLAYER_HEAD);
        Kunectron.adapter().hideAdditionalTooltip(itemStack, Kunectron.plugin());
    }

    @Override
    public @NotNull ItemElement type(final @NotNull ItemType type) {
        if (type != ItemType.PLAYER_HEAD) {
            return new ItemElementImpl(type);
        }
        return this;
    }

    @Override
    public @NotNull PlayerHeadElement name(final @Nullable String name) {
        if (name == null) {
            return url((URL) null);
        }

        if (UUID_CACHE.containsKey(name)) {
            return uuid(UUID_CACHE.get(name));
        }

        Bukkit.getScheduler().runTaskAsynchronously(Kunectron.plugin(), () -> {
            final UUID uuid = Mojango.account(name).getUniqueId();
            uuid(uuid);
            UUID_CACHE.put(name, uuid);
        });
        return this;
    }

    @Override
    public @NotNull PlayerHeadElement uuid(final @Nullable UUID uuid) {
        if (uuid == null) {
            return url((URL) null);
        }

        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            URL skin = player.getPlayerProfile().getTextures().getSkin();
            if (skin != null) {
                return url(skin);
            }
        }

        Bukkit.getScheduler().runTaskAsynchronously(Kunectron.plugin(), () -> {
            final Skin skin = Mojango.account(uuid).getSkin();
            url(skin != null ? skin.getUrl() : null);
        });
        return this;
    }

    @Override
    public @NotNull PlayerHeadElement player(final @Nullable OfflinePlayer player) {
        return uuid(player != null ? player.getUniqueId() : null);
    }

    @Override
    public @Nullable URL url() {
        return url;
    }

    @Override
    public @NotNull PlayerHeadElement url(final @Nullable String url) {
        if (url == null) {
            this.url = null;
            return url((URL) null);
        }

        try {
            final URL uri = URI.create(url).toURL();
            return url(uri);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url format: " + url);
        }
    }

    @NotNull
    @Override
    public PlayerHeadElement url(final @Nullable URL url) {
        if (url == null) {
            this.url = null;
        } else {
            Preconditions.checkArgument(url.getHost().equalsIgnoreCase("textures.minecraft.net"), "No servers other then 'texture.minecraft.net' are allowed.");
            this.url = url;
        }

        final SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

        if (this.url == null) {
            itemMeta.setPlayerProfile(null);
            itemStack.setItemMeta(itemMeta);
            return this;
        }

        final UUID uuid = UUID_CACHE.getOrDefault(this.url.toString(), UUID.randomUUID());
        UUID_CACHE.put(this.url.toString(), uuid);

        final PlayerProfile profile = Bukkit.createProfile(uuid);
        final PlayerTextures textures = profile.getTextures();

        textures.setSkin(this.url);
        profile.setTextures(textures);
        itemMeta.setPlayerProfile(profile);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public @NotNull ItemElement createCopy() {
        final PlayerHeadElementImpl copy = createCopy(new PlayerHeadElementImpl(), itemStack);
        copy.url = url;
        return copy;
    }
}
