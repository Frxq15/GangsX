package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class helpCommand extends SubCommand {
    private final GangsX plugin;
    public helpCommand(GangsX plugin) {
        super("help", "gangsx.command.help", "/gang help <category>", Arrays.asList("?"));
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender s, Command command, String label, String[] args) {
        if(args.length == 0) {
            for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_MAIN")) {
                plugin.getLocaleManager().sendRawMessage(s, lines);
            }
            return;
        }
            String category = args[0];

            switch(category.toLowerCase()) {

                case "social":
                    for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_SOCIAL_MAIN")) {
                        plugin.getLocaleManager().sendRawMessage(s, lines);
                    }
                    return;

                case "currency":
                    for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_CURRENCY_MAIN")) {
                        plugin.getLocaleManager().sendRawMessage(s, lines);
                    }
                    return;

                case "manage":
                    for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_MANAGE_MAIN")) {
                        plugin.getLocaleManager().sendRawMessage(s, lines);
                    }
                    return;

                case "player":
                    if(args.length == 2) {
                        String page = args[1];
                        if(Integer.parseInt(page) > 2) {
                            plugin.getLocaleManager().sendMessage(s, "GANG_HELP_INVALID_CATEGORY");
                            return;
                        }
                        if(Integer.parseInt(page) == 0) {
                            for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_PLAYER_1")) {
                                plugin.getLocaleManager().sendRawMessage(s, lines);
                            }
                            return;
                        }
                        for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_PLAYER_"+page)) {
                            plugin.getLocaleManager().sendRawMessage(s, lines);
                        }
                        return;
                    }
                    //no page specified
                    for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_PLAYER_1")) {
                        plugin.getLocaleManager().sendRawMessage(s, lines);
                    }
                    return;

                default:
                    s.sendMessage(plugin.getLocaleManager().getMessage("GANG_HELP_INVALID_CATEGORY"));
                    return;
        }
    }
}
