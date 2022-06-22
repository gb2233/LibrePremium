package xyz.kyngs.librepremium.common.command.commands.authorization;

import co.aikar.commands.annotation.*;
import net.kyori.adventure.audience.Audience;
import xyz.kyngs.librepremium.api.database.User;
import xyz.kyngs.librepremium.common.AuthenticLibrePremium;
import xyz.kyngs.librepremium.common.command.InvalidCommandArgument;

@CommandAlias("register|reg")
@CommandPermission("librepremium.player.register")
public class RegisterCommand<P> extends AuthorizationCommand<P> {
    public RegisterCommand(AuthenticLibrePremium<P, ?> premium) {
        super(premium);
    }

    @Default
    @Syntax("<password> <passwordRepeat>")
    @CommandCompletion("password password")
    public void onRegister(Audience sender, P player, User user, @Single String password, String passwordRepeat) {
        checkUnauthorized(player);

        if (user.isRegistered()) throw new InvalidCommandArgument(getMessage("error-already-registered"));
        if (plugin.getAuthorizationProvider().shouldBlockReg(player)) throw new InvalidCommandArgument(getMessage("more-regs-pre-ip-than-allowed"));
        if (!password.contentEquals(passwordRepeat))
            throw new InvalidCommandArgument(getMessage("error-password-not-match"));
        if (!plugin.validPassword(password))
            throw new InvalidCommandArgument(getMessage("error-forbidden-password"));

        sender.sendMessage(getMessage("info-registering"));

        var provider = plugin.getDefaultCryptoProvider();

        user.setHashedPassword(provider.createHash(password));

        getDatabaseProvider().updateUser(user);

        sender.sendMessage(getMessage("info-registered"));

        getAuthorizationProvider().authorize(user, player);


    }

}
