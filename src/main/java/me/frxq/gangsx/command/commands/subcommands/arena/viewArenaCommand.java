package me.frxq.gangsx.command.commands.subcommands.arena;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.utils.ArenaUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class viewArenaCommand extends SubCommand {
    private final GangsX plugin;

    public viewArenaCommand(GangsX plugin) {
        super("viewarena", "gangsx.command.viewarena", "/gadmin viewarena", Arrays.asList("tp, viewposition"));
        this.plugin = plugin;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        if (args.length == 2) {
            ArenaUtils arenaUtils = plugin.getArenaUtils();
            String arena = args[0];
            String type = args[1];

            if(!arenaUtils.doesArenaExist(arena)) {
                p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_NOT_FOUND")
                        .replace("%arena%", arena));
                return;
            }

            switch (type.toLowerCase()) {
                case "challenger":
                    if(!arenaUtils.isChallengerPositionSet(arena)) {
                        p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_POSITION_NOT_SET")
                                .replace("%arena%", arena));
                        return;
                    }
                    p.teleport(arenaUtils.getChallengerPosition(arena));
                    return;
                case "opponent":
                    if(!arenaUtils.isOpponentPositionSet(arena)) {
                        p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_POSITION_NOT_SET")
                                .replace("%arena%", arena));
                        return;
                    }
                    p.teleport(arenaUtils.getOpponentPosition(arena));
                    return;
                case "center":
                    if(!arenaUtils.isCenterPositionSet(arena)) {
                        p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_POSITION_NOT_SET")
                                .replace("%arena%", arena));
                        return;
                    }
                    p.teleport(arenaUtils.getArenaCenter(arena));
                    return;
                default:
                    plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gadmin viewarena <arena> <challenger|opponent|center>");
                    return;

            }
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gadmin viewarena <arena> <challenger|opponent|center>");
    }
}
