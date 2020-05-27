/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Communicates with the local-client and passes over the plugin information
 */
public class Communication {

    private static final Logger LOG = Logger.getLogger("AutoPlugPlugin");

    private Socket local_socket;
    private DataInputStream local_dis;
    private DataOutputStream local_dos;

    public Communication(){
        connect();
    }

    public void connect(){

        try {
            LOG.info("Connecting to local AutoPlug.jar...");
            local_socket = new Socket("localhost", 35556);
            local_dis = new DataInputStream(local_socket.getInputStream());
            local_dos = new DataOutputStream(local_socket.getOutputStream());
            LOG.info("Connection successful! ");

        } catch (Exception e) {
            e.printStackTrace();
            LOG.severe(" [!] ERROR: " + e.getMessage());
            LOG.severe(" [!] Couldn't establish connection to local AutoPlug.jar [!]");
            LOG.severe(" [!] Please make sure you downloaded the AutoPlug.jar and placed it into the main directory (where your server.jar is located) [!]");
            LOG.severe(" [!] You must change your startup script, so that it starts the AutoPlug.jar. It will handle the server starts/restarts [!]");
        }


    }

    public int sendPlugins(){


        //Create lists
        List<String> pl_names = Plugins.getNames();
        List<String> pl_authors = Plugins.getAuthors();
        List<String> pl_versions = Plugins.getVersions();

        try{
            int original_size = pl_names.size();
            LOG.info("Scanning "+original_size+" plugins...");

            for (int i = 0; i < pl_names.size(); i++) {

                //Comparing each plugin name with excluded plugins list
                List<String> excluded_plugins = Config.excluded_plugins;
                for (int k = 0; k < excluded_plugins.size(); k++) {

                    if (pl_names.get(i).equals(excluded_plugins.get(k))){
                        LOG.warning("Plugin [" + pl_names.get(i) + "] is in excluded plugins list and will be skipped!");
                        pl_names.remove(i);
                        pl_authors.remove(i);
                        pl_versions.remove(i);
                    }
                }

            }

            //Check for missing information
            for (int q = 0; q < pl_names.size(); q++) {

                if (pl_names.get(q).equals("no_data") || pl_authors.get(q).equals("no_data") || pl_versions.get(q).equals("no_data")){
                    LOG.warning("Plugin [" + pl_names.get(q) + "] is missing critical information in its plugin.yml so it will be skipped!");
                    pl_names.remove(q);
                    pl_authors.remove(q);
                    pl_versions.remove(q);
                }

            }

            int new_size = pl_names.size();
            int skipped_pl = original_size - new_size;
            LOG.info("Scan complete! Skipping " + skipped_pl+"/"+original_size+" plugins!");

            LOG.info("Passing over "+new_size+" plugins...");
            local_dos.writeInt(new_size);

            for (int i = 0; i < new_size; i++) {

                local_dos.writeUTF(pl_names.get(i));
                local_dos.writeUTF(pl_authors.get(i));
                local_dos.writeUTF(pl_versions.get(i));

            }
            LOG.info("Success!");

            //If returned int is 1 -> restart, if 0 -> let server alive
            return local_dis.readInt();

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }


    }


}
