package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.enums.Role;
import net.withery.gangsx.formatting.number.NumberFormatter;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class levelCommand extends SubCommand {
    private final GangsX plugin;

    public levelCommand(GangsX plugin) {
        super("level", "gangsx.command.level", "/gang level <gang>", Arrays.asList("lvl"));
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
            //levels menu
            return;
        }
        if(args.length == 1) {
            String arg = args[0];

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if(plugin.getGangDataFactory().doesGangNameExist(arg)) {

                    UUID uuid = plugin.getGangDataFactory().getGangUniqueId(arg);
                    Gang gang = plugin.getGangDataFactory().getGangData(uuid);
                    p.sendMessage(plugin.getLocaleManager().getMessage("GANG_LEVEL")
                            .replace("%gang%", gang.getName())
                            .replace("%level%", gang.getLevel() + ""));
                    }
                plugin.getLocaleManager().sendMessage(p, "GANG_NOT_FOUND");
                });

            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang level <gang>");
    }
}
