package net.withery.gangsx.gui.menus.gangtop;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.formatting.number.NumberFormatter;
import net.withery.gangsx.gui.GUITemplate;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
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
import java.util.concurrent.atomic.AtomicInteger;

public class Value extends GUITemplate {
    private GangsX plugin;
    private Player player;
    private FileConfiguration gangtop;

    public Value(GangsX plugin, Player player) {
        super(plugin, plugin.getFileManager().getTopFile().getInt("ROWS"),
                plugin.getColorFormatter().format(plugin.getFileManager().getTopFile().getString("TITLE"))
                        .replace("%type%", "Value"));
        this.plugin = plugin;
        this.player = player;
        this.gangtop = plugin.getFileManager().getTopFile();
        initialize();
    }
    public void initialize() {
        int display = gangtop.getInt("DISPLAY_AMOUNT");
        AtomicInteger count = new AtomicInteger(1);

        while(count.get() <= display) {
            if(plugin.getLeaderboardManager().getGangByPosition(count.get()) == null) {
                setItem(gangtop.getInt("DISPLAY_SLOTS."+count.get()), createNullLeaderboardItem(count.get()));
                count.getAndIncrement();
            } else {
                Gang gang = plugin.getLeaderboardManager().getGangByPosition(count.get());
                setItem(gangtop.getInt("DISPLAY_SLOTS."+count.get()), createLeaderboardItem(gang, count.get()), p -> {
                    player.getOpenInventory().close();
                    Bukkit.dispatchCommand(player, "gang info "+gang.getName());
                });
                count.getAndIncrement();
            }
        }
        setItem(gangtop.getInt("VALUE_ITEMS.CLOSE_MENU.SLOT"), createCloseItem(), player -> {
            player.getOpenInventory().close();
        });
        gangtop.getConfigurationSection("VALUE_MISC_ITEMS").getKeys(false).forEach(item -> {
            setItem(gangtop.getInt("VALUE_MISC_ITEMS."+item+".SLOT"), createMiscItem(item));
        });
    }
    public ItemStack createLeaderboardItem(Gang gang, int position) {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        ItemStack i = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        meta.setOwner(Bukkit.getOfflinePlayer(gang.getLeader()).getName());
        String name = gangtop.getString("VALUE_ITEMS.TOP_FORMAT.NAME")
                .replace("%gang%", gang.getName()).replace("%place%", position+"");

        String value = NumberFormatter.format(gang.getValue());


        for (String lines : gangtop.getStringList("VALUE_ITEMS.TOP_FORMAT.LORE")) {
            lines = lines.replace("%gang%", gang.getName())
                    .replace("%place%", position+"")
                    .replace("%value%", value)
                    .replace("%leader%", Bukkit.getOfflinePlayer(gang.getLeader()).getName())
                    .replace("%level%", gang.getLevel()+"")
                    .replace("%blocksmined%", gang.getBlocksBroken()+"")
                    .replace("%kills%", gang.getKills()+"")
                    .replace("%coins%", gang.getCoins()+"");

            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public ItemStack createNullLeaderboardItem(int position) {
        //normal item creation
        List<String> lore = new ArrayList<String>();

        ItemStack i = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        URL l;

        try {
            l = new URL(gangtop.getString("VALUE_ITEMS.GANG_NOT_FOUND.SKULL-TEXTURE"));
        } catch (MalformedURLException e) {
            l = null;
        }
        textures.setSkin(l);
        meta.setOwnerProfile(profile);
        String name = gangtop.getString("VALUE_ITEMS.GANG_NOT_FOUND.NAME")
                .replace("%gang%", "None").replace("%place%", position+"");

        for (String lines : gangtop.getStringList("VALUE_ITEMS.GANG_NOT_FOUND.LORE")) {
            lines = lines.replace("%place%", position+"");
            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public ItemStack createCloseItem() {
        ItemStack i = new ItemStack(Material.getMaterial(gangtop.getString("VALUE_ITEMS.CLOSE_MENU.MATERIAL")), 1);
        String name = gangtop.getString("VALUE_ITEMS.CLOSE_MENU.NAME");
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        i.setItemMeta(meta);
        return i;
    }
    public ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = gangtop.getString("VALUE_MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = gangtop.getInt("VALUE_MISC_ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = gangtop.getString("VALUE_MISC_ITEMS." + item + ".NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : gangtop.getStringList("VALUE_MISC_ITEMS." + item + ".LORE")) {
            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        if (hasGlow(item)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public boolean hasGlow(String item) {
        return gangtop.getBoolean("VALUE_MISC_ITEMS." + item + ".GLOW");
    }
}
