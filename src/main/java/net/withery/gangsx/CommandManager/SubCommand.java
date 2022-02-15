package net.withery.gangsx.CommandManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SubCommand {
    private final String command;
    private final String permission;
    private final String usage;
    private final String[] aliases;

    public SubCommand(String command, String permission, String usage, String... aliases) {
        this.command = command;
        this.permission = permission;
        this.usage = usage;
        this.aliases = aliases;
    }
    @NotNull
    public String getCommand() {
        return command;
    }

    @NotNull
    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return usage;
    }

    public String[] getAliases() {
        return aliases;
    }

    @NotNull
    public abstract void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}
