/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import fr.minuskube.inv.InventoryManager;

import java.util.logging.Logger;

public class Constants {

    public static final Logger LOG = Logger.getLogger("AutoPlug-Plugin");
    public static AutoPlugClientConnection CON = null; // Gets set in Startup.class
    public static InventoryManager INV_MAN = null;

}
