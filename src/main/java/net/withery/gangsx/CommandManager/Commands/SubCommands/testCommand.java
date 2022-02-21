package net.withery.gangsx.CommandManager.Commands.SubCommands;

import net.withery.gangsx.CommandManager.SubCommand;
import net.withery.gangsx.GUIManagement.GUIs.Shop;
import net.withery.gangsx.GangsX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class testCommand extends SubCommand {
    private final GangsX plugin;
    public testCommand(GangsX plugin) {
        super("test", "gangsx.command.test", "test", null);
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Shop shop = new Shop(plugin);
        shop.open((Player) sender);
    }
}
