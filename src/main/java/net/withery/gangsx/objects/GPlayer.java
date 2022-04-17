package net.withery.gangsx.objects;

import net.withery.gangsx.enums.Role;
import net.withery.gangsx.GangsX;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GPlayer {
    private final static Map<UUID, GPlayer> players = new HashMap<>();
    private final GangsX plugin;
    private final UUID uuid;

    private Gang gang;
    private Role role;
    private boolean hasGang;
    private int kills;
    private int deaths;

    public GPlayer(GangsX plugin, final UUID uuid, final Gang gang, final Role role, final boolean hasGang, final int kills, final int deaths) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.gang = gang;
        this.role = role;
        this.hasGang = hasGang;
        this.kills = kills;
        this.deaths = deaths;
        players.put(uuid, this);
    }
    public GPlayer(GangsX plugin, final UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        //set rest from sql
        this.gang = gang;
        this.role = role;
        this.hasGang = hasGang;
        this.kills = kills;
        this.deaths = deaths;
        players.put(uuid, this);
    }

    public static GPlayer getPlayerData(UUID uuid) {
        if (!players.containsKey(uuid)) {
            //new gplayer from sql
        }
        return players.get(uuid);
    }

    public static Map<UUID, GPlayer> getAllPlayerData() {
        return players;
    }


    public UUID getUUID() { return uuid; }
    public Gang getGang() {
        return gang;
    }
    public Role getRole() { return role; }
    public Integer getKills() { return kills; }
    public Integer getDeaths() { return deaths; }
    public void setRole(Role role) {
        this.role = role;
    }
}
