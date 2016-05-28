package com.nguyenquyhy.spongesubwhitelister.util;

import com.google.gson.reflect.TypeToken;
import com.nguyenquyhy.spongesubwhitelister.SpongeSubWhitelister;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hy on 5/28/2016.
 */
public class WhitelistManager {
    private static final HashSet<String> users = new HashSet<String>();

    public static void initialize() {
        Scheduler scheduler = Sponge.getScheduler();
        Task.Builder taskBuilder = scheduler.createTaskBuilder();
        taskBuilder
                .interval(SpongeSubWhitelister.getConfig().getNode("delay").getInt(), TimeUnit.SECONDS)
                .execute(() -> {
                    updateRemoteWhitelists();
                })
                .submit(SpongeSubWhitelister.getInstance());
    }

    public static HashSet<String> getUsers() {
        return users;
    }

    public static void updateRemoteWhitelists() {
        for (String s : SpongeSubWhitelister.getConfig().getNode("urls").getList(t -> t.toString())) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(new URL("http://whitelist.twitchapps.com/list.php?id=" + s).openStream()));
                String l;

                while ((l = in.readLine()) != null) {
                    users.add(l.trim().toLowerCase());
                }
                SpongeSubWhitelister.getInstance().getLogger().debug("Whitelist of " + s + " is updated!");
            } catch (IOException e) {
                SpongeSubWhitelister.getInstance().getLogger().error("The website could be down. We'll keep trying.");
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
