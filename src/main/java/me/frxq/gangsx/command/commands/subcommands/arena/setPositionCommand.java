package me.frxq.gangsx.command.commands.subcommands.arena;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.utils.ArenaUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class setPositionCommand extends SubCommand {
    private final GangsX plugin;

    public setPositionCommand(GangsX plugin) {
        super("setposition", "gangsx.command.setposition", "/gadmin setposition", Arrays.asList("setloc"));
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
                        .replace("%position%", "challenger"));
                return;
            }

            switch (type.toLowerCase()) {
                case "challenger":
                    arenaUtils.setChallengerPosition(arena, p.getLocation());
                    plugin.getFileManager().saveArenaFile();
                    plugin.getFileManager().reloadArenaFile();
                    p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_SET")
                            .replace("%position%", "challenger").replace("%arena%", arena));
                    return;
                    case "opponent":
                        arenaUtils.setOpponentPosition(arena, p.getLocation());
                        plugin.getFileManager().saveArenaFile();
                        plugin.getFileManager().reloadArenaFile();
                        p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_SET")
                                .replace("%position%", "opponent").replace("%arena%", arena));
                        return;
                case "center":
                    arenaUtils.setArenaCenter(arena, p.getLocation());
                    plugin.getFileManager().saveArenaFile();
                    plugin.getFileManager().reloadArenaFile();
                    p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_SET")
                            .replace("%position%", "center").replace("%arena%", arena));
                    return;
                default:
                    plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gadmin setposition <arena> <challenger|opponent|center>");
                    return;

                }
            }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gadmin setposition <arena> <challenger|opponent|center>");
    }
}
