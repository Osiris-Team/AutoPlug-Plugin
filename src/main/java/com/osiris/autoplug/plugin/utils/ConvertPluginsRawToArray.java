/*
 *  Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ConvertPluginsRawToArray {


    private String[] names;
    private String[] authors;
    private String[] versions;

    public ConvertPluginsRawToArray() {

        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        int size = Bukkit.getPluginManager().getPlugins().length;

        names = new String[size];
        authors = new String[size];
        versions = new String[size];

        for (int i = 0; i < size; i++) {
            names[i]= plugins[i].getName();
            authors[i]= plugins[i].getDescription().getAuthors().get(0);
            versions[i]= plugins[i].getDescription().getVersion();

        }
    }



    public String[] getNames() {
        return names;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String[] getVersions() {
        return versions;
    }


}
