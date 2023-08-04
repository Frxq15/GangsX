package net.withery.gangsx.enums;

public enum Upgrades {

    MEMBER_LIMIT("member_limit"),
    BANK_LIMIT("bank_limit"),
    ADDED_DAMAGE("added_damage"),
    SPEED_ON_KILL("speed_on_kill"),
    COIN_MULTIPLIER("coin_multiplier"),
    XP_BOOST("xp_boost"),
    COLOURED_DESCRIPTION("colour_description"),
    MAX_ALLIES("maximum_allies");

    private final String name;

    Upgrades(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
