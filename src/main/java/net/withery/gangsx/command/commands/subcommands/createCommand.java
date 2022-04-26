package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import net.withery.gangsx.settings.locale.LocaleReference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public class createCommand extends SubCommand {
    private final GangsX plugin;

    public createCommand(GangsX plugin) {
        super("create", "gangsx.command.help", "/gang create <name>", Arrays.asList("new", "start"));
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
        if(gPlayer.hasGang()) {
            plugin.getLocaleRegistry().sendMessage(p, LocaleReference.COMMAND_PLAYER_ALREADY_IN_GANG);
            return;
        }
        if(args.length == 1) {
            String name = args[0];
            new BukkitRunnable() {
                @Override
                public void run() {
                    UUID gangId = UUID.randomUUID();
                    while (plugin.getGangDataFactory().doesGangDataExist(gangId)) {
                        gangId = UUID.randomUUID();
                    }
                    Gang gang = new Gang(plugin, gangId, name, p.getUniqueId());
                    plugin.getGangDataFactory().initializeGangData(gang);
                    gPlayer.setGangId(gang.getID());
                    gPlayer.setHasGang(true);
                    plugin.getLocaleRegistry().sendMessageToAll(LocaleReference.COMMAND_GANG_CREATED, gang.getName(), p.getName());
                    plugin.getLocaleRegistry().sendMessage(p, LocaleReference.COMMAND_PLAYER_GANG_CREATED);
                    return;
                }

            }.runTaskAsynchronously(plugin);
            return;
        }
        plugin.getLocaleRegistry().sendMessage(p, LocaleReference.COMMAND_WRONG_USAGE, this.getUsage());
        return;
    }
}
