package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.gui.menus.Shop;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class testCommand extends SubCommand {
    private final GangsX plugin;
    public testCommand(GangsX plugin) {
        super("test", "gangsx.command.test", "test", null);
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;

        if(args.length == 0) {
            boolean t = plugin.getLeaderboardManager().getGangByPosition(0) == null;
            Bukkit.broadcastMessage(t+"");
        }
        if(args.length == 1) {
            Bukkit.broadcastMessage(plugin.getLeaderboardManager().top_values.toString());
        }
        if(args.length == 2) {
            plugin.getGangDataFactory().updateLeaderboardTopValues();
        }
    }
}
