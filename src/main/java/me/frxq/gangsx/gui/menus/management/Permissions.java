package me.frxq.gangsx.gui.menus.management;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.enums.Role;
import me.frxq.gangsx.gui.GUITemplate;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Permissions extends GUITemplate {

    private final GangsX plugin;
    private final Player player;
    private final Gang gang;
    private FileConfiguration permissions;
    public Permissions(GangsX plugin, Player player, Gang gang) {
        super(plugin, plugin.getFileManager().getPermissionsFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getPermissionsFile().getString("TITLE")));
        this.plugin = plugin;
        this.player = player;
        this.gang = gang;
        this.permissions = plugin.getFileManager().getPermissionsFile();
        initialize();
    }
    public void initialize() {
        permissions.getConfigurationSection("ITEMS").getKeys(false).forEach(perm -> {
            setItem(permissions.getInt("ITEMS."+perm+".SLOT"), createItem(perm), p -> {
                p.getOpenInventory().close();
                new PermissionsSetter(plugin, p, gang, Permission.valueOf(perm)).open(p);
            });
        });
        permissions.getConfigurationSection("MISC_ITEMS").getKeys(false).forEach(misc -> {
            if(permissions.getBoolean("MISC_ITEMS."+misc+".CLOSE_INVENTORY")) {
                setItem(permissions.getInt("MISC_ITEMS."+misc+".SLOT"), createMiscItem(misc), p -> {
                    p.getOpenInventory().close();
                });
            } else {
                setItem(permissions.getInt("MISC_ITEMS."+misc+".SLOT"), createMiscItem(misc));
            }
        });
    }
    ItemStack createItem(String item) {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        String material = permissions.getString("ITEMS." + item + ".MATERIAL");
        Integer amount = permissions.getInt("ITEMS." + item + ".AMOUNT");
        Integer data = permissions.getInt("ITEMS." + item + ".DATA");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount, data.shortValue());
        String name = permissions.getString("ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : permissions.getStringList("ITEMS." + item + ".LORE")) {
            lines = lines.replace("%coleader%", gang.getPermissionOutput(Role.CO_LEADER, Permission.valueOf(item)))
                    .replace("%mod%", gang.getPermissionOutput(Role.MOD, Permission.valueOf(item)))
                    .replace("%member%", gang.getPermissionOutput(Role.MEMBER, Permission.valueOf(item)))
                    .replace("%recruit%", gang.getPermissionOutput(Role.RECRUIT, Permission.valueOf(item)));
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
            return permissions.getBoolean("MISC_ITEMS." + item + ".GLOW");
        }
        return permissions.getBoolean("ITEMS." + item + ".GLOW");
    }
    ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = permissions.getString("MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = permissions.getInt("MISC_ITEMS." + item + ".AMOUNT");
        Integer data = permissions.getInt("MISC_ITEMS." + item + ".DATA");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount, data.shortValue());
        String name = permissions.getString("MISC_ITEMS." + item + ".NAME");
        final ItemMeta meta = i.getItemMeta();
        for (String lines : permissions.getStringList("MISC_ITEMS." + item + ".LORE")) {
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
}
