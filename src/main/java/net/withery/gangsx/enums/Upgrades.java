package net.withery.gangsx.enums;

public enum Upgrades {

    MEMBER_LIMIT("member_limit"),
    BANK_LIMIT("bank_limit"),
    HOME_LIMIT("home_limit"),
    MAX_ALLIES("maximum_allies");

    private final String name;

    Upgrades(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
