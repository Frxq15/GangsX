package net.withery.gangsx.settings.locale;

public enum LocaleReference {

    PREFIX("prefix", "&#bdc3c7[&#eb4034GangsX&#bdc3c7] "),

    COMMAND_WRONG_USAGE("command.wrong-usage", "&#eb4034Wrong Usage!");

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