/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public final class Startup extends JavaPlugin {

    private static final Logger LOG = Logger.getLogger("AutoPlugPlugin");

    @Override
    public void onEnable() {

        LOG.info(" ");
        LOG.info(" - Initialising AutoPlugPlugin");
        LOG.info(" - Official registration page: https://autoplug.ddns.net");
        LOG.info(" ");
        new Config(this);
        LOG.info(" ");
        Communication communication = new Communication();
        if (communication.sendPlugins()==1){
            //Stop the server to update the plugins
            //onDisable method will notify the client that the server is closed
            Bukkit.getServer().shutdown();
        } else{
            //Do nothing and let the server run
        }
        LOG.info(" ");


    }

    @Override
    public void onDisable() {

    }


}
