package xyz.kyngs.librepremium.common.command.commands.premium;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.audience.Audience;
import xyz.kyngs.librepremium.api.database.User;
import xyz.kyngs.librepremium.common.AuthenticLibrePremium;

public class PremiumConfirmCommand<P> extends PremiumCommand<P> {
    public PremiumConfirmCommand(AuthenticLibrePremium<P, ?> plugin) {
        super(plugin);
    }

    @CommandAlias("premiumconfirm|confirmpremium")
    @Syntax("")
    @Description("{@@librepremium.desc_premiumconfirm}")
    @CommandPermission("librepremium.player.premium")
    public void onPremiumConfirm(Audience sender, P player, User user) {
        checkAuthorized(player);
        checkCracked(user);

        plugin.getCommandProvider().onConfirm(player, sender, user);
    }

}
