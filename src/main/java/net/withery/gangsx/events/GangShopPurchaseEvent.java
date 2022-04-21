package net.withery.gangsx.events;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GangShopPurchaseEvent extends Event {
    private final GangsX plugin;
    private final Player player;
    private final Gang gang;
    private final String section;
    private boolean cancelled;

    public GangShopPurchaseEvent(GangsX plugin, Player player, Gang gang, String section) {
        this.plugin = plugin;
        this.player = player;
        this.gang = gang;
        this.section = section;
    }
    @NotNull
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
        String item = section;
        FileConfiguration shop = plugin.getFileManager().getShopFile();
        List<String> lore = new ArrayList<String>();
        String material = shop.getString("ITEMS." + item + ".MATERIAL");
        Integer amount = shop.getInt("ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = shop.getString("ITEMS." + item + ".NAME");
        final ItemMeta meta = i.getItemMeta();
        String fcost = String.format("%,d", shop.getInt("ITEMS." + item + ".COST"));
        for (String lines : shop.getStringList("ITEMS." + item + ".LORE")) {
            lines = lines.replace("%cost%", fcost);
            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        if (shop.getBoolean("ITEMS." + item + ".GLOW")) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    public Gang getGang() {
        return gang;
    }
}
