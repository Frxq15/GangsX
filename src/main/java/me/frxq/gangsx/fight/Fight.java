package me.frxq.gangsx.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Fight {

    private final GangsX plugin;

    private final static Map<UUID, Fight> fights = new HashMap<>();
    private Gang challenger;

    private UUID fightID;
    private Gang opponent;

    public Fight(GangsX plugin) {
        this.plugin = plugin;
        this.fightID = UUID.randomUUID();
        fights.put(fightID, this);
    }
    public static Fight getActiveFightData(GangsX plugin, Gang challenger) {
        UUID fightID = challenger.getActiveFight();
        if(!fights.containsKey(fightID) || fightID == null) {
            Fight fight = new Fight(plugin);
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
}
