package net.withery.gangsx.datafactory.gang;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.Objects.Gang;

import java.util.UUID;

public class SQLGangDataFactory extends GangDataFactory {

    public SQLGangDataFactory(GangsX plugin) {
        super(plugin);
    }

    /*
    Hey Dan, this is the class you wanted to work on.
    You basically just need to fill in all the methods with your code that makes it do whatever it's supposed to do.
    As this is the SQLGangDataFactory, you could guess it's all about making this work with an SQL Database (and only
                                        covering the gang objects, the players are going to be handled independently).

    You can look at what the methods are supposed to do, by looking at the methods tooltips or reading the java docs I
    wrote in the GangDataFactory class (which this file is extending).

    It would be smart to add some sort of Map in this class which stores the UUID -> Gang (<UUID, Gang>), which would
    kind of be used as a cache (loadGangData, unloadGangData and isGangDataLoaded are methods that are related to that;
    getGangData should pull from the cache, if it contains the gang, instead of querying).

    There is no need to worry about making anything asynchronous as this is covered by the parent class (Methods such
    as initializeGangDataAsync which call the normal methods asynchronously and return the result as CompletableFuture).

    If you have any questions feel free to message me on Discord (kleinesNugget#5354 🙂)

    Yours sincerely
    Sven De riches
     */

    @Override
    public boolean initialize() {
        return false;
    }

    @Override
    public void terminate() {

    }

    @Override
    public void initializeGangData(Gang gang) {

    }

    @Override
    public void deleteGangData(UUID uuid) {

    }

    @Override
    public Gang getGangData(UUID uuid) {
        return null;
    }

    @Override
    public void updateGangData(Gang gang) {

    }

    @Override
    public boolean doesGangDataExist(UUID uuid) {
        return false;
    }

    @Override
    public boolean isGangDataLoaded(UUID uuid) {
        return false;
    }

    @Override
    public void loadGangData(UUID uuid) {

    }

    @Override
    public void unloadGangData(UUID uuid) {

    }

    @Override
    public String getGangName(UUID uuid) {
        return null;
    }

    @Override
    public UUID getGangUniqueId(String name) {
        return null;
    }

}