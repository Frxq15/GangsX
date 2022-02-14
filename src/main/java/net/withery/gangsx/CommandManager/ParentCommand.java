package net.withery.gangsx.CommandManager;

import net.withery.gangsx.GangsX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ParentCommand implements CommandExecutor, TabCompleter {
    protected final GangsX plugin;
    protected final String name;
    protected final String permission;

    private final Set<SubCommand> subCommands = new HashSet<>();

    protected String subLabel;
    protected String[] subArgs;

    public ParentCommand(GangsX plugin, String name, String permission) {
        this.plugin = plugin;
        this.name = name;
        this.permission = permission;
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(permission)) return null;

        List<String> results = null;
        if (args.length == 1) {
            results = new ArrayList<>();
            for (SubCommand subCommand : getSubCommands()) {
                if (sender.hasPermission(subCommand.getPermission())) {
                    results.add(subCommand.getCommand());
                    results.addAll(Arrays.asList(subCommand.getAliases()));
                }
            }
        }

        if (args.length >= 2) {
            results = new ArrayList<>();
            subLabel = args[0];
            subArgs = Arrays.copyOfRange(args, 1, args.length);

            SubCommand subCommand = getExecutor(subLabel);

            if (subCommand != null && sender.hasPermission(subCommand.getPermission()))
                results.addAll(subCommand.onTabComplete(sender, command, subLabel, subArgs));
        }

        if (results != null && !results.isEmpty()) {
            results = StringUtil.copyPartialMatches(args[args.length - 1], results, new ArrayList<>(results.size()));
            results.sort(String.CASE_INSENSITIVE_ORDER);
        }

        return results;
    }

    public void register(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public boolean exists(String label) {
        for (SubCommand subCommand : subCommands)
            if (subCommand.getCommand().equalsIgnoreCase(label))
                return true;

            else
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(label))
                        return true;

        return false;
    }

    public SubCommand getExecutor(String label) {
        for (SubCommand subCommand : subCommands)
            if (subCommand.getCommand().equalsIgnoreCase(label))
                return subCommand;

            else
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(label))
                        return subCommand;

        return null;
    }

    public Set<SubCommand> getSubCommands() {
        return subCommands;
    }

    protected void sendHelp(CommandSender sender, int page) {
        final int COMMANDS_PER_PAGE = 9;
        final int COMMANDS = getSubCommands().size();
        final int PAGES = (int) Math.ceil((double) COMMANDS / COMMANDS_PER_PAGE);

        if (page < 0)
            page = 1;

        if (page > PAGES)
            page = PAGES;

        List<SubCommand> subCommands = getSubCommands().stream().sorted(Comparator.comparing(SubCommand::getCommand)).collect(Collectors.toList());

        sender.sendMessage("HELP " + page + "/" + PAGES);

        int i = 0;
        for (SubCommand subCommand : subCommands) {
            if (i < ((page - 1) * COMMANDS_PER_PAGE)) {
                i++;
                continue;
            }

            if (i++ >= (COMMANDS_PER_PAGE * page)) break;
            String usage = subCommand.getUsage();
            sender.sendMessage("/" + name + " " + subCommand.getCommand());
        }
    }

}
