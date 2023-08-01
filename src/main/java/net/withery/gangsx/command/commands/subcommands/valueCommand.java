package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.formatting.number.NumberFormatter;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class valueCommand extends SubCommand {
    private final GangsX plugin;

    public valueCommand(GangsX plugin) {
        super("value", "gangsx.command.value", "/gang value <gang>", Arrays.asList("worth"));
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
                    .replace("%amount%", NumberFormatter.format(gang.getValue()) + ""));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang coins");
        return;
    }
}
