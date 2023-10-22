package me.frxq.gangsx.command;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class CommandHandler {
    private final GangsX plugin;
    public CommandHandler(GangsX plugin) {
        this.plugin = plugin;
    }

    public void load() {
        registerCommands();
    }

    private void registerCommands() {
        registerCommand("gang", new GangCommand(plugin));
        registerCommand("fight", new FightCommand(plugin));
        registerCommand("gadmin", new GangAdminCommand(plugin));
        registerCommand("garena", new GangArenaCommand(plugin));
        plugin.getCommand("gangchat").setExecutor(new GangChatCommand());
        plugin.getCommand("allychat").setExecutor(new AllyChatCommand());
    }

    private void registerCommand(@NotNull String name, @NotNull CommandExecutor commandExecutor) {
        PluginCommand command = plugin.getCommand(name);

        // Checking whether the command is registered in the plugin.yml
        if (command == null) {
            plugin.getLogger().log(Level.WARNING, "Command \"" + name + "\" is not registered in the plugin.yml! Skipping command...");
            return;
        }

        command.setExecutor(commandExecutor);

        // Using command executor as tab completer if it is one
        if (commandExecutor instanceof TabCompleter)
            command.setTabCompleter((TabCompleter) commandExecutor);
    }
}
