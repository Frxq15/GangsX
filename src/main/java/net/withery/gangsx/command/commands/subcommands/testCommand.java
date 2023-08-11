package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.gui.menus.GangInvsee;
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
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        Bukkit.broadcastMessage(gang.getPermissions().toString());
    }
}
