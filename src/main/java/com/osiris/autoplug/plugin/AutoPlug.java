/*
 *  Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import com.osiris.autoplug.plugin.tasks.TaskHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;


public class AutoPlug extends JavaPlugin {

    private static final Logger LOG = Logger.getLogger("AutoPlug");
    private static AutoPlug instance;
    private String version;
    private boolean debug;


    @Override
    public void onEnable() {
        AutoPlug.instance = this;

        // Set the plugin version.
        version = getDescription().getVersion();


        LOG.info("     ___       __       ___  __             ");
        LOG.info("    / _ |__ __/ /____  / _ \\/ /_ _____ _   ");
        LOG.info("   / __ / // / __/ _ \\/ ___/ / // / _ `/   ");
        LOG.info("  /_/ |_\\_,_/\\__/\\___/_/  /_/\\_,_/\\_, /");
        LOG.info("                                 /___/    ");
        LOG.info("|----------------------------------------|");
        LOG.info("                   v."+version+"          ");
        LOG.info("      Copyright (c) 2020 Osiris Team      ");
        LOG.info("                                          ");
        LOG.info(" - Initialising AutoPlug on "+Bukkit.getServer().getName()+ " with Motd: "+ Bukkit.getServer().getMotd());
        LOG.info(" - Official registration page: " + GLOBALDATA.OFFICIAL_WEBSITE);
        LOG.warning(" - Note* On our website you will have access to ALL of your plugins, so NEVER share your server_keys!");
        LOG.warning(" - If you think your server_keys got leaked, change them immediately!");

        if (!getDataFolder().exists()) {
            // First time run - do some initialization.
            LOG.warning(" ");
            LOG.warning(" - Configuring AutoPlug for the first time...");
            LOG.warning(" - Thank you for installing our Plugin <3");
            LOG.warning(" - Remember! You are not done by just downloading this and starting it up!");
            LOG.warning(" - You need to sign-in yourself on our website and register your server there first.");
            LOG.warning(" - After completing this step you will get a server_key. Insert it into the config.yml and restart the server.");
            LOG.warning(" - Easy right? For registration and help visit our website at:"+ GLOBALDATA.OFFICIAL_WEBSITE);
            getDataFolder().mkdir();
        }
        File config = new File(getDataFolder() + "/config.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);


        /*
         * Create configuration file if it does not exists; otherwise, load it
         */
        if (!config.exists()) {
            try {

                // Initialize the configuration file.
                LOG.info(" - No config found! Creating new config with placeholder data...");
                config.createNewFile();

                cfg.set("server-key", "819109234");
                cfg.set("async-check", false);
                cfg.set("debug", false);

                cfg.save(config);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, " - Error creating configuration file [!]", ex);
                return;
            }
        } else {

            LOG.info(" - Loading configuration... ");
            // Load configuration.
            cfg = YamlConfiguration.loadConfiguration(config);
        }


        // Initialize settings.
        String server_key = cfg.getString("server-key", "819109234");
        debug = cfg.getBoolean("debug", false);
        boolean async = cfg.getBoolean("async-check", false);

        if (debug) { LOG.info(" - DEBUG mode enabled!"); }

        LOG.info(" - AutoPlug enabled successfully!");
        LOG.info("|----------------------------------------|");
        TaskHandler taskHandler = new TaskHandler(""+server_key, "exec_update", async);
        taskHandler.execute();
    }

    @Override
    public void onDisable() {
        LOG.info(" - Disabling AutoPlug...");
        LOG.info(" - AutoPlug disabled.");
    }


    public static AutoPlug getInstance() {
        return instance;
    }


    public String getVersion() {
        return version;
    }


    public boolean isDebug() {
        return debug;
    }

}
