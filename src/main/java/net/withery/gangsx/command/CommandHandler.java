package net.withery.gangsx.command;

import net.withery.gangsx.command.commands.GangChatCommand;
import net.withery.gangsx.command.commands.GangCommand;
import net.withery.gangsx.GangsX;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public record CommandHandler(GangsX plugin) {

    public void load() {
        registerCommands();
    }

    private void registerCommands() {
        registerCommand("gang", new GangCommand(plugin));
        plugin.getCommand("gangchat").setExecutor(new GangChatCommand());
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
