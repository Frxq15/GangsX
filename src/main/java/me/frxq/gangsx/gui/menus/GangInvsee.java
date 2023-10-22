package me.frxq.gangsx.gui.menus;

import me.frxq.gangsx.GangsX;
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
import java.util.concurrent.atomic.AtomicInteger;

public class GangInvsee extends GUITemplate {
    private final Player player;
    private final Player target;
    private final GangsX plugin;
    private FileConfiguration invsee;
    public GangInvsee(GangsX plugin, Player player, Player target) {
        super(plugin, 6, plugin.getColorFormatter().format(plugin.getFileManager().getInvseeFile().getString("TITLE")).replace("%player%", target.getName()));
        this.player = player;
        this.target = target;
        this.plugin = plugin;
        this.invsee = plugin.getFileManager().getInvseeFile();
        initialize();
    }
    public void initialize() {
        setInventoryItems();
        setArmor();
        invsee.getConfigurationSection("MISC_ITEMS").getKeys(false).forEach(item -> {
            setItem(invsee.getInt("MISC_ITEMS."+item+".SLOT"), createMiscItem(item));
        });
    }
    public void setInventoryItems() {
        int size = target.getInventory().getSize();
        size = size-5;
        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger slot = new AtomicInteger(18);
        while(i.get() < size) {
            setItem(slot.get(), target.getInventory().getItem(i.get()));
            i.getAndIncrement();
            slot.getAndIncrement();
        }
    }
    public void setArmor() {
        ItemStack item;
        item = target.getInventory().getHelmet();
        if(item == null) {
            setItem(invsee.getInt("ITEMS.HELMET.SLOT"), createBlankItem("HELMET"));
        } else {
            setItem(invsee.getInt("ITEMS.HELMET.SLOT"), target.getInventory().getHelmet());
        }
        item = target.getInventory().getChestplate();
        if(item == null) {
            setItem(invsee.getInt("ITEMS.CHESTPLATE.SLOT"), createBlankItem("CHESTPLATE"));
        } else {
            setItem(invsee.getInt("ITEMS.CHESTPLATE.SLOT"), target.getInventory().getChestplate());
        }
        item = target.getInventory().getLeggings();
        if(item == null) {
            setItem(invsee.getInt("ITEMS.LEGGINGS.SLOT"), createBlankItem("LEGGINGS"));
        } else {
            setItem(invsee.getInt("ITEMS.LEGGINGS.SLOT"), target.getInventory().getLeggings());
        }
        item = target.getInventory().getBoots();
        if(item == null) {
            setItem(invsee.getInt("ITEMS.BOOTS.SLOT"), createBlankItem("BOOTS"));
        } else {
            setItem(invsee.getInt("ITEMS.BOOTS.SLOT"), target.getInventory().getBoots());
        }
    }
    ItemStack createBlankItem(String item) {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        String material = invsee.getString("ITEMS." + item + ".MATERIAL");
        Integer data = invsee.getInt("ITEMS." + item + ".DATA");
        final ItemStack i = new ItemStack(Material.valueOf(material), 1, data.shortValue());
        String name = invsee.getString("ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : invsee.getStringList("ITEMS." + item + ".LORE")) {
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
            return invsee.getBoolean("MISC_ITEMS." + item + ".GLOW");
        }
        return invsee.getBoolean("ITEMS." + item + ".GLOW");
    }
    ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = invsee.getString("MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = invsee.getInt("MISC_ITEMS." + item + ".AMOUNT");
        Integer data = invsee.getInt("MISC_ITEMS." + item + ".DATA");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount, data.shortValue());
        String name = invsee.getString("MISC_ITEMS." + item + ".NAME");
        final ItemMeta meta = i.getItemMeta();
        for (String lines : invsee.getStringList("MISC_ITEMS." + item + ".LORE")) {
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
