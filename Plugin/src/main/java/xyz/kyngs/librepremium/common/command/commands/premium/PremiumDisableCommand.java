package xyz.kyngs.librepremium.common.command.commands.premium;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.audience.Audience;
import xyz.kyngs.librepremium.api.database.User;
import xyz.kyngs.librepremium.api.event.events.PremiumLoginSwitchEvent;
import xyz.kyngs.librepremium.common.AuthenticLibrePremium;
import xyz.kyngs.librepremium.common.event.events.AuthenticPremiumLoginSwitchEvent;

public class PremiumDisableCommand<P> extends PremiumCommand<P> {

    public PremiumDisableCommand(AuthenticLibrePremium<P, ?> premium) {
        super(premium);
    }

    @CommandAlias("cracked|manuallogin")
    @Syntax("")
    @Description("{@@librepremium.desc_premiumdisable}")
    @CommandPermission("librepremium.player.premium")
    public void onCracked(Audience sender, P player, User user) {
        checkAuthorized(player);
        checkPremium(user);

        sender.sendMessage(getMessage("info-disabling"));

        user.setPremiumUUID(null);

        plugin.getEventProvider().fire(PremiumLoginSwitchEvent.class, new AuthenticPremiumLoginSwitchEvent<>(user, player, plugin));

        getDatabaseProvider().updateUser(user);

        plugin.getPlatformHandle().kick(player, getMessage("kick-premium-info-disabled"));
    }

}
