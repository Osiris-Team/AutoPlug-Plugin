/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import com.osiris.autoplug.plugin.tasks.TaskHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends JavaPlugin {

    private static final Logger LOG = Logger.getLogger("AutoPlug");
    private static Main instance;
    private String version;
    private boolean debug;


    @Override
    public void onEnable() {
        Main.instance = this;

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
        LOG.info(" - Initialising AutoPlugPlugin");
        LOG.info(" - Official registration page: " + GLOBALDATA.OFFICIAL_WEBSITE);

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
                LOG.log(Level.SEVERE, " [!] Error creating configuration file [!]", ex);
                return;
            }
        } else {

            //LOG.info(" - Loading configuration... ");
            // Load configuration.
            cfg = YamlConfiguration.loadConfiguration(config);
        }


        // Initialize settings.
        String server_key = cfg.getString("server-key", "819109234");
        debug = cfg.getBoolean("debug", false);
        boolean async = cfg.getBoolean("async-check", false);

        if (debug) { LOG.info(" - DEBUG mode enabled!"); }

        LOG.info(" - AutoPlug initialised!");
        LOG.info("|--------------[CONNECTING]--------------|");
        init_plugin_check(server_key, async);
    }

    @Override
    public void onDisable() {
        LOG.info(" - Disabling AutoPlug...");
        LOG.info(" - AutoPlug disabled.");
    }

    private void init_plugin_check(String server_key, boolean async) {
        TaskHandler taskHandler = new TaskHandler(""+server_key, "exec_update", async);
        taskHandler.execute();
    }

}
