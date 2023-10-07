package net.withery.gangsx.enums;

import net.withery.gangsx.GangsX;

public enum Permission {

    BANK_DEPOSIT("BANK_DEPOSIT"),
    BANK_WITHDRAW("BANK_WITHDRAW"),
    CHANGE_DESCRIPTION("CHANGE_DESCRIPTION"),
    MANAGE_RELATIONS("MANAGE_RELATIONS"),
    PURCHASE_VALUE("PURCHASE_VALUE"),
    PURCHASE_UPGRADES("PURCHASE_UPGRADES"), //change to levelup
    PROMOTE("PROMOTE"),
    DEMOTE("DEMOTE"),
    MANAGE_FRIENDLY_FIRE("MANAGE_FRIENDLY_FIRE"),
    KICK("KICK"),
    RENAME_GANG("RENAME_GANG"),
    SHOP("SHOP"),
    INVSEE("INVSEE"),

    //new ones to add to everything

    ALERT("ALERT"),

    GANG_CHAT("GANG_CHAT"),

    INVITE("INVITE");

    private final String name;

    Permission(String name) { this.name = name; }

    public String getName() {
        return name;
    }
}
