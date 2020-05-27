/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Config {

    private static final Logger LOG = Logger.getLogger("AutoPlugPlugin");
    private Startup instance;

    //Call this one at the plugin startup
    public Config(Startup instance){
        this.instance = instance;
        initConfig();
    }

    //Call this one whenever you need a config value
    public Config(){
    }

    //Config user values
    public static List<String> excluded_plugins;

    private void initConfig(){
        LOG.info(" - Loading config.yml");

        if (!instance.getDataFolder().exists()) {
            // First time run
            LOG.info(" ");
            LOG.info(" - Configuring AutoPlug for the first time...");
            LOG.info(" - Thank you for installing our Plugin <3");
            LOG.info(" - Remember! You are not done by just downloading this and starting it up!");
            LOG.info(" - You need to sign-in yourself on our website and register your server there first.");
            LOG.info(" - After completing this step you will get a server_key. Insert it into the config.yml and restart the server.");
            LOG.info(" - Easy right? For registration and help visit our website at: https://autoplug.ddns.net");
            LOG.info(" ");
            instance.getDataFolder().mkdir();
        }
        File config = new File(instance.getDataFolder() + "/config.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);

        List<String> listOfStrings = Arrays.asList("AutoPlugPlugin", "AnotherPlugin");
        if (!config.exists()) {
            try {
                // Create config
                LOG.info(" - No config found! Creating new config with placeholder data...");
                config.createNewFile();

                cfg.set("excluded-plugins", listOfStrings);

                cfg.save(config);
            } catch (Exception ex) {
                ex.printStackTrace();
                LOG.severe(" [!] Error creating configuration file [!]");
            }
        } else {
            // Load configuration.
            cfg = YamlConfiguration.loadConfiguration(config);
        }

        excluded_plugins = cfg.getStringList("excluded-plugins");

        LOG.info(" - Config loaded!");
    }

}
