package net.withery.gangsx.objects;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.enums.Role;

import java.util.UUID;

public class GPlayer {

    private final GangsX plugin;
    private final UUID uuid;
    private String name;

    private UUID gangId;
    private Role role;
    private int kills, deaths;
    private boolean hasGang;

    public GPlayer(GangsX plugin, final UUID uuid, final String name, final UUID gangId, final Role role, final int kills, final int deaths) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.gangId = gangId;
        this.role = role;
        this.kills = kills;
        this.deaths = deaths;

        if(getGangId().equals("N/A")) {

            return;
        }
    }

    public GPlayer(GangsX plugin, final UUID uuid, final String name) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.gangId = null;
        this.role = null;
        this.kills = 0;
        this.deaths = 0;
    }

    public UUID getID() {
        return uuid;
    }

    public boolean hasGang() { return hasGang; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getGangId() {
        if (gangId == null) {
            return UUID.fromString("N/A");
        }
        return gangId;
    }

    public void setHasGang(boolean hasGang) { this.hasGang = hasGang; }

    public void setGangId(UUID gangId) {
        this.gangId = gangId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getKills() {
        return kills;
    }

    public Integer getDeaths() {
        return deaths;
    }

}