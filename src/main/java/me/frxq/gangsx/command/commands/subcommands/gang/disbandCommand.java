package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.gui.menus.Disband;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.enums.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class disbandCommand extends SubCommand {
    private final GangsX plugin;

    public disbandCommand(GangsX plugin) {
        super("disband", "gangsx.command.disband", "/gang disband", null);
        this.plugin = plugin;
    }
    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        Player p = (Player) sender;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        if(!gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
            return;
        }
        if(args.length == 0) {
            if(!gPlayer.getRole().equals(Role.LEADER)) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_ONLY_LEADER_CAN_DISBAND");
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            if(plugin.getConfig().getBoolean("gang.use-disband-gui")) {
                new Disband(plugin, p, gang).open(p);
                return;
            }
            gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_DISBANDED").replace("%gang%", gang.getName()));
            if(plugin.getConfig().getBoolean("gang.player_actions.disband_gang")) {
                plugin.getLocaleManager().broadcastMessage(plugin.getLocaleManager().getMessage("BROADCAST_GANG_DISBANDED")
                        .replace("%player%", p.getName())
                        .replace("%gang%", gang.getName()));
            }
            plugin.getGangUtils().prepareDisband(gang);
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang disband");
        return;
    }
}
