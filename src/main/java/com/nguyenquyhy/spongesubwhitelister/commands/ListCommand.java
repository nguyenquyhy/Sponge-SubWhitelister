package com.nguyenquyhy.spongesubwhitelister.commands;

import com.nguyenquyhy.spongesubwhitelister.util.WhitelistManager;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

/**
 * Created by Hy on 5/28/2016.
 */
public class ListCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String mess = "Users: ";
        mess += StringUtils.join(WhitelistManager.getUsers(), ", ");

        src.sendMessage(Text.of(mess));
        return CommandResult.success();
    }
}
