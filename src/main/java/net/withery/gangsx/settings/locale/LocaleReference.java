package net.withery.gangsx.settings.locale;

public enum LocaleReference {

    PREFIX("prefix", "&#bdc3c7[&#eb4034GangsX&#bdc3c7] "),

    COMMAND_NO_PERMISSION("command.no-permission", "&7You need the permission &#eb4034&n%permission%&7 to use this command.", "permission"),
    COMMAND_WRONG_USAGE("command.wrong-usage", "&#eb4034Invalid usage, use %usage%", "usage"),
    COMMAND_SUB_COMMAND_NOT_FOUND("command.sub-command-not-found", "&7The sub command provided could not be found.");

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