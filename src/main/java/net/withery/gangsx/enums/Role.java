package net.withery.gangsx.enums;

import net.withery.gangsx.GangsX;

public enum Role {

    RECRUIT("Recruit"),
    MEMBER("Member"),
    MOD("Mod"),
    CO_LEADER("Co Leader"),
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

    public int getPriority() {
        return this.ordinal();
    }
}