package me.frxq.gangsx.command.commands.subcommands.admin;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.utils.ArenaUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class viewArena extends SubCommand {
    private final GangsX plugin;

    public viewArena(GangsX plugin) {
        super("viewarena", "gangsx.command.viewarena", "/gadmin viewarena", Arrays.asList("arenatp"));
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
                    p.teleport(arenaUtils.getChallengerPosition());
                    return;
                case "opponent":
                    p.teleport(arenaUtils.getOpponentPosition());
                    return;
                case "center":
                    p.teleport(arenaUtils.getArenaCenter());
                    return;
                default:
                    plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gadmin viewarena <challenger|opponent|center>");
                    return;

            }
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gadmin viewarena <challenger|opponent|center>");
    }
}
