package me.frxq.gangsx.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;

import java.util.List;

public class Fight {

    private final GangsX plugin;
    private final Gang challenger;
    private final Gang opponent;

    public Fight(GangsX plugin, Gang challenger, Gang opponent) {
        this.plugin = plugin;
        this.challenger = challenger;
        this.opponent = opponent;
    }
    public List<GPlayer> getChallengerRoster() { return challenger.getFightRoster(); }

    public List<GPlayer> getOpponentRoster() { return opponent.getFightRoster();}
}
