package net.withery.gangsx.Objects;

import net.withery.gangsx.Enums.Role;
import net.withery.gangsx.GangsX;

import java.util.UUID;

public class GPlayer {
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
    }


}
