package xyz.kyngs.librepremium.common.command.commands;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.audience.Audience;
import xyz.kyngs.librepremium.api.database.User;
import xyz.kyngs.librepremium.api.event.events.PasswordChangeEvent;
import xyz.kyngs.librepremium.common.AuthenticLibrePremium;
import xyz.kyngs.librepremium.common.command.Command;
import xyz.kyngs.librepremium.common.command.InvalidCommandArgument;
import xyz.kyngs.librepremium.common.event.events.AuthenticPasswordChangeEvent;

public class ChangePasswordCommand<P> extends Command<P> {
    public ChangePasswordCommand(AuthenticLibrePremium<P, ?> plugin) {
        super(plugin);
    }

    @CommandAlias("changepassword|changepass|passwd|passch|changepw")
    @Syntax("<oldPassword> <newPassword>")
    @CommandCompletion("oldPassword newPassword")
    @Description("{@@librepremium.desc_changepass}")
    @CommandPermission("librepremium.player.changepassword")
    public void onPasswordChange(Audience sender, P player, User user, String oldPass, @Single String newPass) {
        checkAuthorized(player);

        sender.sendMessage(getMessage("info-editing"));

        var hashed = user.getHashedPassword();
        var crypto = getCrypto(hashed);

        if (!plugin.validPassword(newPass))
            throw new InvalidCommandArgument(getMessage("error-forbidden-password"));

        if (!crypto.matches(oldPass, hashed)) {
            throw new InvalidCommandArgument(getMessage("error-password-wrong"));
        }

        var defaultProvider = plugin.getDefaultCryptoProvider();

        user.setHashedPassword(defaultProvider.createHash(newPass));

        getDatabaseProvider().updateUser(user);

        sender.sendMessage(getMessage("info-edited"));

        plugin.getEventProvider().fire(PasswordChangeEvent.class, new AuthenticPasswordChangeEvent<>(user, player, plugin, hashed));
    }
}
