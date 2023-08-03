package net.withery.gangsx.utils;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.Gang;

public class GangUtils {
    private final GangsX plugin;
    public GangUtils(GangsX plugin) {
        this.plugin = plugin;
    }
    public void prepareDisband(Gang gang) {
        gang.getMembers().forEach(member -> {
            member.kickFromGang();
        });
        plugin.getGangDataFactory().unloadGangDataAsync(gang.getID());
        plugin.getGangDataFactory().deleteGangDataAsync(gang.getID());
    }
}
