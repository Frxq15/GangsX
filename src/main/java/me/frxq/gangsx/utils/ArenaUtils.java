package me.frxq.gangsx.utils;

import me.frxq.gangsx.GangsX;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenaUtils {
    private GangsX plugin = GangsX.getInstance();
    private FileConfiguration arena = plugin.getFileManager().getArenaFile();

    public void setChallengerPosition(Location location) {
        arena.set("ARENA.CHALLENGER_POSITION", location.serialize());
        plugin.getFileManager().saveArenaFile();
        plugin.getFileManager().reloadArenaFile();
    }
    public void setOpponentPosition(Location location) {
        arena.set("ARENA.OPPONENT_POSITION", location.serialize());
        plugin.getFileManager().saveArenaFile();
        plugin.getFileManager().reloadArenaFile();
    }
    public void setArenaCenter(Location location) {
        arena.set("ARENA.ARENA_CENTER", location.serialize());
        plugin.getFileManager().saveArenaFile();
        plugin.getFileManager().reloadArenaFile();
    }
    public Location getArenaCenter() {
        Location loc = new Location(Bukkit.getWorld(arena.getString("ARENA.ARENA_CENTER.world")),
                arena.getDouble("ARENA.ARENA_CENTER.x"),
                arena.getDouble("ARENA.ARENA_CENTER.y"),
                arena.getDouble("ARENA.ARENA_CENTER.z"),
                (float)arena.getDouble("ARENA.ARENA_CENTER.yaw"),
                (float)arena.getDouble("ARENA.ARENA_CENTER.pitch"));
        return loc;
    }
    public Location getChallengerPosition() {
        Location loc = new Location(Bukkit.getWorld(arena.getString("ARENA.CHALLENGER_POSITION.world")),
                arena.getDouble("ARENA.CHALLENGER_POSITION.x"),
                arena.getDouble("ARENA.CHALLENGER_POSITION.y"),
                arena.getDouble("ARENA.CHALLENGER_POSITION.z"),
                (float)arena.getDouble("ARENA.CHALLENGER_POSITION.yaw"),
                (float)arena.getDouble("ARENA.CHALLENGER_POSITION.pitch"));
        return loc;
    }
    public Location getOpponentPosition() {
        Location loc = new Location(Bukkit.getWorld(arena.getString("ARENA.OPPONENT_POSITION.world")),
                arena.getDouble("ARENA.OPPONENT_POSITION.x"),
                arena.getDouble("ARENA.OPPONENT_POSITION.y"),
                arena.getDouble("ARENA.OPPONENT_POSITION.z"),
                (float)arena.getDouble("ARENA.OPPONENT_POSITION.yaw"),
                (float)arena.getDouble("ARENA.OPPONENT_POSITION.pitch"));
        return loc;
    }
    public boolean isArenaCenterSet() {
        if(arena.getString("ARENA.ARENA_CENTER.world") == null) {
            return false;
        }
        return true;
    }
    public boolean isChallengerPositionSet() {
        if(arena.getString("ARENA.CHALLENGER_POSITION.world") == null) {
            return false;
        }
        return true;
    }
    public boolean isOpponentPositionSet() {
        if(arena.getString("ARENA.OPPONENT_POSITION.world") == null) {
            return false;
        }
        return true;
    }
}
