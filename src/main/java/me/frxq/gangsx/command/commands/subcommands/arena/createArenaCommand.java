package me.frxq.gangsx.command.commands.subcommands.arena;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.utils.ArenaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class createArenaCommand extends SubCommand {
    private final GangsX plugin;

    public createArenaCommand(GangsX plugin) {
        super("createarena", "gangsx.command.createarena", "/gadmin createarena <arena>", Arrays.asList("createfightarena"));
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
            String arenaName = args[0];

            if(arenaUtils.doesArenaExist(arenaName)) {
                p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_ALREADY_EXISTS"));
                return;
            }
            arenaUtils.createArena(arenaName, p.getLocation());
            p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_ARENA_CREATED").replace("%arena%", arenaName));
            return;

        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /garena createarena <arena>");
    }
}
