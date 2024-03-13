package me.frxq.gangsx.enums;

public enum Upgrade {

    MEMBER_LIMIT("member_limit"),
    BANK_LIMIT("bank_limit"),
    COIN_MULTIPLIER("coin_multiplier"),
    SHOP_DISCOUNT("shop_discount"),
    COLOURED_DESCRIPTION("coloured_description"),
    MAX_ALLIES("max_allies");

    private final String name;

    Upgrade(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Upgrade findByValue(String value) {
        Upgrade result = null;
        for (Upgrade upgrade : values()) {
            if (upgrade.getName().equalsIgnoreCase(value)) {
                result = upgrade;
                break;
            }
        }
        return result;
    }

}
