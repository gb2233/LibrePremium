package xyz.kyngs.librepremium.common.command.commands.authorization;

import org.jetbrains.annotations.NotNull;
import xyz.kyngs.librepremium.api.database.User;
import xyz.kyngs.librepremium.common.AuthenticLibrePremium;
import xyz.kyngs.librepremium.common.command.Command;
import xyz.kyngs.librepremium.common.command.InvalidCommandArgument;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationCommand<P> extends Command<P> {

    public AuthorizationCommand(AuthenticLibrePremium<P, ?> premium) {
        super(premium);
    }

    protected void checkUnauthorized(P player) {
        if (getAuthorizationProvider().isAuthorized(player)) {
            throw new InvalidCommandArgument(getMessage("error-already-authorized"));
        }
    }

}
