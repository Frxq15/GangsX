package net.withery.gangsx.CommandManager;

import net.withery.gangsx.GangsX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ParentCommand implements CommandExecutor {
    protected final GangsX plugin;
    protected final String name;
    protected final String permission;

    private final Set<SubCommand> subCommands = new HashSet<>();

    protected String subLabel;
    protected String[] subArgs;

    public ParentCommand(GangsX plugin, String name, String permission) {
        this.plugin = plugin;
        this.name = name;
        this.permission = permission;
    }

    public void register(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public boolean exists(String label) {
        for (SubCommand subCommand : subCommands)
            if (subCommand.getCommand().equalsIgnoreCase(label))
                return true;

        for (SubCommand subCommand : subCommands)
            if(subCommand.getAliases() == null) {
                return false;
            }

            else
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(label))
                        return true;

        return false;
    }

    public SubCommand getExecutor(String label) {
        for (SubCommand subCommand : subCommands)
            if (subCommand.getCommand().equalsIgnoreCase(label))
                return subCommand;

            else
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(label))
                        return subCommand;

        return null;
    }

    public Set<SubCommand> getSubCommands() {
        return subCommands;
    }

}
