package com.nguyenquyhy.spongesubwhitelister;

import com.google.inject.Inject;
import com.nguyenquyhy.spongesubwhitelister.commands.*;
import com.nguyenquyhy.spongesubwhitelister.util.WhitelistManager;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Hy on 5/27/2016.
 */
@Plugin(id = "com.nguyenquyhy.spongesubwhitelister", name = "Sponge SubWhitelister")
public class SpongeSubWhitelister {
    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    private static ConfigurationNode config;
    private static ConfigurationLoader configLoader;

    @Inject
    private Game game;

    private static SpongeSubWhitelister instance;

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
        instance = this;
        loadConfiguration();

                    WhitelistManager.initialize();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        CommandSpec listCmd = CommandSpec.builder()
                .permission("spongesubwhitelister.list")
                .description(Text.of("List whitelisted users"))
                .executor(new ListCommand())
                .build();

        CommandSpec reloadCmd = CommandSpec.builder()
                .permission("spongesubwhitelister.reload")
                .description(Text.of("Reloads remote whitelist"))
                .executor(new ReloadCommand())
                .build();

        CommandSpec toggleCmd = CommandSpec.builder()
                .permission("spongesubwhitelister.toggle")
                .description(Text.of("Toggles remote whitelist"))
                .executor(new ToggleCommand())
                .build();

        CommandSpec exportCmd = CommandSpec.builder()
                .permission("spongesubwhitelister.export")
                .description(Text.of("Reload Sponge Discord configuration"))
                .executor(new ExportCommand())
                .build();

        CommandSpec mainCommandSpec = CommandSpec.builder()
                .description(Text.of("Discord in Minecraft"))
                .child(listCmd, "list", "l")
                .child(reloadCmd, "reload", "r")
                .child(toggleCmd, "toggle", "t")
                .child(exportCmd, "export", "e")
                .build();

        game.getCommandManager().register(this, mainCommandSpec, "whitelist");

        getLogger().info("/whitelist command registered.");
    }

    @Listener
    public void onAuth(ClientConnectionEvent.Auth event) {
        if (!config.getNode("enabled").getBoolean()) return;

        GameProfile profile = event.getProfile();
        // TODO: exempt permission
//        Optional<Player> player = game.getServer().getPlayer(profile.getUniqueId());
//        if (player.isPresent() && player.get().hasPermission("spongesubwhitelister.exempt"))
//            return;
        if (!WhitelistManager.getUsers().contains(profile.getName().get().toLowerCase())) {
            event.setCancelled(true);
        }
    }

    public static SpongeSubWhitelister getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public static ConfigurationNode getConfig() {
        return config;
    }

    public Path getConfigDir() {
        return configDir;
    }

    public static ConfigurationLoader getConfigLoader() {
        return configLoader;
    }

    public void loadConfiguration() {
        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            Path configFile = Paths.get(configDir + "/config.conf");
            configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();

            if (!Files.exists(configFile)) {
                Files.createFile(configFile);
            }
            config = configLoader.load();
            if (config.getNode("enabled").getValue() == null)
                config.getNode("enabled").setValue(true);
            if (config.getNode("delay").getValue() == null)
                config.getNode("delay").setValue(60);
            if (config.getNode("urls").getValue() == null)
                config.getNode("urls").setValue(new ArrayList<>());
            configLoader.save(config);
            getLogger().info("Configuration is loaded.");
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().error("Couldn't create default configuration file!");
        }
    }
}
