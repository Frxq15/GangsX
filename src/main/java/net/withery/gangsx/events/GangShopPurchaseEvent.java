package net.withery.gangsx.events;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GangShopPurchaseEvent extends Event implements Cancellable {
    private final GangsX plugin;
    private final Player player;
    private final Gang gang;
    private final String section;
    private boolean cancelled;
    private final ItemStack item;
    private static HandlerList handlers = new HandlerList();

    public GangShopPurchaseEvent(GangsX plugin, Player player, Gang gang, String section, ItemStack item) {
        this.plugin = plugin;
        this.player = player;
        this.gang = gang;
        this.section = section;
        this.item = item;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    public Integer getCost() {
        return plugin.getFileManager().getShopFile().getInt("ITEMS." + section + ".COST");
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public ItemStack getPurchasedItem() {
        return item;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    public Gang getGang() {
        return gang;
    }
}
