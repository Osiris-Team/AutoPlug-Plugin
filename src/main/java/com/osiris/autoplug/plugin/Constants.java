/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import fr.minuskube.inv.InventoryManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Constants {

    public static final Logger LOG = Logger.getLogger("AutoPlug-Plugin");
    public static AutoPlugClientConnection CON = null; // Gets set in Startup.class
    public static InventoryManager INV_MAN = null;

    public static void sendMsg(Player player, String msg){
        player.sendMessage(msg);
    }
    public static void sendMsg(CommandSender sender, String msg){
        sender.sendMessage(msg);
    }

}
