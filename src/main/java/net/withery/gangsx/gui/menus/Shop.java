package net.withery.gangsx.gui.menus;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.events.GangShopPurchaseEvent;
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

public class Shop extends GUITemplate {
    private Player player;
    private Gang gang;
    private GangsX plugin;
    private FileConfiguration shop;
    private Double balance;
    private Integer coins;

    public Shop(GangsX plugin, Player player) {
        super(plugin, plugin.getFileManager().getShopFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getShopFile().getString("TITLE")));
        this.plugin = plugin;
        this.player = player;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
        this.gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        this.shop = plugin.getFileManager().getShopFile();
        this.balance = gang.getBankBalance();
        this.coins = gang.getCoins();
        initialize();
    }

    public Integer getBalance() {
        String selection = plugin.getConfig().getString("shop.currency");
        switch (selection.toLowerCase()) {
            case "coins":
                return coins;
            case "bankBalance", "balance":
                return balance.intValue();
            default:
                plugin.log("An error occurred whilst setting currency type for gang shop, coins has been selected by default, please check your config.yml");
                return coins;
        }
    }

    public void takeBalance(String item) {
        String selection = plugin.getConfig().getString("shop.currency");
        switch (selection.toLowerCase()) {
            case "coins":
                gang.removeCoins(getItemCost(item));
                return;
            case "bankBalance", "balance":
                gang.removeBankMoney(getItemCost(item));
                return;
            default:
                gang.removeCoins(getItemCost(item));
        }
    }

    void initialize() {
        initializeItems();
        miscItems();
    }

    void initializeItems() {
        shop.getConfigurationSection("ITEMS").getKeys(false).forEach(item -> {
            setItem(getItemSlot(item, false), createItem(item), p -> {
                if (getBalance() < getItemCost(item)) {
                    p.sendMessage(plugin.getLocaleManager().getMessage("SHOP_NOT_ENOUGH_MONEY")
                            .replace("%amount%", getItemCost(item)+"")
                            .replace("%currency%", plugin.getConfig().getString("shop.currency")));
                    delete();
                    return;
                }
                delete();
                takeBalance(item);
                for (String commands : shop.getStringList("ITEMS." + item + ".COMMANDS")) {
                    commands = commands.replace("%player%", p.getName())
                            .replace("%gang%", gang.getName())
                            .replace("%cost%", getItemCost(item) + "");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);


                    gang.sendMessage(
                            plugin.getLocaleManager().getMessage("SHOP_ITEM_PURCHASED")
                            .replace("%amount%", getItemCost(item)+"")
                            .replace("%currency%", plugin.getConfig().getString("shop.currency"))
                                    .replace("%player%", p.getName())
                                    .replace("%gang%", gang.getName())
                                    .replace("%item%", shop.getString("ITEMS."+item+".NAME")));
                    return;
                }
            });
        });
    }

    void miscItems() {
        shop.getConfigurationSection("SHOP_MISC_ITEMS").getKeys(false).forEach(item -> {
            setItem(getItemSlot(item, true), createMiscItem(item));
        });
    }

    public Integer getItemSlot(String item, boolean miscitem) {
        if (miscitem) {
            return shop.getInt("SHOP_MISC_ITEMS." + item + ".SLOT");
        }
        return shop.getInt("ITEMS." + item + ".SLOT");
    }

    public boolean hasGlow(String item, boolean miscitem) {
        if (miscitem) {
            return shop.getBoolean("SHOP_MISC_ITEMS." + item + ".GLOW");
        }
        return shop.getBoolean("ITEMS." + item + ".GLOW");
    }

    public Integer getItemCost(String item) {
        return shop.getInt("ITEMS." + item + ".COST");
    }

    ItemStack createItem(String item) {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        String material = shop.getString("ITEMS." + item + ".MATERIAL");
        Integer amount = shop.getInt("ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = shop.getString("ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        String fcost = String.format("%,d", getItemCost(item));
        for (String lines : shop.getStringList("ITEMS." + item + ".LORE")) {
            lines = lines.replace("%cost%", fcost);
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

    ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = shop.getString("SHOP_MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = shop.getInt("SHOP_MISC_ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = shop.getString("SHOP_MISC_ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : shop.getStringList("SHOP_MISC_ITEMS." + item + ".LORE")) {
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
}
