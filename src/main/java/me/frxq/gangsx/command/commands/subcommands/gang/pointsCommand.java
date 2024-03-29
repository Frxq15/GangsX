package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.formatting.number.NumberFormatter;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class pointsCommand extends SubCommand {
    private final GangsX plugin;

    public pointsCommand(GangsX plugin) {
        super("points", "gangsx.command.points", "/gang points <gang>", Arrays.asList("p"));
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        Player p = (Player) sender;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());

        if(args.length == 0) {
            if(!gPlayer.hasGang()) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            p.sendMessage(plugin.getLocaleManager().getMessage("GANG_POINTS")
                    .replace("%gang%", gang.getName())
                    .replace("%amount%", NumberFormatter.format(gang.getPoints()) + ""));
            return;
        }

        if (args.length == 1) {
            String target = args[0];
            if(!plugin.getGangDataFactory().doesGangNameExist(target)) {
                plugin.getLocaleManager().sendMessage(p, "GANG_NOT_FOUND");
                return;
            }
            UUID gangId = plugin.getGangDataFactory().getGangUniqueId(target);
            Gang gang = plugin.getGangDataFactory().getGangData(gangId);
            p.sendMessage(plugin.getLocaleManager().getMessage("GANG_VALUE")
                    .replace("%gang%", gang.getName())
                    .replace("%amount%", NumberFormatter.format(gang.getPoints()) + ""));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang points <gang>");
        return;
    }
}
