package net.withery.gangsx.datafactory.player.sql;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.datafactory.player.GPlayerDataFactory;
import net.withery.gangsx.objects.GPlayer;

import java.util.UUID;

public class SQLGPlayerDataFactory extends GPlayerDataFactory {

    public SQLGPlayerDataFactory(GangsX plugin) {
        super(plugin);
    }

    @Override
    public boolean initialize() {
        return false;
    }

    @Override
    public void terminate() {

    }

    @Override
    public void initializeGPlayerData(GPlayer gPlayer) {

    }

    @Override
    public void deleteGPlayerData(UUID uuid) {

    }

    @Override
    public GPlayer getGPlayerData(UUID uuid) {
        return null;
    }

    @Override
    public void updateGPlayerData(GPlayer gPlayer) {

    }

    @Override
    public boolean doesGPlayerDataExist(UUID uuid) {
        return false;
    }

    @Override
    public boolean isGPlayerDataLoaded(UUID uuid) {
        return false;
    }

    @Override
    public void loadGPlayerData(UUID uuid) {

    }

    @Override
    public void unloadGPlayerData(UUID uuid) {

    }

    @Override
    public String getGPlayerName(UUID uuid) {
        return null;
    }

    @Override
    public UUID getGPlayerUniqueId(String name) {
        return null;
    }

}