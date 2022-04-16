package net.withery.gangsx.enums;

public enum Role {

    RECRUIT,
    MEMBER,
    MOD,
    CO_LEADER,
    LEADER;


    public int getPriority() {
        return this.ordinal();
    }
}