package net.withery.gangsx.Enums;

public enum Upgrade {
    MEMBER_LIMIT(0),
    MAX_ALLIES(1);

    public final int value;

    Upgrade(final int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

}
