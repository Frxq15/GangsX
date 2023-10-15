package me.frxq.gangsx.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.utils.ArenaUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {
    private final GangsX plugin;
    private final FileConfiguration arena;

    private final ArenaUtils arenaUtils;

    private List<String> inUse = new ArrayList<>();

    public ArenaManager(GangsX plugin) {
        this.plugin = plugin;
        this.arena = plugin.getFileManager().getArenaFile();
        this.arenaUtils = plugin.getArenaUtils();
    }
    public boolean isInUse(String arenaName) {
        return inUse.contains(arenaName);
    }
    public void setInUse(String arenaName, boolean value) {
        if(value) {
            inUse.add(arenaName);
        }
        inUse.remove(arenaName);
    }
    public Location getCenter(String arenaName) {
        return arenaUtils.getArenaCenter(arenaName);
    }
    public Location getOpponentPosition(String arenaName) {
        return arenaUtils.getOpponentPosition(arenaName);
    }
    public Location getChallengerPosition(String arenaName) {
        return arenaUtils.getChallengerPosition(arenaName);
    }
    public ItemStack getArenaItem(String arenaName) {
        return arenaUtils.getArenaItem(arenaName);
    }

}
