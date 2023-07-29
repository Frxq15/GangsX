package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class kickCommand extends SubCommand {
    private final GangsX plugin;

    public kickCommand(GangsX plugin) {
        super("kick", "gangsx.command.kick", "/gang kick <player>", Arrays.asList("remove"));
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
        if(args.length == 1) {
            UUID uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
            GPlayer tPlayer = plugin.getGPlayerDataFactory().getGPlayerData(uuid);

            if(!tPlayer.getGangId().equals(gPlayer.getGangId())) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_YOUR_GANG");
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            tPlayer.kickFromGang();


        }
        return;
    }
}
