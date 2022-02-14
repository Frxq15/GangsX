package net.withery.gangsx.CommandManager;

import net.withery.gangsx.CommandManager.Commands.gangCommand;
import net.withery.gangsx.GangsX;
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
        registerCommand("gang", new gangCommand(plugin, "gang", "gangsx.command.gang"));
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

        plugin.getLogger().log(Level.INFO, "Registered command " + command.getName());
    }
}