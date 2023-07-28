package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.gui.menus.Shop;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class setdescriptionCommand extends SubCommand {
    private final GangsX plugin;

    public setdescriptionCommand(GangsX plugin) {
        super("setdescription", "gangsx.command.setdescription", "/gang setdescription <description>",
                Arrays.asList("setdesc", "description", "desc"));
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
        if(args.length > 2) {
            String description = plugin.getCommandUtils().getFinalArg(args, 1);

            if(plugin.getCommandUtils().containsBlacklistedWords(description)) {
                plugin.getLocaleManager().sendMessage(p, "BLACKLISTED_WORDS");
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            gang.setDescription(description);
            gang.sendMessage(plugin.getLocaleManager().getMessage("DESCRIPTION_SET").replace("%description%", description));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang setdescription <description>");
    }
}
