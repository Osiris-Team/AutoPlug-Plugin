/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getPluginManager;

/**
 * Gets all plugin relevant information
 */
public class Plugins {

    private static final Logger LOG = Logger.getLogger("AutoPlugPlugin");

    private static PluginManager manager = getPluginManager();
    private static Plugin[] plugins = manager.getPlugins();
    private static int amount = plugins.length;

    public static List<String> getNames(){

        List<String> result = new ArrayList<>();
        for (int i = 0; i < amount; i++) {

            if (plugins[i].getName().isEmpty()){
                result.add("no_data");
            }
            else{
                result.add(plugins[i].getName());
            }
        }
        return result;

    }

    public static List<String> getAuthors(){

        List<String> result = new ArrayList<>();
        for (int i = 0; i < amount; i++) {

            if (plugins[i].getDescription().getAuthors().isEmpty()){
                result.add("no_data");
            }
            else{
                result.add(plugins[i].getDescription().getAuthors().get(0));
            }

        }
        return result;

    }

    public static List<String> getVersions(){

        List<String> result = new ArrayList<>();
        for (int i = 0; i < amount; i++) {

            if (plugins[i].getDescription().getVersion().isEmpty()){
                result.add("no_data");
            }
            else{
                result.add(plugins[i].getDescription().getVersion());
            }
        }
        return result;

    }


}
