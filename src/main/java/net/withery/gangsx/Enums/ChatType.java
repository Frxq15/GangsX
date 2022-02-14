package net.withery.gangsx.Enums;

public enum ChatType {
    PUBLIC(0),
    GANG(1),
    MOD(2),
    ALLY(3);

    public final int value;

    ChatType(final int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
