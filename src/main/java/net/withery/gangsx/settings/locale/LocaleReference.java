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
    COMMAND_SUB_COMMAND_NOT_FOUND("command.sub-command_not_found", "&7The sub command provided could not be found.");

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