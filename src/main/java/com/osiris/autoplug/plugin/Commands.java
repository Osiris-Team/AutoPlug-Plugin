/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.osiris.autoplug.plugin.AutoPlugCommandsGUI.INVENTORY;

public class Commands {

    // To avoid:
    // "Caused by: java.lang.ClassCastException: class com.destroystokyo.paper.console.TerminalConsoleCommandSender cannot be cast
    // to class org.bukkit.entity.Player (com.destroystokyo.paper.console.TerminalConsoleCommandSender and org.bukkit.entity.Player
    // are in unnamed module of loader 'app')"
    // we do not cast CommandSender to Player.

    // In the case below casting somehow works, idk why.
    public static void openGUI(Player player) {
        INVENTORY.open(player);
    }

    public static void restart(Player player) {
        Constants.sendMsg(player,"Restarting the server...");
        AutoPlugClientConnection.send(".restart");
    }
    public static void restart(CommandSender player) {
        Constants.sendMsg(player,"Restarting the server...");
        AutoPlugClientConnection.send(".restart");
    }

    public static void stop(CommandSender player) {
        Constants.sendMsg(player,"Stopping the server...");
        AutoPlugClientConnection.send(".stop");
    }
    public static void stop(Player player) {
        Constants.sendMsg(player,"Stopping the server...");
        AutoPlugClientConnection.send(".stop");
    }

    public static void stopBoth(Player player) {
        Constants.sendMsg(player, "Stopping the server and the AutoPlug-Client...");
        AutoPlugClientConnection.send(".stop both");
    }
    public static void stopBoth(CommandSender player) {
        Constants.sendMsg(player, "Stopping the server and the AutoPlug-Client...");
        AutoPlugClientConnection.send(".stop both");
    }
}
