package me.frxq.gangsx.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SubCommand {
    private final String command;
    private final String permission;
    private final String usage;
    private final List<String> aliases;

    public SubCommand(String command, String permission, String usage, List<String> aliases) {
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

    public List<String> getAliases() {
        return aliases;
    }

    @NotNull
    public abstract void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}
