package xyz.kyngs.librepremium.common.command.commands.email;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import xyz.kyngs.librepremium.common.AuthenticLibrePremium;
import xyz.kyngs.librepremium.common.command.Command;

@CommandAlias("email")
@CommandPermission("librepremium.player.email")
public class EmailCommand <P> extends Command<P> {

    public EmailCommand(AuthenticLibrePremium<P, ?> plugin) {
        super(plugin);
    }


}
