package me.frxq.gangsx.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Fight {

    private final GangsX plugin;

    private final static Map<UUID, Fight> fights = new HashMap<>();
    private HashMap<Player, ItemStack[]> inventories = new HashMap<>();
    private Gang challenger;

    private UUID fightID;
    private Gang opponent;
    private String kit;
    private String arena;

    public Fight(GangsX plugin) {
        this.plugin = plugin;
        this.fightID = UUID.randomUUID();
        fights.put(fightID, this);
    }
    public static Fight getActiveFightData(GangsX plugin, Gang challenger) {
        UUID fightID = challenger.getActiveFight();
        if(!fights.containsKey(fightID) || fightID == null) {
            Fight fight = new Fight(plugin);
            fight.setChallenger(challenger);
        }
        return fights.get(fightID);
    }
    public List<GPlayer> getChallengerRoster() { return challenger.getFightRoster(); }

    public List<GPlayer> getOpponentRoster() { return opponent.getFightRoster();}

    public void setChallenger(Gang gang) {
        this.challenger = challenger;
    }
    public void setOpponent(Gang gang) {
        this.opponent = opponent;
    }
    public Gang getChallenger() { return challenger; }
    public Gang getOpponent() { return opponent; }

    public static Map<UUID, Fight> getAllFightData() {
        return fights;
    }
    public static void removeFight(UUID uuid) { fights.remove(uuid); }
    public UUID getFightID() { return fightID;}
    public void setKit(String kit) { this.kit = kit; }
    public void setArena(String arena) { this.arena = arena; }

    public String getKit() { return kit; }
    public String getArena() { return arena; }

    public void initialize() {
        Bukkit.broadcastMessage("fight confirmed");
    }
    public void saveInventory(Player player) {
        ItemStack[] playerinv = player.getInventory().getContents();
        inventories.put(player, playerinv);
    }
    public void returnInventory(Player player) {
        ItemStack[] items = inventories.get(player);
        for(ItemStack item : items){
            player.getInventory().addItem(item);
        }
    }
    public void saveInventories() {
        challenger.getFightRoster().forEach(g -> {
            Player p = Bukkit.getPlayer(g.getID());
            saveInventory(p);
        });
        opponent.getFightRoster().forEach(o -> {
            Player p = Bukkit.getPlayer(o.getID());
            saveInventory(p);
        });
    }
    public void returnInventories() {
        challenger.getFightRoster().forEach(g -> {
            Player p = Bukkit.getPlayer(g.getID());
            returnInventory(p);
        });
        opponent.getFightRoster().forEach(o -> {
            Player p = Bukkit.getPlayer(o.getID());
            returnInventory(p);
        });
    }
}
