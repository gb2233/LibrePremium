package xyz.kyngs.librepremium.common.authorization;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.title.Title;
import xyz.kyngs.librepremium.api.authorization.AuthorizationProvider;
import xyz.kyngs.librepremium.api.database.User;
import xyz.kyngs.librepremium.api.event.events.AuthenticatedEvent;
import xyz.kyngs.librepremium.common.AuthenticHandler;
import xyz.kyngs.librepremium.common.AuthenticLibrePremium;
import xyz.kyngs.librepremium.common.event.events.AuthenticAuthenticatedEvent;
import xyz.kyngs.librepremium.common.util.expiring.TimedCounter;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AuthenticAuthorizationProvider<P, S> extends AuthenticHandler<P, S> implements AuthorizationProvider<P> {

    private final Map<P, Boolean> unAuthorized;

    private  Map<String, TimedCounter<String>> ipLoginFailureCounts;
    private final Set<P> awaiting2FA;

    public AuthenticAuthorizationProvider(AuthenticLibrePremium<P, S> plugin) {
        super(plugin);
        unAuthorized = new ConcurrentHashMap<>();
        awaiting2FA = ConcurrentHashMap.newKeySet();
        ipLoginFailureCounts = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isAuthorized(P player) {
        return !unAuthorized.containsKey(player);
    }
    @Override
    public boolean premiumEnabled() {
        return plugin.getConfiguration().premiumEnabled();
    }

    @Override
    public boolean isAwaiting2FA(P player) {
        return awaiting2FA.contains(player);
    }

    @Override
    public void authorize(User user, P player) {
        if (isAuthorized(player)) {
            throw new IllegalStateException("Player is already authorized");
        }
        stopTracking(player);

        var audience = platformHandle.getAudienceForPlayer(player);

        audience.clearTitle();
        String IP = plugin.getPlatformHandle().getIP(player);
        user.setIP(IP);
        plugin.getDatabaseProvider().updateUser(user);
        resetCount(IP,user.getLastNickname());

        plugin.getEventProvider().fire(AuthenticatedEvent.class, new AuthenticAuthenticatedEvent<>(user, player, plugin));
        plugin.authorize(player, user, audience);
    }

    public void startTracking(User user, P player) {
        var audience = platformHandle.getAudienceForPlayer(player);

        unAuthorized.put(player, user.isRegistered());

        plugin.cancelOnExit(plugin.delay(() -> {
            if (!unAuthorized.containsKey(player)) return;
            sendInfoMessage(user.isRegistered(), audience);
        }, 250), player);

        var limit = plugin.getConfiguration().secondsToAuthorize();

        if (limit > 0) {
            plugin.cancelOnExit(plugin.delay(() -> {
                if (!unAuthorized.containsKey(player)) return;
                platformHandle.kick(player, plugin.getMessages().getMessage("kick-time-limit"));
            }, limit * 1000L), player);
        }
    }

    private void sendInfoMessage(boolean registered, Audience audience) {
        audience.sendMessage(plugin.getMessages().getMessage(registered ? "prompt-login" : "prompt-register"));
        if (!plugin.getConfiguration().useTitles()) return;
        var toRefresh = plugin.getConfiguration().milliSecondsToRefreshNotification();
        //noinspection UnstableApiUsage
        audience.showTitle(Title.title(
                plugin.getMessages().getMessage(registered ? "title-login" : "title-register"),
                plugin.getMessages().getMessage(registered ? "sub-title-login" : "sub-title-register"),
                Title.Times.of(
                        Duration.ofMillis(0),
                        Duration.ofMillis(toRefresh > 0 ?
                                (long) (toRefresh * 1.1) :
                                10000
                        ),
                        Duration.ofMillis(0)
                )
        ));
    }

    public void stopTracking(P player) {
        unAuthorized.remove(player);
    }

    public void notifyUnauthorized() {
        var wrong = new HashSet<P>();
        unAuthorized.forEach((player, registered) -> {
            var audience = platformHandle.getAudienceForPlayer(player);

            if (audience == null) {
                wrong.add(player);
                return;
            }

            sendInfoMessage(registered, audience);

        });

        wrong.forEach(unAuthorized::remove);
    }

    public void onExit(P player) {
        stopTracking(player);
        awaiting2FA.remove(player);
    }

    public void beginTwoFactorAuth(User user, P player) {
        awaiting2FA.add(player);

        platformHandle.movePlayer(player, plugin.chooseLimbo(user, player)).whenComplete((t, e) -> {
            if (t != null || e != null) awaiting2FA.remove(player);
        });
    }

    public boolean shouldTempbanIP(P player) {
        if (plugin.getConfiguration().tempbanEnabled()){
            TimedCounter<String> countsByName = ipLoginFailureCounts.get(plugin.getPlatformHandle().getIP(player));
            if (countsByName != null) {
                return countsByName.total() >= plugin.getConfiguration().getTempbanMaxTries();
            }
        }
        return false;
    }
    public void performCleanup() {
        for (TimedCounter<String> countsByIp : ipLoginFailureCounts.values()) {
            countsByIp.removeExpiredEntries();
        }
        ipLoginFailureCounts.entrySet().removeIf(e -> e.getValue().isEmpty());
    }


    public void increaseCount(P player, String name) {
        if (plugin.getConfiguration().tempbanEnabled()) {
            TimedCounter<String> countsByName = ipLoginFailureCounts.computeIfAbsent(
                plugin.getPlatformHandle().getIP(player), k -> new TimedCounter<>(plugin.getConfiguration().getTempbanCounterReset(), TimeUnit.MINUTES));
            countsByName.increment(name);
        }
    }
    public void resetCount(String address, String name) {
        if (plugin.getConfiguration().tempbanEnabled()) {
            TimedCounter<String> counter = ipLoginFailureCounts.get(address);
            if (counter != null) {
                counter.remove(name);
            }
        }
    }

    public boolean shouldBlockReg(P player) {
        return plugin.getDatabaseProvider().getUsersByIP(plugin.getPlatformHandle().getIP(player)).size() >= plugin.getConfiguration().maxRegPerIP();
    }

}
