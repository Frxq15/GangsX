package me.frxq.gangsx.gui.menus;

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
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Level extends GUITemplate {
    private Player player;
    private Gang gang;
    private GangsX plugin;
    private FileConfiguration level;

    public Level(GangsX plugin, Player player) {
        super(plugin, plugin.getFileManager().getLevelFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getLevelFile().getString("TITLE")));
        this.plugin = plugin;
        this.player = player;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
        this.gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        this.level = plugin.getFileManager().getLevelFile();
        initialize();
    }
    public void initialize() {
        if(level.getBoolean("USE-SKULL-TEXTURES")) {
            level.getConfigurationSection("ITEMS").getKeys(false).forEach(lvl -> {
                setItem(getItemSlot(lvl, false), createTexturedItem(lvl));
            });
        } else {
            level.getConfigurationSection("ITEMS").getKeys(false).forEach(lvl -> {
                setItem(getItemSlot(lvl, false), createItem(lvl));
            });
        }
        level.getConfigurationSection("MISC_ITEMS").getKeys(false).forEach(item -> {
            if(item.equalsIgnoreCase("CLOSE_MENU")) {
                setItem(getItemSlot(item, true), createMiscItem(item), p -> {
                    p.getOpenInventory().close();
                });
            } else {
                setItem(getItemSlot(item, true), createMiscItem(item));
            }
        });
    }
    public Integer getItemSlot(String item, boolean miscitem) {
        if (miscitem) {
            return level.getInt("MISC_ITEMS." + item + ".SLOT");
        }
        return level.getInt("ITEMS." + item + ".SLOT");
    }
    ItemStack createItem(String item) {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        String material = level.getString("ITEMS." + item + ".MATERIAL");
        Integer amount = level.getInt("ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = level.getString("ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : level.getStringList("ITEMS." + item + ".LORE")) {
            lines = lines.replace("%lore_outcome%", getLoreOutcome(Integer.parseInt(item)));
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
    ItemStack createTexturedItem(String item) {
        //textured item creation
        List<String> lore = new ArrayList<String>();

        Integer amount = level.getInt("ITEMS." + item + ".AMOUNT");
        ItemStack i = new ItemStack(Material.PLAYER_HEAD, amount, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        URL l;
        try {
            l = new URL(level.getString("ITEMS."+item+".TEXTURE"));
        } catch (MalformedURLException e) {
            l = null;
        }
        textures.setSkin(l);
        meta.setOwnerProfile(profile);
        String name = level.getString("ITEMS." + item + ".NAME");

        for (String lines : level.getStringList("ITEMS." + item + ".LORE")) {
            lines = lines.replace("%lore_outcome%", getLoreOutcome(Integer.parseInt(item)));
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
    public String getLoreOutcome(int lvl) {
        int glevel = gang.getLevel();
        if(lvl > glevel) {
            return level.getString("LORE_OUTCOMES.fail");
        }
        return level.getString("LORE_OUTCOMES.pass");
    }
    public boolean hasGlow(String item, boolean miscitem) {
        if (miscitem) {
            return level.getBoolean("MISC_ITEMS." + item + ".GLOW");
        }
        return level.getBoolean("ITEMS." + item + ".GLOW");
    }
    ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = level.getString("MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = level.getInt("MISC_ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = level.getString("MISC_ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : level.getStringList("MISC_ITEMS." + item + ".LORE")) {
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
