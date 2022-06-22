package xyz.kyngs.librepremium.common.command.commands.premium;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.kyori.adventure.audience.Audience;
import xyz.kyngs.librepremium.api.database.User;
import xyz.kyngs.librepremium.common.AuthenticLibrePremium;

@CommandAlias("premiumconfirm|confirmpremium")
@CommandPermission("librepremium.player.premium")
public class PremiumConfirmCommand<P> extends PremiumCommand<P> {
    public PremiumConfirmCommand(AuthenticLibrePremium<P, ?> plugin) {
        super(plugin);
    }

    @Default
    public void onPremiumConfirm(Audience sender, P player, User user) {
        checkAuthorized(player);
        checkCracked(user);

        plugin.getCommandProvider().onConfirm(player, sender, user);
    }

}
