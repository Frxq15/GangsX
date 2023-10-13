package me.frxq.gangsx.command.commands.subcommands.admin;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.utils.ArenaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class setArenaCommand extends SubCommand {
    private final GangsX plugin;

    public setArenaCommand(GangsX plugin) {
        super("setarena", "gangsx.command.setarena", "/gadmin setarena", Arrays.asList("setfightarena"));
        this.plugin = plugin;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        if (args.length == 1) {
            ArenaUtils arenaUtils = plugin.getArenaUtils();
            String type = args[0];
            switch (type.toLowerCase()) {
                case "challenger":
                    arenaUtils.setChallengerPosition(p.getLocation());
                    p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_SET")
                            .replace("%position%", "challenger"));
                    return;
                    case "opponent":
                        arenaUtils.setOpponentPosition(p.getLocation());
                        p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_SET")
                                .replace("%position%", "opponent"));
                        return;
                case "center":
                    arenaUtils.setArenaCenter(p.getLocation());
                    p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_SET")
                            .replace("%position%", "center"));
                    return;
                default:
                    plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gadmin setarena <challenger|opponent|center>");
                    return;

                }
            }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gadmin setarena <challenger|opponent|center>");
    }
}
