package net.withery.gangsx.APIManager;

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
        if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            plugin.log("PlaceholderAPI not found, placeholders will not be enabled.");
            return;
        }
        plugin.log("Hooked into PlaceholderAPI successfully");
        //load papi shit
    }
}
