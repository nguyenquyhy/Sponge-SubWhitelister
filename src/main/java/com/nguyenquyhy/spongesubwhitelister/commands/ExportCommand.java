package com.nguyenquyhy.spongesubwhitelister.commands;

import com.nguyenquyhy.spongesubwhitelister.SpongeSubWhitelister;
import com.nguyenquyhy.spongesubwhitelister.util.WhitelistManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Hy on 5/28/2016.
 */
public class ExportCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        File f = new File(SpongeSubWhitelister.getInstance().getConfigDir().toString(), "users.txt");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
            for (String s : WhitelistManager.getUsers()) {
                bw.write(s);
            }

            bw.close();
            src.sendMessage(Text.of("Remote whitelist written to file."));

            return CommandResult.success();
        } catch (IOException e) {
            e.printStackTrace();
            return CommandResult.empty();
        }
    }
}
