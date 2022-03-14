/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import fr.minuskube.inv.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static com.osiris.autoplug.plugin.Constants.*;

public final class Startup extends JavaPlugin {

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        INV_MAN = new InventoryManager(this);
        INV_MAN.init();

        // Check if AutoPlug-Client is installed
        try {
            File autoPlugClientJar = new FileManager().autoplugJar(new File(System.getProperty("user.dir"))); // May throw an exception
            if (autoPlugClientJar == null)
                throw new Exception("To use this plugin, AutoPlug-Client must be installed." +
                        " No AutoPlug-Client installation found in '" + System.getProperty("user.dir") + "'." +
                        " To install the AutoPlug-Client, follow the installation instructions at: https://autoplug.online/installation");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try{
            CON = new AutoPlugClientConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Register commands:
        this.getCommand(".").setExecutor((sender, command, label, args) -> {
            Commands.openGUI((Player) sender);
            return true;
        });

        this.getCommand(".restart").setExecutor((sender, command, label, args) -> {
            Commands.restart(sender);
            return true;
        });
        this.getCommand(".r").setExecutor((sender, command, label, args) -> {
            Commands.restart(sender);
            return true;
        });

        this.getCommand(".stop").setExecutor((sender, command, label, args) -> {
            Commands.stop(sender);
            return true;
        });
        this.getCommand(".st").setExecutor((sender, command, label, args) -> {
            Commands.stop(sender);
            return true;
        });

        this.getCommand(".stop both").setExecutor((sender, command, label, args) -> {
            Commands.stopBoth(sender);
            return true;
        });
        this.getCommand(".stb").setExecutor((sender, command, label, args) -> {
            Commands.stopBoth(sender);
            return true;
        });
        LOG.info("AutoPlug-Plugin enabled successfully.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        try {
            if (CON != null) CON.getSocket().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LOG.info("Disabled AutoPlug-Plugin successfully.");
    }

}
