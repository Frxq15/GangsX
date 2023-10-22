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

public class AddToRoster extends GUITemplate {
    private Player player;
    private Gang gang;
    private GangsX plugin;
    private FileConfiguration roster;

    public AddToRoster(GangsX plugin, Player player, Gang gang) {
        super(plugin, plugin.getFileManager().getAddToRosterFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getAddToRosterFile().getString("TITLE")));
        this.plugin = plugin;
        this.player = player;
        this.gang = gang;
        this.roster = plugin.getFileManager().getAddToRosterFile();
        initialize();
    }
    public void initialize() {
        AtomicInteger start = new AtomicInteger(0);
        List<GPlayer> available = new ArrayList<>(gang.getOnlineMembers());
        gang.getFightRoster().forEach(f -> available.remove(f));


        if(available.size() == 0) {
            setItem(roster.getInt("ITEMS.NONE_AVAILABLE.SLOT"), createEmptyItem());
        } else {
            available.forEach(a -> {
                setItem(start.get(), createRosterItem(a), action -> {
                    gang.addToFightRoster(a);
                    gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_ROSTER_ADDED")
                            .replace("%target%", a.getName())
                            .replace("%player%", action.getName()));
                    action.getOpenInventory().close();
                    new Roster(plugin, action, gang).open(action);
                });
            });
        }
        available.forEach(member -> {
            setItem(start.get(), createRosterItem(member));
        });
        setItem(roster.getInt("ITEMS.CLOSE_MENU.SLOT"), createCloseItem(), player -> {
            player.getOpenInventory().close();
            new Roster(plugin, player, gang).open(player);
        });
        if(roster.getConfigurationSection("MISC_ITEMS") != null) {
            roster.getConfigurationSection("MISC_ITEMS").getKeys(false).forEach(item -> {
                setItem(getItemSlot(item, true), createMiscItem(item));
            });
        }
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
        String name = roster.getString("ITEMS.MEMBERS.NAME")
                .replace("%player%", gPlayer.getName()).replace("%role%", gPlayer.getRole().getName());


        for (String lines : roster.getStringList("ITEMS.MEMBERS.LORE")) {
            lines = lines.replace("%gang%", gang.getName())
                    .replace("%kills%", gPlayer.getKills()+"")
                    .replace("%deaths%", gPlayer.getDeaths()+"").replace("%role%", gPlayer.getRole().getName());

            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public ItemStack createCloseItem() {
        ItemStack i = new ItemStack(Material.getMaterial(roster.getString("ITEMS.CLOSE_MENU.MATERIAL")), 1);
        String name = roster.getString("ITEMS.CLOSE_MENU.NAME");
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        i.setItemMeta(meta);
        return i;
    }
    public ItemStack createEmptyItem() {
        List<String> lore = new ArrayList<String>();
        ItemStack i = new ItemStack(Material.getMaterial(roster.getString("ITEMS.NONE_AVAILABLE.MATERIAL")), 1);
        String name = roster.getString("ITEMS.NONE_AVAILABLE.NAME");
        ItemMeta meta = i.getItemMeta();
        for (String lines : roster.getStringList("ITEMS.NONE_AVAILABLE.LORE")) {
            lore.add(plugin.getColorFormatter().format(lines));
        }
        if (hasGlow("NONE_AVAILABLE", false)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = roster.getString("MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = roster.getInt("MISC_ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
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
