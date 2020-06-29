/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import org.simpleyaml.configuration.file.YamlFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Communicates with the local-client and passes over the plugin information
 */
public class Communication {

    private static final Logger LOG = Logger.getLogger("AutoPlugPlugin");

    private static Socket local_socket;
    private static DataInputStream local_dis;
    private static DataOutputStream local_dos;

    public Communication(){
        getServerKey();
        findAutoPlugClient();
    }

    public static String server_key;

    private YamlFile config = new YamlFile("autoplug-server-config.yml");

    /**
     * Get the server key from /autoplug-server-config.yml.
     * Remember the working directory of every plugin is at / .
     */
    private void getServerKey(){

        YamlFile config = new YamlFile("autoplug-server-config.yml");

        // Load the YAML file
        try {
            if (!config.exists()) {
                LOG.severe(" - autoplug-server-config.yml not found!");
            }
            else {
                LOG.info(" - Loading autoplug-server-config.yml...");
            }
            config.load(); // Loads the entire file

        } catch (Exception e) {
            e.printStackTrace();
        }

        server_key = config.getString("autoplug-server-config.server.key");

    }

    /**
     * If there are multiple servers running(Bungeecord), we need to differentiate between them
     * so each plugin list is delivered to the right AutoPlug-Client.
     * To do this, we iterate through all ports starting at 35565, and send them the server key.
     * If there is no server key in response or that key doesn't match, we move on.
     * Max port is 65535.
     */
    private void findAutoPlugClient(){

        LOG.info("Searching AutoPlug-Client on local network...");
        int i = 35565;
        boolean match = false;
        while(!match){

            try {
                LOG.info("Connecting to port "+i+"...");
                local_socket = new Socket("localhost", i);
                local_dis = new DataInputStream(local_socket.getInputStream());
                local_dos = new DataOutputStream(local_socket.getOutputStream());
                LOG.info("Connection successful!");
                LOG.info("Comparing server-keys");

                //Send server key to AutoPlug-Client on that port and let it be compared
                local_dos.writeUTF(server_key);

                //Get comparison result
                if (local_dis.readUTF().equals("true")){
                    LOG.info("Server-keys match!");
                    match = true;
                }
                else{
                    LOG.info("Server-keys not matching!");
                    match = false;
                    i++;
                }

            } catch (Exception e) {
                LOG.severe(" [!] ERROR: " + e.getMessage());
                match = false;
                i++;
            }

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
