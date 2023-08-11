package net.withery.gangsx.enums;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;

public enum Role {

    RECRUIT("Recruit"),
    MEMBER("Member"),
    MOD("Mod"),
    CO_LEADER("Co-Leader"),
    LEADER("Leader");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getRoleIcon() {
        return GangsX.getInstance().getConfig().getString("gang.roles.icons."+getName().toLowerCase());
    }
    public String getRolePrefix() {
        return GangsX.getInstance().getConfig().getString("gang.roles.prefixes."+getName().toLowerCase());
    }
    public boolean hasMinimumPermission(GPlayer gPlayer, Permission permission, Role role) {
        Gang gang = GangsX.getInstance().getGangDataFactory().getGangData(gPlayer.getGangId());
        Role minimum = gang.getPermissions().get(permission);

        if(minimum.getPriority() >= role.getPriority()) {
            return true;
        }
        return false;
    }

    public int getPriority() {
        return this.ordinal();
    }
}