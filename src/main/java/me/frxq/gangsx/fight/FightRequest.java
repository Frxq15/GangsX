package me.frxq.gangsx.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FightRequest {
    private final GangsX plugin;

    private final static Map<UUID, FightRequest> requests = new HashMap<>();
    private Gang challenger;

    private UUID fightID;
    private Gang opponent;

    private String arena;
    private String kit;

    private List<GPlayer> has_accepted;

    public FightRequest(GangsX plugin) {
        this.plugin = plugin;
        this.fightID = UUID.randomUUID();
        requests.put(fightID, this);
    }
    public static FightRequest getFightRequest(GangsX plugin, Gang challenger) {
        UUID fightID = challenger.getActiveFight();
        if(!requests.containsKey(fightID) || fightID == null) {
            FightRequest fr = new FightRequest(plugin);
            fr.setChallenger(challenger);
        }
        return requests.get(fightID);
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

    public static Map<UUID, FightRequest> getAllFightRequests() {
        return requests;
    }
    public static void removeRequest(UUID uuid) { requests.remove(uuid); }
    public UUID getFightID() { return fightID;}
    public void addAcceptedPlayer(GPlayer gPlayer) { has_accepted.add(gPlayer); }
    public void declineRequest() {
        requests.remove(this);
    }
    public void setID(UUID uuid) { this.fightID = uuid; }
}
