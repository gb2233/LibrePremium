package xyz.kyngs.librepremium.api.database;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * This interface is used to read from the database.
 *
 * @author kyngs
 */
public interface ReadDatabaseProvider {

    /**
     * This method finds a player by their name.
     *
     * @param name The name of the player.
     * @return The player, or null if the player does not exist.
     */
    User getByName(String name);

    /**
     * This method finds a player by their UUID.
     *
     * @param uuid The UUID of the player.
     * @return The player, or null if the player does not exist.
     */
    User getByUUID(UUID uuid);

    /**
     * This method finds a player by their premium UUID.
     *
     * @param uuid The premium UUID of the player.
     * @return The player, or null if the player does not exist.
     */
    User getByPremiumUUID(UUID uuid);

    List<User> getUsersByIP(String ipAddress);

    /**
     * This method fetches all players. <b>Use this with caution.</b>
     *
     * @return A collection of all players.
     */
    Collection<User> getAllUsers();

}
