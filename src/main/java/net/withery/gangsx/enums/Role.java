package net.withery.gangsx.enums;

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

    public int getPriority() {
        return this.ordinal();
    }
}