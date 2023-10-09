package me.frxq.gangsx.command;

import me.frxq.gangsx.GangsX;
import org.bukkit.command.CommandExecutor;

import java.util.HashSet;
import java.util.Set;

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
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getCommand().equalsIgnoreCase(label))
                return true;
            else if (subCommand.getAliases() != null)
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(label))
                        return true;
        }

        return false;
    }

    public SubCommand getExecutor(String label) {
        for (SubCommand subCommand : subCommands){
            if (subCommand.getCommand().equalsIgnoreCase(label))
                return subCommand;
            else if (subCommand.getAliases() != null)
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(label))
                        return subCommand;
        }

        return null;
    }

    public Set<SubCommand> getSubCommands() {
        return subCommands;
    }

}
