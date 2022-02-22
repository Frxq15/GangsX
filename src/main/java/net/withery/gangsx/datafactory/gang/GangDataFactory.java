package net.withery.gangsx.datafactory.gang;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class GangDataFactory {

    private final GangsX plugin;

    public GangDataFactory(GangsX plugin) {
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
     * Adds the gang data to the storage for the first time.
     * This method is not made to update existing data.
     * Can block the main thread ({@link GangDataFactory#initializeGangDataAsync(Gang)} may be used instead)
     *
     * @param gang Gang that the data will be taken from
     */
    public abstract void initializeGangData(Gang gang);

    /**
     * Asynchronously adds the gang data to the storage for the first time.
     * This method is not made to update existing data.
     *
     * @param gang Gang that the data will be taken from
     */
    public void initializeGangDataAsync(Gang gang) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> initializeGangData(gang));
    }

    /**
     * Deletes the gang data with the given unique id from the storage.
     * Can block the main thread ({@link GangDataFactory#deleteGangDataAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the gang that should be deleted
     */
    public abstract void deleteGangData(UUID uuid);

    /**
     * Asynchronously deletes the gang data with the given unique id from the storage.
     *
     * @param uuid Unique id of the gang that should be deleted
     */
    public void deleteGangDataAsync(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> deleteGangDataAsync(uuid));
    }

    /**
     * Gets the gang data with the given unique id from the storage.
     * If there isn't any gang with the given unique id, a new gang will be created.
     * Can block the main thread ({@link GangDataFactory#getGangDataAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the gang that should be loaded
     * @return The loaded gang
     */
    public abstract Gang getGangData(UUID uuid);

    /**
     * Asynchronously gets the gang data with the given unique id from the storage.
     * If there isn't any gang with the given unique id, a new gang will be created.
     *
     * @param uuid Unique id of the gang that should be loaded
     * @return A {@link CompletableFuture<Gang>} that will contain the loaded gang, once the task has finished
     */
    public CompletableFuture<Gang> getGangDataAsync(UUID uuid) {
        CompletableFuture<Gang> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(getGangData(uuid)));
        return future;
    }

    /**
     * Updates the data of the given gang in the storage.
     * Can block the main thread ({@link GangDataFactory#updateGangDataAsync(Gang)} may be used instead).
     *
     * @param gang Gang that should be updated
     */
    public abstract void updateGangData(Gang gang);

    /**
     * Asynchronously updates the data of the given gang in the storage.
     *
     * @param gang Gang that should be updated
     */
    public void updateGangDataAsync(Gang gang) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateGangData(gang));
    }

    /**
     * Checks whether gang data with the given unique id exists.
     * Can block the main thread ({@link GangDataFactory#doesGangDataExistAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the gang
     * @return true if there is any gang data with the given unique id
     */
    public abstract boolean doesGangDataExist(UUID uuid);


    /**
     * Asynchronously checks whether gang data with the given unique id exists.
     *
     * @param uuid Unique id of the gang
     * @return A {@link CompletableFuture<Boolean>} that will contain whether any gang data with the given unique id exists
     */
    public CompletableFuture<Boolean> doesGangDataExistAsync(UUID uuid) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(doesGangDataExist(uuid)));
        return future;
    }

    /**
     * Checks whether gang data with the given unique id is loaded on the server.
     *
     * @param uuid Unique id of the gang
     * @return true if the gang has been loaded locally onto the server
     */
    public abstract boolean isGangDataLoaded(UUID uuid);

    /**
     * Loads the gang data from the storage.
     * Can block the main thread ({@link GangDataFactory#loadGangDataAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the gang
     */
    public abstract void loadGangData(UUID uuid);

    /**
     * Asynchronously loads the gang data from the storage.
     *
     * @param uuid Unique id of the gang
     */
    public void loadGangDataAsync(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> loadGangData(uuid));
    }

    /**
     * Unloads the gang data from the server.
     * Can block the main thread ({@link GangDataFactory#unloadGangDataAsync(UUID)} may be used instead).
     *
     * @param uuid Unique id of the gang
     */
    public abstract void unloadGangData(UUID uuid);

    /**
     * Asynchronously unloads the gang data from the server.
     *
     * @param uuid Unique id of the gang
     */
    public void unloadGangDataAsync(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> unloadGangData(uuid));
    }

    /**
     * Gets the name of the gang with the given uuid.
     * Can block the main thread ({@link GangDataFactory#getGangNameAsync(UUID)}  may be used instead).
     *
     * @param uuid Unique id of the gang
     * @return Name of the gang
     */
    public abstract String getGangName(UUID uuid);

    /**
     * Asynchronously gets the name of the gang with the given uuid.
     *
     * @param uuid Unique id of the gang
     * @return Name of the gang
     */
    public CompletableFuture<String> getGangNameAsync(UUID uuid) {
        CompletableFuture<String> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(getGangName(uuid)));
        return future;
    }

    /**
     * Gets the unique id of the gang with the given name.
     * Can block the main thread ({@link GangDataFactory#getGangUniqueIdAsync(String)} may be used instead).
     *
     * @param name Name of the gang
     * @return Unique id of the gang
     */
    public abstract UUID getGangUniqueId(String name);

    /**
     * Asynchronously gets the unique id of the gang with the given name.
     *
     * @param name Name of the gang
     * @return Unique id of the gang
     */
    public CompletableFuture<UUID> getGangUniqueIdAsync(String name) {
        CompletableFuture<UUID> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(getGangUniqueId(name)));
        return future;
    }

}