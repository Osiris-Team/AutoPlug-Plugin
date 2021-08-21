/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import org.bukkit.entity.Player;

import static com.osiris.autoplug.plugin.AutoPlugCommandsGUI.INVENTORY;

public class Commands {

    public static void openGUI(Player player) {
        INVENTORY.open(player);
    }

    public static void restart() {
        AutoPlugClientConnection.send(".restart");
    }

    public static void stop() {
        AutoPlugClientConnection.send(".stop");
    }

    public static void stopBoth() {
        AutoPlugClientConnection.send(".stop both");
    }
}
