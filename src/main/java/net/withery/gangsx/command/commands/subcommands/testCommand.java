package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.gui.GUIs.Shop;
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
        Shop shop = new Shop(plugin, (Player) sender);
        shop.open((Player) sender);
    }
}
