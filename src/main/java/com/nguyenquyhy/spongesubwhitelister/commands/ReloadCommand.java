package com.nguyenquyhy.spongesubwhitelister.commands;

import com.nguyenquyhy.spongesubwhitelister.util.WhitelistManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

/**
 * Created by Hy on 5/28/2016.
 */
public class ReloadCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        WhitelistManager.updateRemoteWhitelists();
        src.sendMessage(Text.of("Remote whitelist reloaded."));
        return CommandResult.success();
    }
}
