package me.frxq.gangsx.gui.menus.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.gui.GUITemplate;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Roster extends GUITemplate {
    private Player player;
    private Gang gang;
    private GangsX plugin;
    private FileConfiguration roster;

    public Roster(GangsX plugin, Player player, Gang gang) {
        super(plugin, plugin.getFileManager().getRosterFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getRosterFile().getString("TITLE")));
        this.plugin = plugin;
        this.player = player;
        this.gang = gang;
        this.roster = plugin.getFileManager().getRosterFile();
        initialize();
    }
    public void initialize() {
        int max = plugin.getConfig().getInt("gang.max-roster-size");
        AtomicInteger start = new AtomicInteger(0);

        if(gang.getFightRoster().isEmpty()) {
            while(start.get() < max) {
                setItem(start.get(), createEmptyItem(), e -> {
                    e.getOpenInventory().close();
                    new AddToRoster(plugin, e, gang).open(e);
                });
                start.getAndIncrement();
            }
        } else {
            gang.getFightRoster().forEach(fplayer -> {
                setItem(start.get(), createRosterItem(fplayer), t -> {
                    t.getOpenInventory().close();
                    gang.removeFromFightRoster(fplayer);
                    gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_ROSTER_REMOVED")
                            .replace("%target%", fplayer.getName())
                            .replace("%player%", t.getName()));
                    new Roster(plugin, t, gang).open(t);

                });
                start.getAndIncrement();
            });
            while(start.get() < max) {
                setItem(start.get(), createEmptyItem(), e -> {
                    e.getOpenInventory().close();
                    new AddToRoster(plugin, e, gang).open(e);
                });
                start.getAndIncrement();
            }
        }
        setItem(roster.getInt("ITEMS.CLOSE_MENU.SLOT"), createCloseItem(), player -> {
            player.getOpenInventory().close();
        });
        if(roster.getConfigurationSection("MISC_ITEMS") != null) {
            roster.getConfigurationSection("MISC_ITEMS").getKeys(false).forEach(item -> {
                setItem(getItemSlot(item, true), createMiscItem(item));
            });
        }
    }
    ItemStack createEmptyItem() {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        String material = roster.getString("ITEMS.NO_PLAYER_SELECTED.MATERIAL");
        Integer amount = roster.getInt("ITEMS.NO_PLAYER_SELECTED.AMOUNT");
        Integer data = roster.getInt("ITEMS.NO_PLAYER_SELECTED.DATA");
        final ItemStack i = new ItemStack(Material.valueOf(material), 1, data.shortValue());
        String name = roster.getString("ITEMS.NO_PLAYER_SELECTED.NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : roster.getStringList("ITEMS.NO_PLAYER_SELECTED.LORE")) {
            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        if (hasGlow("NO_PLAYER_SELECTED", false)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public Integer getItemSlot(String item, boolean miscitem) {
        if (miscitem) {
            return roster.getInt("MISC_ITEMS." + item + ".SLOT");
        }
        return roster.getInt("ITEMS." + item + ".SLOT");
    }
    public boolean hasGlow(String item, boolean miscitem) {
        if (miscitem) {
            return roster.getBoolean("MISC_ITEMS." + item + ".GLOW");
        }
        return roster.getBoolean("ITEMS." + item + ".GLOW");
    }
    public ItemStack createRosterItem(GPlayer gPlayer) {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        meta.setOwner(gPlayer.getName());
        String name = roster.getString("ITEMS.MEMBER_CURRENT.NAME")
                .replace("%player%", gPlayer.getName());


        for (String lines : roster.getStringList("ITEMS.MEMBER_CURRENT.LORE")) {
            lines = lines.replace("%gang%", gang.getName())
                    .replace("%kills%", gPlayer.getKills()+"")
                    .replace("%deaths%", gPlayer.getDeaths()+"");

            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public ItemStack createCloseItem() {
        Integer data = roster.getInt("ITEMS.CLOSE_MENU.DATA");
        ItemStack i = new ItemStack(Material.getMaterial(roster.getString("ITEMS.CLOSE_MENU.MATERIAL")), 1, data.shortValue());
        String name = roster.getString("ITEMS.CLOSE_MENU.NAME");
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        i.setItemMeta(meta);
        return i;
    }
    ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = roster.getString("MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = roster.getInt("MISC_ITEMS." + item + ".AMOUNT");
        Integer data = roster.getInt("MISC_ITEMS." + item + ".DATA");
        final ItemStack i = new ItemStack(Material.valueOf(material), 1, data.shortValue());
        String name = roster.getString("MISC_ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : roster.getStringList("MISC_ITEMS." + item + ".LORE")) {
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
}
