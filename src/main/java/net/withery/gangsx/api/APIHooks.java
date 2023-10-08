package net.withery.gangsx.api;

import net.milkbowl.vault.economy.Economy;
import net.withery.gangsx.GangsX;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class APIHooks {
    private final GangsX plugin;
    private Economy econ = null;

    public APIHooks(GangsX plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        setupEconomy();
        setupPapi();
    }
    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.log("Vault not found, economy commands will not be enabled.");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        plugin.log("Hooked into Vault successfully");
        return econ != null;
    }
    public void setupPapi() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            plugin.log("PlaceholderAPI not found, placeholders will not be enabled.");
            return;
        }
        new Placeholders(plugin).register();
        plugin.log("Hooked into PlaceholderAPI successfully");
        return;
    }
    public Economy getEconomy() {
        return econ;
    }
}
