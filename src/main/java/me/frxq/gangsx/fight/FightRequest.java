package me.frxq.gangsx.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FightRequest {
    private final GangsX plugin;

    private final static Map<UUID, FightRequest> requests = new HashMap<>();
    private Gang challenger;

    private UUID fightID;
    private Gang opponent;

    private String arena;

    private BukkitTask bukkitTask;
    private String kit;

    private List<GPlayer> has_accepted;

    private boolean isCancelled = false;

    public FightRequest(GangsX plugin, Gang challenger) {
        this.plugin = plugin;
        this.fightID = UUID.randomUUID();
        this.challenger = challenger;
        this.initializeCountdown();
        requests.put(fightID, this);
    }
    public static FightRequest getFightRequest(GangsX plugin, Gang challenger) {
        UUID fightID = challenger.getActiveFight();
        if(!requests.containsKey(fightID) || fightID == null) {
            FightRequest fr = new FightRequest(plugin, challenger);
            fr.setChallenger(challenger);
            challenger.setActiveFight(fightID);
        }
        return requests.get(fightID);
    }
    public List<GPlayer> getChallengerRoster() { return challenger.getFightRoster(); }

    public boolean isCancelled() { return isCancelled; }
    public void setCancelled(boolean value) { isCancelled = value; }

    public List<GPlayer> getOpponentRoster() { return opponent.getFightRoster();}

    public void setChallenger(Gang gang) {
        this.challenger = challenger;
    }
    public void setOpponent(Gang gang) {
        this.opponent = opponent;
    }
    public Gang getChallenger() { return challenger; }
    public Gang getOpponent() { return opponent; }

    public static Map<UUID, FightRequest> getAllFightRequests() {
        return requests;
    }
    public static void removeRequest(UUID uuid) { requests.remove(uuid); }
    public UUID getFightID() { return fightID;}
    public void addAcceptedPlayer(GPlayer gPlayer) { has_accepted.add(gPlayer); }
    public void cancelRequest() {
        requests.remove(this);
    }
    public void setID(UUID uuid) { this.fightID = uuid; }
    public UUID getID() { return fightID; }

    public boolean isConfirmed() {
        if(has_accepted.size() == opponent.getFightRoster().size()) {
            return true;
        }
        return false;
    }
    public void acceptRequest() {
        Fight fight = Fight.getActiveFightData(plugin, challenger);
        fight.setOpponent(opponent);
        fight.setKit(kit);
        fight.setArena(arena);
        fight.initialize();
        removeRequest(getID());
    }
    public void initializeCountdown() {
            final int[] count = {plugin.getConfig().getInt("fight.request-timeout")};
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(isCancelled() || (isConfirmed())) {
                    cancel();
                }
                switch(count[0]) {
                    case 0:
                        getChallengerRoster().forEach(c -> {
                            Player p = Bukkit.getPlayer(c.getID());
                            p.sendMessage(plugin.getLocaleManager().getMessage("GANG_FIGHT_REQUEST_EXPIRED").replace("%gang%", opponent.getName()));
                        });
                        cancel();
                        return;

                    default:
                        count[0]--;
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
        }
}
