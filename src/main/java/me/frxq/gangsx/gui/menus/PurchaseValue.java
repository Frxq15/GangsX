package me.frxq.gangsx.gui.menus;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.Gang;
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

public class PurchaseValue extends GUITemplate {
    private Player player;
    private Gang gang;
    private GangsX plugin;
    private FileConfiguration value;
    private Double balance;
    private Integer coins;

    public PurchaseValue(GangsX plugin, Player player, Gang gang) {
        super(plugin, plugin.getFileManager().getValueFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getValueFile().getString("TITLE")));
        this.plugin = plugin;
        this.player = player;
        this.gang = gang;
        this.value = plugin.getFileManager().getValueFile();
        this.balance = gang.getBankBalance();
        this.coins = gang.getCoins();
        initialize();
    }

    public Integer getBalance() {
        return balance.intValue();
    }
    public void takeBalance(String item) {
        gang.removeBankMoney(getItemCost(item));
    }

    void initialize() {
        initializeItems();
        miscItems();
    }

    void initializeItems() {
        value.getConfigurationSection("ITEMS").getKeys(false).forEach(item -> {
            String cost = String.format("%,d",getItemCost(item));
            setItem(getItemSlot(item, false), createItem(item), p -> {
                if (getBalance() < getItemCost(item)) {
                    p.sendMessage(plugin.getLocaleManager().getMessage("SHOP_NOT_ENOUGH_MONEY")
                            .replace("%amount%", "$"+cost)
                            .replace("%currency%", "bank balance"));
                    player.getOpenInventory().close();
                    return;
                }
                player.getOpenInventory().close();
                takeBalance(item);
                gang.addValue(getItemValue(item));

                    gang.sendMessage(
                            plugin.getLocaleManager().getMessage("VALUE_ITEM_PURCHASED")
                                    .replace("%amount%", getItemCost(item)+"")
                                    .replace("%currency%", "bank balance")
                                    .replace("%player%", p.getName())
                                    .replace("%gang%", gang.getName())
                                    .replace("%item%", value.getString("ITEMS."+item+".NAME")));
            });
        });
    }

    void miscItems() {
        value.getConfigurationSection("VALUE_MISC_ITEMS").getKeys(false).forEach(item -> {
            setItem(getItemSlot(item, true), createMiscItem(item));
        });
    }

    public Integer getItemSlot(String item, boolean miscitem) {
        if (miscitem) {
            return value.getInt("VALUE_MISC_ITEMS." + item + ".SLOT");
        }
        return value.getInt("ITEMS." + item + ".SLOT");
    }

    public boolean hasGlow(String item, boolean miscitem) {
        if (miscitem) {
            return value.getBoolean("VALUE_MISC_ITEMS." + item + ".GLOW");
        }
        return value.getBoolean("ITEMS." + item + ".GLOW");
    }

    public Integer getItemCost(String item) {
        return value.getInt("ITEMS." + item + ".COST");
    }
    public Integer getItemValue(String item) {
        return value.getInt("ITEMS." + item + ".WORTH");
    }

    ItemStack createItem(String item) {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        String material = value.getString("ITEMS." + item + ".MATERIAL");
        Integer amount = value.getInt("ITEMS." + item + ".AMOUNT");
        Integer data = value.getInt("ITEMS." + item + ".DATA");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount, data.shortValue());
        String name = value.getString("ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        String fcost = String.format("%,d", getItemCost(item));
        String fvalue = String.format("%,d", getItemValue(item));
        for (String lines : value.getStringList("ITEMS." + item + ".LORE")) {
            lines = lines.replace("%cost%", fcost).replace("%worth%", fvalue);
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

    ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = value.getString("VALUE_MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = value.getInt("VALUE_MISC_ITEMS." + item + ".AMOUNT");
        Integer data = value.getInt("VALUE_MISC_ITEMS." + item + ".DATA");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount, data.shortValue());
        String name = value.getString("VALUE_MISC_ITEMS." + item + ".NAME");
        String fvalue = String.format("%,d", gang.getValue());
        final ItemMeta meta = i.getItemMeta();
        for (String lines : value.getStringList("VALUE_MISC_ITEMS." + item + ".LORE")) {
            lines = lines.replace("%gang%", gang.getName()).replace("%value%", fvalue);
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
