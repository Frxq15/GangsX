package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import net.withery.gangsx.settings.locale.LocaleReference;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class infoCommand extends SubCommand {
    private final GangsX plugin;

    public infoCommand(GangsX plugin) {
        super("info", "gcgangs.command.info", "/gang info <gang>", Arrays.asList("show", "view"));
        this.plugin = plugin;
    }
    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            if(!(sender instanceof Player)) {
                plugin.getLocaleRegistry().sendMessage(sender, LocaleReference.COMMAND_WRONG_USAGE, "/gang info <gang>");
                return;
            }
            Player p = (Player) sender;
            GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
            if(!gPlayer.hasGang()) {
                plugin.getLocaleRegistry().sendMessage(p, LocaleReference.COMMAND_PLAYER_NOT_IN_A_GANG);
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            //TODO: change getmembers to show online ppl as green and offline as red
            plugin.getLocaleRegistry().sendMessage(sender, LocaleReference.COMMAND_GANG_INFO, gang.getName(),
                    gang.getCreationDate(), Bukkit.getOfflinePlayer(gang.getLeader()).getName(), String.valueOf(gang.getLevel()),
                    String.valueOf(gang.getKills()),
                    String.valueOf(gang.getDeaths()), String.valueOf(gang.getBlocksBroken()), "insert members",
                    "insert allies", String.format("%,d", gang.getCoins()), String.format("%,d", (int)gang.getBankBalance()));
            return;
        }
        if(args.length == 2) {
            String arg = args[0];
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if(plugin.getGangDataFactory().doesGangNameExist(arg)) {
                    //arg provided is a gang
                    UUID uuid = plugin.getGangDataFactory().getGangUniqueId(arg);
                    if(plugin.getGangDataFactory().isGangDataLoaded(uuid)) {

                    }
                    plugin.getGangDataFactory().initializeGangData(plugin.getGangDataFactory().getGangData(uuid));
                }
            });
            return;
        }
        plugin.getLocaleRegistry().sendMessage(sender, LocaleReference.COMMAND_WRONG_USAGE, "/gang info <gang>");
        return;
    }
}
