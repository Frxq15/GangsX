package net.withery.gangsx.objects;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.enums.Role;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

public class GPlayer {

    private final GangsX plugin;
    private final UUID uuid;
    private String name;

    private UUID gangId;
    private Role role;
    private int kills, deaths;
    private String gangIdString;
    private boolean hasGang;

    private boolean chatEnabled;
    private boolean allyChatEnabled;

    private ArrayList<GPlayer> alertCooldown = new ArrayList<>();

    public GPlayer(GangsX plugin, final UUID uuid, final String name, final UUID gangId, final Role role, final int kills, final int deaths, final boolean chatEnabled) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.role = role;
        this.kills = kills;
        this.deaths = deaths;
        this.chatEnabled = chatEnabled;
        this.allyChatEnabled = false;

        if(gangId == null) {
            setGangIdString("N/A");
            setHasGang(false);
        } else {
            this.gangId = gangId;
            setHasGang(true);
        }
        this.alertCooldown = new ArrayList<>();
    }

    public GPlayer(GangsX plugin, final UUID uuid, final String name) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.role = null;
        this.kills = 0;
        this.deaths = 0;
        if(gangId == null) {
            setGangIdString("N/A");
            setHasGang(false);
        }
        this.alertCooldown = new ArrayList<>();
        this.chatEnabled = false;
        this.allyChatEnabled = false;
    }

    public UUID getID() {
        return uuid;
    }

    public boolean hasGang() { return hasGang; }

    public boolean isOnAlertCooldown() {
        return this.alertCooldown.contains(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getGangId() {
        return gangId;
    }

    public String getGangIDString() {
        return gangIdString;
    }

    public void setGangIdString(String id) {
        this.gangIdString = id;
    }

    public void setHasGang(boolean hasGang) { this.hasGang = hasGang; }

    public void applyAlertCooldown() {
        this.alertCooldown.add(this);
    }
    public void removeAlertCooldown() { this.alertCooldown.remove(this); }



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

    public void kickFromGang() {
        Gang gang = plugin.getGangDataFactory().getGangData(getGangId());
        gang.removeMember(this);
        setHasGang(false);
        setGangId(null);
        setGangIdString(null);
        setRole(null);

        if(Bukkit.getPlayer(getName()) != null) {
            gang.removeOnlineMember(this);
        }
    }
    public boolean hasChatEnabled() {
        return chatEnabled;
    }
    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;
    }

    public boolean hasAllyChatEnabled() { return allyChatEnabled; }

    public void setAllyChatEnabled(boolean chatEnabled) { allyChatEnabled = chatEnabled; }
}