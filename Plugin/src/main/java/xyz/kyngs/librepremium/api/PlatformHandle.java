package xyz.kyngs.librepremium.api;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Platform-specific things that are used to interact with platform's objects
 *
 * @param <P> Player Type
 * @param <S> Server Type
 */
public interface PlatformHandle<P, S> {

    Audience getAudienceForPlayer(P player);

    UUID getUUIDForPlayer(P player);

    CompletableFuture<Throwable> movePlayer(P player, S to);

    void kick(P player, Component reason);

    S getServer(String name);

    Class<S> getServerClass();

    Class<P> getPlayerClass();

    String getIP(P player);

    void tempbanIP(P player, TextComponent reason);
}
