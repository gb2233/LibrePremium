package xyz.kyngs.librepremium.api.authorization;

import xyz.kyngs.librepremium.api.database.User;

/**
 * This interface manages user authorization.
 *
 * @param <P> The type of the player.
 * @author kyngs
 */
public interface AuthorizationProvider<P> {

    /**
     * Checks whether the user has already passed the login process.
     *
     * @param player The player.
     * @return True if the user has already passed the login process, false otherwise.
     */
    boolean isAuthorized(P player);

    boolean premiumEnabled();

    /**
     * Checks whether the player is in the process of enabling 2FA.
     *
     * @param player The player.
     * @return True if the player is in the process of enabling 2FA, false otherwise.
     */
    boolean isAwaiting2FA(P player);

    /**
     * Authorizes the player, if the player is not already authorized. Implementation must make sure that {@link #isAuthorized(P)} returns false.
     *
     * @param user   The user.
     * @param player The player.
     */
    void authorize(User user, P player);
}
