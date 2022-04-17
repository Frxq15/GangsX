package net.withery.gangsx.enums;

public enum Role {

    Recruit,
    Member,
    Mod,
    Co_Leader,
    Leader;


    public int getPriority() {
        return this.ordinal();
    }
}