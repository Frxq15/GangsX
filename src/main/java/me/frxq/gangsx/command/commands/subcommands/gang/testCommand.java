package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
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
        Player p = (Player) sender;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        Bukkit.broadcastMessage(plugin.getGangUtils().canLevelup(gang)+"");
    }
}
