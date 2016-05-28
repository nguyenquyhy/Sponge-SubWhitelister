package com.nguyenquyhy.spongesubwhitelister.commands;

import com.nguyenquyhy.spongesubwhitelister.SpongeSubWhitelister;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.config.ConfigManager;
import org.spongepowered.api.text.Text;

import java.io.IOException;

/**
 * Created by Hy on 5/28/2016.
 */
public class ToggleCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ConfigurationNode config = SpongeSubWhitelister.getConfig();
        boolean b = !config.getNode("enabled").getBoolean();

        src.sendMessage(Text.of("Remote whitelist " + (b ? "enabled!" : "disabled!")));

        config.getNode("enabled").setValue(b);
        try {
            SpongeSubWhitelister.getConfigLoader().save(config);
            return CommandResult.success();
        } catch (IOException e) {
            SpongeSubWhitelister.getInstance().getLogger().error("Cannot save config!", e);
            e.printStackTrace();
            return CommandResult.empty();
        }
    }
}
