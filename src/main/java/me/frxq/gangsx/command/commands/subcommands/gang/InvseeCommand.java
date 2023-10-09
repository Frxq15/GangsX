package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.gui.menus.GangInvsee;
import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class InvseeCommand extends SubCommand {
    private final GangsX plugin;

    public InvseeCommand(GangsX plugin) {
        super("invsee", "gangsx.command.invsee", "/gang invsee", Arrays.asList("seeinv"));
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!plugin.getConfig().getBoolean("gang.command.enable_invsee")) {
            plugin.getLocaleManager().sendMessage(sender, "FEATURE_DISABLED");
            return;
        }
        if(!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        Player p = (Player) sender;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        if(!plugin.getGangUtils().playerHasGangPermission(gPlayer,gang, Permission.INVSEE)) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_NO_PERMISSION");
            return;
        }
        if(args.length == 1) {
            if(Bukkit.getPlayer(args[0]) == null) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_FOUND");
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            GPlayer tPlayer = plugin.getGPlayerDataFactory().getGPlayerData(target.getUniqueId());

            if(tPlayer.getGangId() == null || !tPlayer.getGangId().equals(gPlayer.getGangId())) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_YOUR_GANG");
                return;
            }
            new GangInvsee(plugin, p, target).open(p);
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang invsee <player>");
    }
}
