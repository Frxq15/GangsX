package net.withery.gangsx.Enums;

public enum Role {
    RECRUIT(0),
    MEMBER(1),
    MOD(2),
    CO_LEADER(3),
    LEADER(4);

    public final int value;

    Role(final int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
