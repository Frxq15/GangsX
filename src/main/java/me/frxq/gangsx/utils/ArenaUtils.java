package me.frxq.gangsx.utils;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.fight.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ArenaUtils {
    private GangsX plugin = GangsX.getInstance();
    private FileConfiguration arena = plugin.getFileManager().getArenaFile();

    public void updateArenasFile() {
        plugin.getFileManager().saveArenaFile();
        plugin.getFileManager().reloadArenaFile();
        arena = plugin.getFileManager().getArenaFile();
    }

    public void setChallengerPosition(String name, Location location) {
        arena.set("ARENAS."+name+".CHALLENGER_POSITION", location.serialize());
    }
    public void setOpponentPosition(String name, Location location) {
        arena.set("ARENAS."+name+".OPPONENT_POSITION", location.serialize());
    }
    public void setArenaCenter(String name, Location location) {
        arena.set("ARENAS."+name+".ARENA_CENTER", location.serialize());
    }
    public Location getArenaCenter(String name) {
        Location loc = new Location(Bukkit.getWorld(arena.getString("ARENAS."+name+".ARENA_CENTER.world")),
                arena.getDouble("ARENAS."+name+".ARENA_CENTER.x"),
                arena.getDouble("ARENAS."+name+".ARENA_CENTER.y"),
                arena.getDouble("ARENAS."+name+".ARENA_CENTER.z"),
                (float)arena.getDouble("ARENAS."+name+".ARENA_CENTER.yaw"),
                (float)arena.getDouble("ARENAS."+name+".ARENA_CENTER.pitch"));
        return loc;
    }
    public Location getChallengerPosition(String name) {
        Location loc = new Location(Bukkit.getWorld(arena.getString("ARENAS."+name+".CHALLENGER_POSITION.world")),
                arena.getDouble("ARENAS."+name+".CHALLENGER_POSITION.x"),
                arena.getDouble("ARENAS."+name+".CHALLENGER_POSITION.y"),
                arena.getDouble("ARENAS."+name+".CHALLENGER_POSITION.z"),
                (float)arena.getDouble("ARENAS."+name+".CHALLENGER_POSITION.yaw"),
                (float)arena.getDouble("ARENAS."+name+".CHALLENGER_POSITION.pitch"));
        return loc;
    }
    public Location getOpponentPosition(String name) {
        Location loc = new Location(Bukkit.getWorld(arena.getString("ARENAS."+name+".OPPONENT_POSITION.world")),
                arena.getDouble("ARENAS."+name+".OPPONENT_POSITION.x"),
                arena.getDouble("ARENAS."+name+".OPPONENT_POSITION.y"),
                arena.getDouble("ARENAS."+name+".OPPONENT_POSITION.z"),
                (float)arena.getDouble("ARENAS."+name+".OPPONENT_POSITION.yaw"),
                (float)arena.getDouble("ARENAS."+name+".OPPONENT_POSITION.pitch"));
        return loc;
    }
    public boolean doesArenaExist(String name) {
        if(arena.getString("ARENAS."+name) == null) {
            return false;
        }
        return true;
    }
    public boolean isChallengerPositionSet(String name) {
        if(!arena.isDouble("ARENAS."+name+".CHALLENGER_POSITION.x")) {
            return false;
        }
        return true;
    }
    public boolean isOpponentPositionSet(String name) {
        if(!arena.isDouble("ARENAS."+name+".OPPONENT_POSITION.x")) {
            return false;
        }
        return true;
    }
    public boolean isCenterPositionSet(String name) {
        if(!arena.isDouble("ARENAS."+name+".ARENA_CENTER.x")) {
            return false;
        }
        return true;
    }
    public void createArena(String name, Location location) {
        List<String> lore = new ArrayList<>();
        lore.add("&fThis is the default lore for this arena");
        lore.add("&fYou can change this in arena.yml");
        lore.add("");
        lore.add("&e| &fIn Use: %inuse%");
        arena.set("ARENAS."+name+".ITEM.DISPLAY_NAME", "&b"+name);
        arena.set("ARENAS."+name+".ITEM.MATERIAL", "MAP");
        arena.set("ARENAS."+name+".ITEM.LORE", lore);
        setArenaCenter(name, location);

        updateArenasFile();
    }
    public ItemStack getArenaItem(String name) {
        //normal item creation
        List<String> lore = new ArrayList<String>();


        String material = arena.getString("ARENAS." + name + ".ITEM.MATERIAL");
        final ItemStack i = new ItemStack(Material.valueOf(material), 1);
        String display = arena.getString("ARENAS." + name + ".ITEM.NAME");

        final ItemMeta meta = i.getItemMeta();
        for (String lines : arena.getStringList("ARENAS." + name + ".ITEM.LORE")) {
            lines = lines.replace("%inuse%", replaceBoolean(plugin.getArenaManager().isInUse(name)));
            lore.add(plugin.getColorFormatter().format(lines));
        }
        meta.setDisplayName(plugin.getColorFormatter().format(display));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public String replaceBoolean(Boolean value) {
        if(value) {
            return "&cNo";
        }
        return "&aYes";
    }
}
