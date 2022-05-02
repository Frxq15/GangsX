package net.withery.gangsx.settings.locale;

import java.util.List;

public enum LocaleReference {

    PREFIX("prefix", "&#bdc3c7[&#eb4034GangsX&#bdc3c7] "),

    COMMAND_NO_PERMISSION("command.no-permission", "&7You need the permission &#eb4034&n%permission%&7 to use this command.", "permission"),
    COMMAND_GANG_CREATED("command.gang_created", "&7The gang &#eb4034%gang% &7was created by &#eb4034%player%.", "gang", "player"),
    COMMAND_PLAYER_GANG_CREATED("command.player_gang_created", "&7You created a gang successfully."),
    COMMAND_WRONG_USAGE("command.wrong_usage", "&#eb4034Invalid usage provided, use %usage%", "usage"),
    COMMAND_PLUGIN_VERSION("command.plugin-version", "&fRunning &#eb4034GangsX v%version%-SNAPSHOT", "version"),
    COMMAND_PLAYER_ALREADY_IN_GANG("command.player_already_in_gang", "&fYou are already in a gang."),
    COMMAND_PLAYER_NOT_IN_A_GANG("command.player_not_in_a_gang", "&fYou are not in a gang."),
    COMMAND_SUB_COMMAND_NOT_FOUND("command.sub-command_not_found", "&7The sub command provided could not be found."),
    COMMAND_GANG_INFO("command.gang_info", "&7{split}" +
            "&b&lGang Info &8- &b%gang%{split}" +
            "&7Created: &b%created%{split}" +
            "&7{split}" +
            "&b&l → &7Leader: &b%leader%{split}" +
            "&b&l • &7Level: &b%level%{split}" +
            "&7{split}" +
            "&bStats:{split}" +
            "&b&l • &7Kills: &b%kills%{split}" +
            "&b&l • &7Deaths: &b%deaths%{split}" +
            "&b&l • &7Blocks Broken: &b%blocks%{split}" +
            "{split}" +
            "&bInformation:{split}" +
            "&b&l • &7Members: &b%members%{split}" +
            "&b&l • &7Allies: &d%allies%{split}" +
            "{split}" +
            "&b&l • &7Coins: &b%coins%{split}" +
            "&b&l • &7Bank Account: &b$%bank%{split}" +
            "&7{split}",
            "gang", "created", "leader", "level", "kills", "deaths", "blocks", "members", "allies", "coins", "bank");

    private final String reference;
    private final String defaultValue;
    private final String[] variables;

    LocaleReference(String reference, String defaultValue, String... variables) {
        this.reference = reference;
        this.defaultValue = defaultValue;
        this.variables = variables;
    }

    public String getReference() {
        return reference;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String[] getVariables() {
        return variables;
    }

}