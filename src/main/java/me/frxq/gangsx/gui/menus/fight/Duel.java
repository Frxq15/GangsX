package me.frxq.gangsx.gui.menus.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.gui.GUITemplate;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Duel extends GUITemplate {
    private Player player;
    private final Gang challenger;
    private final Gang opponent;

    private final String kit;
    private final String arena;
    private GangsX plugin;
    private FileConfiguration duel;

    private List<Integer> CHALLENGER_SLOTS = new ArrayList<>();
    private List<Integer> OPPONENT_SLOTS = new ArrayList<>();

    private List<Integer> ANIMATED_SLOTS = new ArrayList<>();

    public Duel(GangsX plugin, Player player, Gang gang, Gang opponent, String kit, String arena) {
        super(plugin, plugin.getFileManager().getDuelFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getDuelFile().getString("TITLE").replace("%gang%", opponent.getName())));
        this.plugin = plugin;
        this.player = player;
        this.challenger = gang;
        this.opponent = opponent;
        this.duel = plugin.getFileManager().getDuelFile();
        this.kit = kit;
        this.arena = arena;
        initializeSlots();
        initialize();
    }
    public void initializeSlots() {
        duel.getStringList("ITEMS.CHALLENGER_ROSTER.SLOTS").forEach(c -> {
            CHALLENGER_SLOTS.add(Integer.parseInt(c));
        });
        duel.getStringList("ITEMS.OPPONENT_ROSTER.SLOTS").forEach(o -> {
            OPPONENT_SLOTS.add(Integer.parseInt(o));
        });
        duel.getStringList("ANIMATED_ITEMS.ITEM.SLOTS").forEach(a -> {
            ANIMATED_SLOTS.add(Integer.parseInt(a));
        });
    }

    public void initialize() {
        ANIMATED_SLOTS.forEach(slot -> {
            setItem(slot, createAnimationItem());
        });
        setItem(getItemSlot("CHALLENGER", false), createGangItem("CHALLENGER", challenger));
        setItem(getItemSlot("OPPONENT", false), createGangItem("OPPONENT", opponent));
        setItem(getItemSlot("KIT", false), createItem("KIT"));
        setItem(getItemSlot("ARENA", false), createItem("ARENA"));
        setItem(getItemSlot("INFO", false), createInfoItem("INFO"));

        duel.getConfigurationSection("MISC_ITEMS").getKeys(false).forEach(item -> {
            setItem(getItemSlot(item, true), createMiscItem(item));
        });
        duel.getStringList("ITEMS.ACCEPT.SLOTS").forEach(accept -> {
            setItem(Integer.parseInt(accept), createItem("ACCEPT"), p -> {
                runAnimation(p);
            });
        });
        duel.getStringList("ITEMS.CANCEL.SLOTS").forEach(cancel -> {
            setItem(Integer.parseInt(cancel), createItem("CANCEL"), p -> {
                p.getOpenInventory().close();
            });
        });
        AtomicInteger challenger_done = new AtomicInteger(0);
        AtomicInteger opponent_done = new AtomicInteger(0);
        challenger.getFightRoster().forEach(c -> {
            setItem(CHALLENGER_SLOTS.get(challenger_done.get()), createRosterItem("CHALLENGER_ROSTER", c, challenger));
            challenger_done.getAndIncrement();
        });
        opponent.getFightRoster().forEach(o -> {
            setItem(OPPONENT_SLOTS.get(opponent_done.get()), createRosterItem("OPPONENT_ROSTER", o, opponent));
            opponent_done.getAndIncrement();
        });
    }
    public boolean hasGlow(String item, boolean miscitem) {
        if (miscitem) {
            return duel.getBoolean("MISC_ITEMS." + item + ".GLOW");
        }
        return duel.getBoolean("ITEMS." + item + ".GLOW");
    }
    public Integer getItemSlot(String item, boolean miscitem) {
        if (miscitem) {
            return duel.getInt("MISC_ITEMS." + item + ".SLOT");
        }
        return duel.getInt("ITEMS." + item + ".SLOT");
    }
    public String colourKDR(Gang gang) { //change to win%
        Integer kdr = gang.getKDR();
        if(duel.getBoolean("colour-percentages")) {
            if(kdr <= 20) {
                return "&c"+gang.getKDRString();
            }
            if(kdr <= 60) {
                return "&e"+gang.getKDRString();
            }
            return "&a"+gang.getKDRString();
        }
        return gang.getKDRString();
    }
    public ItemStack createGangItem(String type, Gang gang) {
        //gang item creation
        List<String> lore = new ArrayList<String>();

        ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        meta.setOwner(Bukkit.getOfflinePlayer(gang.getLeader()).getName());
        String name = duel.getString("ITEMS."+type+".NAME")
                .replace("%gang%", gang.getName());


        for (String lines : duel.getStringList("ITEMS."+type+".LORE")) {
            lines = lines.replace("%gang%", gang.getName())
                    .replace("%kills%", gang.getKills()+"")
                    .replace("%deaths%", gang.getDeaths()+"")
                    .replace("%kdr%", gang.getKDRString());

            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public ItemStack createRosterItem(String type, GPlayer gPlayer, Gang gang) {
        //roster item creation
        List<String> lore = new ArrayList<String>();

        ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        meta.setOwner(Bukkit.getOfflinePlayer(gPlayer.getID()).getName());
        String name = duel.getString("ITEMS."+type+".NAME")
                .replace("%gang%", gang.getName()).replace("%player%", gPlayer.getName());


        for (String lines : duel.getStringList("ITEMS."+type+".LORE")) {
            lines = lines.replace("%gang%", gang.getName())
                    .replace("%kills%", gPlayer.getKills()+"")
                    .replace("%deaths%", gPlayer.getDeaths()+"")
                    .replace("%kdr%", gPlayer.getKDRString()).replace("%player%", gPlayer.getName());

            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    ItemStack createItem(String item) {
        // item creation
        List<String> lore = new ArrayList<String>();

        String material = duel.getString("ITEMS." + item + ".MATERIAL");
        final ItemStack i = new ItemStack(Material.valueOf(material), 1);
        String name = duel.getString("ITEMS." + item + ".NAME").replace("%kit%", kit).replace("%arena%", arena);

        final ItemMeta meta = i.getItemMeta();
        Byte data = (Byte) duel.get("MISC_ITEMS." + item + ".DATA");
        MaterialData materialData = new MaterialData(Integer.parseInt(material), data);
        i.setData(materialData);
        for (String lines : duel.getStringList("ITEMS." + item + ".LORE")) {
            lines= lines.replace("%kit%", kit).replace("%arena%", arena);
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
    ItemStack createInfoItem(String item) {
        // info item creation
        List<String> lore = new ArrayList<String>();

        String material = duel.getString("ITEMS." + item + ".MATERIAL");
        final ItemStack i = new ItemStack(Material.valueOf(material), 1);
        String name = duel.getString("ITEMS." + item + ".NAME").replace("%challenger%", challenger.getName())
                .replace("%opponent%", opponent.getName());
        Byte data = (Byte) duel.get("ITEMS." + item + ".DATA");
        MaterialData materialData = new MaterialData(Integer.parseInt(material), data);
        i.setData(materialData);
        final ItemMeta meta = i.getItemMeta();

        for (String lines : duel.getStringList("ITEMS." + item + ".LORE")) {
            lines = lines.replace("%challenger%", challenger.getName())
                    .replace("%opponent%", opponent.getName())
                    .replace("%type%", challenger.getFightRoster().size()+"v"+challenger.getFightRoster().size())
                    .replace("%kit%", kit)
                    .replace("%arena%", arena);;
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
    public ItemStack createMiscItem(String item) {
        // misc item creation
        List<String> lore = new ArrayList<String>();

        String material = duel.getString("MISC_ITEMS." + item + ".MATERIAL");
        Integer amount = duel.getInt("MISC_ITEMS." + item + ".AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = duel.getString("MISC_ITEMS." + item + ".NAME");
        Byte data = (Byte) duel.get("MISC_ITEMS." + item + ".DATA");
        MaterialData materialData = new MaterialData(Integer.parseInt(material), data);
        i.setData(materialData);

        final ItemMeta meta = i.getItemMeta();
        for (String lines : duel.getStringList("MISC_ITEMS." + item + ".LORE")) {
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
    public ItemStack createAnimationItem() {
        // animation item creation
        List<String> lore = new ArrayList<String>();

        String material = duel.getString("ANIMATED_ITEMS.ITEM.MATERIAL");
        Integer amount = duel.getInt("ANIMATED_ITEMS.ITEM.AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        String name = duel.getString("ANIMATED_ITEMS.ITEM.NAME");
        Byte data = (Byte) duel.get("ANIMATED_ITEMS.ITEM.DATA");
        MaterialData materialData = new MaterialData(Integer.parseInt(material), data);
        i.setData(materialData);

        final ItemMeta meta = i.getItemMeta();
        for (String lines : duel.getStringList("ANIMATED_ITEMS.ITEM.LORE")) {
            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public ItemStack createAnimatedItem() {
        // animated item creation
        List<String> lore = new ArrayList<String>();

        String material = duel.getString("ANIMATED_ITEMS.ANIMATED_ITEM.MATERIAL");
        Integer amount = duel.getInt("ANIMATED_ITEMS.ANIMATED_ITEM.AMOUNT");
        final ItemStack i = new ItemStack(Material.valueOf(material), amount);
        Byte data = (Byte) duel.get("ANIMATED_ITEMS.ANIMATED_ITEM.DATA");
        MaterialData materialData = new MaterialData(Integer.parseInt(material), data);
        i.setData(materialData);
        String name = duel.getString("ANIMATED_ITEMS.ANIMATED_ITEM.NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : duel.getStringList("ANIMATED_ITEMS.ANIMATED_ITEM.LORE")) {
            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(name));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public void runAnimation(Player player) {
        AtomicInteger animation_done = new AtomicInteger(0);
        final int[] count = {duel.getStringList("ANIMATED_ITEMS.ITEM.SLOTS").size()};
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                switch(count[0]) {
                    case 0:
                        player.getOpenInventory().close();
                        executeSend(player);
                        cancel();
                        return;

                    default:
                        setItem(ANIMATED_SLOTS.get(animation_done.get()), createAnimatedItem());
                        animation_done.getAndIncrement();
                        playAnimationSound(player);
                        count[0]--;
                }
            }
        }.runTaskTimer(plugin, 5L, 10L);
    }
    public void playAnimationSound(Player player) {
        if(duel.getBoolean("enable-animation-sound")) {
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        }
    }
    public void executeSend(Player player) {
        challenger.getFightRoster().forEach(roster -> {
            Player u = Bukkit.getPlayer(roster.getID());
            for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_FIGHT_REQUEST_SENT")) {
                lines= lines.replace("%challenger%", challenger.getName())
                        .replace("%opponent%", opponent.getName())
                        .replace("%type%", challenger.getFightRoster().size()+"v"+challenger.getFightRoster().size())
                        .replace("%kit%", kit)
                        .replace("%arena%", arena).replace("%player%", player.getName()).replace("%gang%", challenger.getName());
                plugin.getLocaleManager().sendRawMessage(u, lines);
            }
        });
    }
}
