package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class onlineCommand extends SubCommand {
    private final GangsX plugin;

    public onlineCommand(GangsX plugin) {
        super("online", "gangsx.command.online", "/gang online", Arrays.asList("onlinemembers"));
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
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            p.sendMessage(plugin.getLocaleManager().getMessage("GANG_ONLINE")
                    .replace("%gang%", gang.getName()).replace("%members%", gang.convertOnlineMembersForInfo()));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang online");
        return;
    }
}
