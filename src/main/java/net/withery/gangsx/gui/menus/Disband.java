package net.withery.gangsx.gui.menus;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.gui.GUITemplate;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Disband extends GUITemplate {
    private Player player;
    private Gang gang;
    private GangsX plugin;
    private FileConfiguration disband;

    public Disband(GangsX plugin, Player player, Gang gang) {
        super(plugin, plugin.getFileManager().getDisbandFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getDisbandFile().getString("TITLE")));
        this.plugin = plugin;
        this.player = player;
        this.gang = gang;
        this.disband = plugin.getFileManager().getDisbandFile();
        initialize();
    }
    public void initialize() {
        disband.getConfigurationSection("ITEMS").getKeys(false).forEach(item -> {
            if(item.equalsIgnoreCase("CONFIRM")) {
                disband.getStringList("ITEMS.CONFIRM.SLOTS").forEach(slot -> {
                    setItem(Integer.parseInt(slot), createItem(item), p -> {
                        p.getOpenInventory().close();
                        gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_DISBANDED").replace("%gang%", gang.getName()));
                        if(plugin.getConfig().getBoolean("gang.player_actions.disband_gang")) {
                            plugin.getLocaleManager().broadcastMessage(plugin.getLocaleManager().getMessage("BROADCAST_GANG_DISBANDED")
                                    .replace("%player%", p.getName())
                                    .replace("%gang%", gang.getName()));
                        }
                        plugin.getGangUtils().prepareDisband(gang);
                    });
                });
            } else {
                disband.getStringList("ITEMS.CANCEL.SLOTS").forEach(slot -> {
                    setItem(Integer.parseInt(slot), createItem(item), p -> {
                        p.getOpenInventory().close();
                    });
                });
            }
        });
        disband.getConfigurationSection("MISC_ITEMS").getKeys(false).forEach(item -> {
            setItem(getItemSlot(item, true), createMiscItem(item));
        });
    }
    ItemStack createItem(String item) {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        String material = disband.getString("ITEMS." + item + ".MATERIAL");
        Integer amount = disband.getInt("ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = disband.getString("ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : disband.getStringList("ITEMS." + item + ".LORE")) {
            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        if (hasGlow(item, false)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public boolean hasGlow(String item, boolean miscitem) {
        if (miscitem) {
            return disband.getBoolean("MISC_ITEMS." + item + ".GLOW");
        }
        return disband.getBoolean("ITEMS." + item + ".GLOW");
    }
    ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = disband.getString("MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = disband.getInt("MISC_ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = disband.getString("MISC_ITEMS." + item + ".NAME");
        name = name.replace("%gang%", gang.getName());

        final ItemMeta meta = i.getItemMeta();
        for (String lines : disband.getStringList("MISC_ITEMS." + item + ".LORE")) {
            lines = lines.replace("%gang%", gang.getName());
            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        if (hasGlow(item, true)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public Integer getItemSlot(String item, boolean miscitem) {
        if (miscitem) {
            return disband.getInt("MISC_ITEMS." + item + ".SLOT");
        }
        return disband.getInt("ITEMS." + item + ".SLOT");
    }
}
