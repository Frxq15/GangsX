package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.gui.menus.management.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class permissionsCommand extends SubCommand {
    private final GangsX plugin;

    public permissionsCommand(GangsX plugin) {
        super("permissions", "gangsx.command.permissions", "/gang permissions", Arrays.asList("perms"));
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
            new Permissions(plugin, p, gang).open(p);
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang permissions");
    }
}
