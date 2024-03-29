package me.frxq.gangsx.gui.menus.management;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.enums.Role;
import me.frxq.gangsx.gui.GUITemplate;
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

public class PermissionsSetter extends GUITemplate {
    private final GangsX plugin;
    private final Player player;
    private final Gang gang;
    private FileConfiguration permissions;
    private final Permission permission;
    public PermissionsSetter(GangsX plugin, Player player, Gang gang, Permission permission) {
        super(plugin, plugin.getFileManager().getPermissionsManagerFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getPermissionsManagerFile().getString("TITLE")));
        this.plugin = plugin;
        this.player = player;
        this.gang = gang;
        this.permission = permission;
        this.permissions = plugin.getFileManager().getPermissionsManagerFile();
        initialize();
    }
    public void initialize() {
        if(permissions.getBoolean("USE-SKULL-TEXTURES")) {
            permissions.getConfigurationSection("ITEMS").getKeys(false).forEach(role -> {
                setItem(permissions.getInt("ITEMS."+role+".SLOT"), createTexturedItem(role), p -> {
                    p.getOpenInventory().close();
                    gang.updatePermission(permission, Role.valueOf(role));
                    gang.getOnlineMembers().forEach(om -> {
                        if(om.getRole().getPriority() < Role.valueOf(role).getPriority()) {
                            om.setChatEnabled(false);
                        }
                    });
                    gang.sendMessage(plugin.getColorFormatter().format(plugin.getLocaleManager().getMessage("PERMISSION_UPDATED"))
                            .replace("%permission%", permission.getName()).replace("%role%", Role.valueOf(role).getName()).replace("%player%", p.getName()));
                });
            });
        } else {
            permissions.getConfigurationSection("ITEMS").getKeys(false).forEach(role -> {
                setItem(permissions.getInt("ITEMS."+role+".SLOT"), createItem(role), p -> {
                    p.getOpenInventory().close();
                    gang.updatePermission(permission, Role.valueOf(role));
                    gang.getOnlineMembers().forEach(om -> {
                        if(om.getRole().getPriority() < Role.valueOf(role).getPriority()) {
                            om.setChatEnabled(false);
                        }
                    });
                    gang.sendMessage(plugin.getColorFormatter().format(plugin.getLocaleManager().getMessage("PERMISSION_UPDATED"))
                            .replace("%permission%", permission.getName()).replace("%role%", Role.valueOf(role).getName()).replace("%player%", p.getName()));
                });
            });
        }
        permissions.getConfigurationSection("MISC_ITEMS").getKeys(false).forEach(misc -> {
            if(permissions.getBoolean("MISC_ITEMS."+misc+".GO_BACK")) {
                setItem(permissions.getInt("MISC_ITEMS."+misc+".SLOT"), createMiscItem(misc), p -> {
                    p.getOpenInventory().close();
                    new Permissions(plugin, p, gang).open(p);
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
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = permissions.getString("ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : permissions.getStringList("ITEMS." + item + ".LORE")) {
            lines = lines.replace("%haspermission%", gang.getPermissionOutput(Role.valueOf(item), permission)).replace("%permission%", permission.getName());
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

        Integer amount = permissions.getInt("ITEMS." + item + ".AMOUNT");
        ItemStack i = new ItemStack(Material.PLAYER_HEAD, amount, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        URL l;

        try {
            l = new URL(permissions.getString("ITEMS."+item+".TEXTURE"));
        } catch (MalformedURLException e) {
            l = null;
        }
        textures.setSkin(l);
        meta.setOwnerProfile(profile);
        String name = permissions.getString("ITEMS." + item + ".NAME");

        for (String lines : permissions.getStringList("ITEMS." + item + ".LORE")) {
            lines = lines.replace("%haspermission%", gang.getPermissionOutput(Role.valueOf(item), permission)).replace("%permission%", permission.getName());
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
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = permissions.getString("MISC_ITEMS." + item + ".NAME");
        final ItemMeta meta = i.getItemMeta();
        for (String lines : permissions.getStringList("MISC_ITEMS." + item + ".LORE")) {
            lines = lines.replace("%gang%", gang.getName()).replace("%permission%", permission.getName());
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
