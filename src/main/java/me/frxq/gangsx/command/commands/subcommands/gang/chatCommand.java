package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class chatCommand extends SubCommand {
    private final GangsX plugin;

    public chatCommand(GangsX plugin) {
        super("chat", "gangsx.command.chat", "/gang chat", Arrays.asList("msg"));
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
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        if(!plugin.getGangUtils().playerHasGangPermission(gPlayer,gang, Permission.GANG_CHAT)) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_NO_PERMISSION");
            return;
        }
        if(args.length == 0) {
            if(gPlayer.hasChatEnabled()) {
                gPlayer.setChatEnabled(false);
                plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_CHAT_DISABLED");
                return;
            }
            gPlayer.setChatEnabled(true);
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_CHAT_ENABLED");
            return;
        }
        if(args.length > 0) {
            String message = plugin.getCommandUtils().getFinalArg(args, 0);

            gang.sendMessage(plugin.getConfig().getString("gang.chat_format")
                    .replace("%role%", gPlayer.getRole().getRolePrefix())
                    .replace("%player%", p.getName())
                    .replace("%message%", message));
        }
    }
}
