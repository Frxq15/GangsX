package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import net.withery.gangsx.settings.locale.LocaleReference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class infoCommand extends SubCommand {
    private final GangsX plugin;

    public infoCommand(GangsX plugin) {
        super("info", "gcgangs.command.info", "/gang info <gang>", Arrays.asList("show", "view"));
        this.plugin = plugin;
    }
    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            if(!(sender instanceof Player)) {
                plugin.log("This command cannot be executed from console.");
                return;
            }
            Player p = (Player) sender;
            GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
            if(!gPlayer.hasGang()) {
                plugin.getLocaleRegistry().sendMessage(p, LocaleReference.COMMAND_PLAYER_NOT_IN_A_GANG);
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());

            return;
        }
        if(args.length == 2) {
            return;
        }
        plugin.getLocaleRegistry().sendMessage(sender, LocaleReference.COMMAND_WRONG_USAGE, "/gang info <gang>");
        return;
    }
}
