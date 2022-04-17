package net.withery.gangsx.datafactory.player;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.GPlayer;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class GPlayerDataFactory {

    protected final GangsX plugin;

    public GPlayerDataFactory(GangsX plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs the initialization logic of the data factory.
     * Shouldn't be called during runtime.
     *
     * @return false if the initialization fails
     */
    public abstract boolean initialize();

    /**
     * Runs the termination logic of the data factory.
     * Shouldn't be called during runtime.
     */
    public abstract void terminate();

    /**
     * Adds the player data to the storage for the first time.
     * This method is not used to update existing data.
     * Can block the main thread ({@link GPlayerDataFactory#initializeGPlayerDataAsync(GPlayer)} may be used instead)
     *
     * @param gPlayer Player that the data will be taken from
     */
    public abstract void initializeGPlayerData(GPlayer gPlayer);

    /**
     * Asynchronously adds the player data to the storage for the first time.
     * This method is not used to update existing data.
     *
     * @param gPlayer Player that the data will be taken from
     */
    public void initializeGPlayerDataAsync(GPlayer gPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> initializeGPlayerData(gPlayer));
    }

    /**
     * Deletes the player data with the given unique id from the storage.
     * Can block the main thread ({@link GPlayerDataFactory#deleteGPlayerDataAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the player that should be deleted
     */
    public abstract void deleteGPlayerData(UUID uuid);

    /**
     * Asynchronously deletes the player data with the given unique id from the storage.
     *
     * @param uuid Unique id of the player that should be deleted
     */
    public void deleteGPlayerDataAsync(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> deleteGPlayerDataAsync(uuid));
    }

    /**
     * Gets the player data with the given unique id from the storage.
     * Can block the main thread ({@link GPlayerDataFactory#getGPlayerDataAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the player that should be loaded
     * @return The loaded player
     */
    public abstract GPlayer getGPlayerData(UUID uuid);

    /**
     * Asynchronously gets the player data with the given unique id from the storage.
     *
     * @param uuid Unique id of the player that should be loaded
     * @return A {@link CompletableFuture<GPlayer>} that will contain the loaded player, once the task has finished
     */
    public CompletableFuture<GPlayer> getGPlayerDataAsync(UUID uuid) {
        CompletableFuture<GPlayer> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(getGPlayerData(uuid)));
        return future;
    }

    /**
     * Updates the data of the given player in the storage.
     * Can block the main thread ({@link GPlayerDataFactory#updateGPlayerDataAsync(GPlayer)} may be used instead).
     *
     * @param gPlayer Player that should be updated
     */
    public abstract void updateGPlayerData(GPlayer gPlayer);

    /**
     * Asynchronously updates the data of the given gang in the storage.
     *
     * @param gPlayer Player that should be updated
     */
    public void updateGPlayerDataAsync(GPlayer gPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateGPlayerData(gPlayer));
    }

    /**
     * Checks whether player data with the given unique id exists.
     * Can block the main thread ({@link GPlayerDataFactory#doesGPlayerDataExistAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the player
     * @return true if there is any player data with the given unique id
     */
    public abstract boolean doesGPlayerDataExist(UUID uuid);


    /**
     * Asynchronously checks whether player data with the given unique id exists.
     *
     * @param uuid Unique id of the gang
     * @return A {@link CompletableFuture<Boolean>} that will contain whether any player data with the given unique id exists
     */
    public CompletableFuture<Boolean> doesGPlayerDataExistAsync(UUID uuid) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(doesGPlayerDataExist(uuid)));
        return future;
    }

    /**
     * Checks whether player data with the given unique id is loaded on the server.
     *
     * @param uuid Unique id of the player
     * @return true if the player has been loaded locally onto the server
     */
    public abstract boolean isGPlayerDataLoaded(UUID uuid);

    /**
     * Loads the player data from the storage.
     * Can block the main thread ({@link GPlayerDataFactory#loadGPlayerDataAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the player
     */
    public abstract void loadGPlayerData(UUID uuid);

    /**
     * Asynchronously loads the player data from the storage.
     *
     * @param uuid Unique id of the player
     */
    public void loadGPlayerDataAsync(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> loadGPlayerData(uuid));
    }

    /**
     * Unloads the player data from the server.
     * Can block the main thread ({@link GPlayerDataFactory#unloadGPlayerDataAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the player
     */
    public abstract void unloadGPlayerData(UUID uuid);

    /**
     * Asynchronously unloads the player data from the server.
     *
     * @param uuid Unique id of the player
     */
    public void unloadGPlayerDataAsync(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> unloadGPlayerData(uuid));
    }

    /**
     * Gets the name of the player with the given uuid.
     * Can block the main thread ({@link GPlayerDataFactory#getGPlayerNameAsync(UUID)}  may be used instead).
     *
     * @param uuid Unique id of the player
     * @return Name of the player
     */
    public abstract String getGPlayerName(UUID uuid);

    /**
     * Asynchronously gets the name of the player with the given uuid.
     *
     * @param uuid Unique id of the player
     * @return Name of the player
     */
    public CompletableFuture<String> getGPlayerNameAsync(UUID uuid) {
        CompletableFuture<String> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(getGPlayerName(uuid)));
        return future;
    }

    /**
     * Gets the unique id of the player with the given name.
     * Can block the main thread ({@link GPlayerDataFactory#getGPlayerUniqueIdAsync(String)} may be used instead).
     *
     * @param name Name of the player
     * @return Unique id of the player
     */
    public abstract UUID getGPlayerUniqueId(String name);

    /**
     * Asynchronously gets the unique id of the player with the given name.
     *
     * @param name Name of the player
     * @return Unique id of the player
     */
    public CompletableFuture<UUID> getGPlayerUniqueIdAsync(String name) {
        CompletableFuture<UUID> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(getGPlayerUniqueId(name)));
        return future;
    }

}