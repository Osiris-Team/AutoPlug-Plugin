/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin.tasks;

import com.osiris.autoplug.plugin.GLOBALDATA;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getPluginManager;


public class UpdatePlugins {

    private static final Logger LOG = Logger.getLogger("AutoPlug");

    private String server_key;
    private String host = "144.91.78.158"; // "http://vmd51572.contaboserver.net";

    private Socket client;
    private DataInputStream dis;
    private DataOutputStream dos;

    private Socket local_socket;
    private DataInputStream local_dis;
    private DataOutputStream local_dos;

    private String output_name;
    private String output_author;
    private String output_version;

    private int UpdateCounter = 0;


    public UpdatePlugins(String server_key) {
        this.server_key = server_key;
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws Exception {

        //Connecting to online server...
        try {

            client = new Socket(host,35555);
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());

        } catch (Exception ex) {
            LOG.log(Level.SEVERE,
                    " [!] Error connecting to the Online-Server at "+ GLOBALDATA.OFFICIAL_WEBSITE+" [!]");
            LOG.log(Level.SEVERE,
                    " [!] In most cases we are just performing updates and the website is up again after 2min [!]");
            LOG.log(Level.SEVERE,
                    " [!] So please wait 2min and try again. If you still get this error, please notify our Team [!]", ex);
        }

        //Connecting to AutoPlug.jar in main dir...
        try {

            local_socket = new Socket("localhost", 35556);
            local_dis = new DataInputStream(local_socket.getInputStream());
            local_dos = new DataOutputStream(local_socket.getOutputStream());

            String local_connection_result = local_dis.readUTF();
            if (local_connection_result.equals("true")) {
                LOG.info(" - Connection to local AutoPlug.jar successful! ");

            } else {
                LOG.severe(" [!] Couldn't establish connection to local AutoPlug.jar [!]");
                LOG.severe(" [!] Unknown cause [!]");
            }

        } catch (IOException e) {
            LOG.severe(" [!] ERROR: " + e.getMessage());
            LOG.severe(" [!] Couldn't establish connection to local AutoPlug.jar [!]");
            LOG.severe(" [!] Please make sure you downloaded the AutoPlug.jar and placed it into the main directory (where your server.jar is located) [!]");
            LOG.severe(" [!] You must change your startup script, so that it starts the AutoPlug.jar. It will handle the server starts/restarts [!]");
            e.printStackTrace();
        }
    }

    public boolean start(){
        try {
            //SEND 0: Send Server-Key for Auth
            LOG.info(" - Connecting to Online-Server with given server_key...");
            dos.writeUTF(server_key);

            //Sending request string
            LOG.info(" - Sending request exec_update...");
            dos.writeUTF("exec_update");


            //RECEIVE 3: get response
            if (dis.readUTF().equals("true")) {
                LOG.info(" - Connection successful! We are ready to go!");

                int size = getPluginManager().getPlugins().length;

                LOG.info(" - Checking "+size+" plugins...");

                //SEND 4: send the size of for-loop
                dos.writeInt(size);

                //SEND 4: send the size of for-loop to local autoplug.jar
                local_dos.writeInt(size);

                //RECEIVE 7: ready to start for loop?

                if (dis.readUTF().equals("true")) {

                    Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
                    for (int i = 0; i < size; i++) {

                        //At start of the loop reset Data for new Plugin Data!
                        output_name=null;
                        output_author=null;
                        output_version=null;

                        if (!plugins[i].getName().isEmpty()) {
                            LOG.info("|----------------------------------------|");
                            LOG.info(" ");
                            output_name= plugins[i].getName();
                            //SEND 8: Send Plugin Name
                            dos.writeUTF(output_name);
                            LOG.info(" - Checking [" + output_name +"]["+i+"/"+size+"] for updates...");
                        } else{
                            LOG.severe(" - Checking [ERROR: NO PLUGIN NAME FOUND IN PLUGIN.YML!]["+i+"/"+size+"] for updates...");
                            output_name = "no_data";
                            dos.writeUTF(output_name);
                        }

                        if(!plugins[i].getDescription().getAuthors().isEmpty()) {
                            output_author = plugins[i].getDescription().getAuthors().get(0);
                            //SEND 9: Send Plugin Author
                            dos.writeUTF(output_author);
                            LOG.info(" - Author: " + output_author);

                        } else {
                            LOG.severe(" - Author: [ERROR: NO AUTHOR FOUND IN PLUGIN.YML!]");
                            output_author = "no_data";
                            dos.writeUTF(output_author);
                        }

                        if (!plugins[i].getDescription().getVersion().isEmpty()) {
                            output_version= plugins[i].getDescription().getVersion();
                            //SEND 10: Send Plugin Version
                            dos.writeUTF(output_version);
                            LOG.info(" - Version: " + output_version);
                        } else{
                            LOG.severe(" - Version: [ERROR: NO PLUGIN VERSION FOUND IN PLUGIN.YML!]");
                            output_version = "no_data";
                            dos.writeUTF(output_version);


                        }

                        //RECEIVE 15: get final download-link
                        String final_resultString = dis.readUTF();

                        if (final_resultString.equals("query_returns_array_no_update")) {
                            LOG.info(" - Result: Already on latest version!("+output_version+")");
                            LOG.info(" ");

                            //RECEIVE 16: receive latest version
                            String latest_version = dis.readUTF();

                            //SEND local autoplug.jar, that this plugin doesn't needs further actions
                            local_dos.writeUTF("false");

                            //SEND 17: send final reponse
                            dos.writeUTF("true");
                        }
                        else if (final_resultString.equals("query_returns_array_no_author")) {
                            LOG.info(" - Result: Couldn't find this Author on Spigot!");
                            LOG.info(" - Info: The plugin name/author shouldn't diverge too much from it's spigot-thread. ");
                            LOG.info(" ");

                            //RECEIVE 16: receive latest version
                            String latest_version = dis.readUTF();

                            //SEND local autoplug.jar, that this plugin doesn't needs further actions
                            local_dos.writeUTF("false");

                            //SEND 17: send final reponse
                            dos.writeUTF("true");
                        }
                        else if(final_resultString.equals("query_returns_object")){
                            LOG.warning(" - Result: Returned Json-Object and not array?!");
                            LOG.warning(" - Info: VERY rare error! Please notify us!");
                            LOG.info(" ");

                            //RECEIVE 16: receive latest version
                            String latest_version = dis.readUTF();

                            //SEND local autoplug.jar, that this plugin doesn't needs further actions
                            local_dos.writeUTF("false");

                            //SEND 17: send final reponse
                            dos.writeUTF("true");
                        }
                        else if(final_resultString.equals("query_no_result")) {
                            LOG.warning(" - Result: Author was found, but not the plugin!");
                            LOG.warning(" - Info: The plugin name/author shouldn't diverge too much from it's spigot-thread. ");
                            LOG.info(" ");

                            //RECEIVE 16: receive latest version
                            String latest_version = dis.readUTF();

                            //SEND local autoplug.jar, that this plugin doesn't needs further actions
                            local_dos.writeUTF("false");

                            //SEND 17: send final reponse
                            dos.writeUTF("true");
                        }
                        else if(final_resultString.equals("query_error")) {
                            LOG.severe(" - Result: Server error! No result!");
                            LOG.severe(" - Info: Abnormal result pls notify us -> https://discord.gg/GGNmtCC ");
                            LOG.severe(" ");

                            //RECEIVE 16: receive latest version
                            String latest_version = dis.readUTF();

                            //SEND local autoplug.jar, that this plugin doesn't needs further actions
                            local_dos.writeUTF("false");

                            //SEND 17: send final reponse
                            dos.writeUTF("false");
                        }
                        else {

                            //RECEIVE 16: receive latest version
                            String latest_version = dis.readUTF();

                            String directory_plugins = System.getProperty("user.dir") + "/plugins";

                            LOG.info(" - Result: Update available! ["+output_version+"] -> ["+latest_version+"]");
                            LOG.info(" - Info: Downloading from Spigot: " + final_resultString);
                            //Download new version and write to download cache
                            DownloadUpdate downloadUpdate = new DownloadUpdate();
                            String cached_plugin_path = downloadUpdate.downloadJar(final_resultString, output_name, latest_version);

                            //If succes then:
                            if (!cached_plugin_path.equals("failed")){

                                //SEND local autoplug.jar, that this plugin needs update
                                local_dos.writeUTF("true");

                                LOG.info(" - Info: Downloaded update to cache successfully!");
                                LOG.info(" ");

                                String new_plugin_path =  directory_plugins + "/"+output_name+"["+latest_version+"]-AUTOPLUGGED-LATEST.jar";

                                //SEND: Path of files in cache
                                local_dos.writeUTF(cached_plugin_path);
                                //SEND: Path of files copy origin
                                local_dos.writeUTF(new_plugin_path);
                                //SEND: Plugin Name
                                local_dos.writeUTF(output_name);


                                //SEND 17: send final reponse
                                dos.writeUTF("true");

                                UpdateCounter++;

                            }else{

                                //SEND local autoplug.jar, that this plugin doesn't need further changes
                                local_dos.writeUTF("false");

                                LOG.severe(" [!] Failed to download jar to cache [!]");
                                LOG.info(" ");

                                //SEND 17: send final reponse
                                dos.writeUTF("false");
                            }

                        }
                    }

                    LOG.info("|----------------------------------------|");
                    LOG.info("     ___       __       ___  __");
                    LOG.info("    / _ |__ __/ /____  / _ \\/ /_ _____ _");
                    LOG.info("   / __ / // / __/ _ \\/ ___/ / // / _ `/");
                    LOG.info("  /_/ |_\\_,_/\\__/\\___/_/  /_/\\_,_/\\_, /");
                    LOG.info("                                 /___/");
                    LOG.info("                _/Result\\_               ");
                    LOG.info("|----------------------------------------|");
                    LOG.info("|Plugins checked total -> "+ size);
                    LOG.info("|Plugins to update total -> " + UpdateCounter);

                    if (UpdateCounter>0){
                        local_dos.writeUTF("true");

                        String profile = local_dis.readUTF();
                        if (profile.equals("MANUAL")) {
                            LOG.info("|[MANUAL] Plugins downloaded, please update them by yourself...");
                        }
                        else if(profile.equals("AUTOMATIC")){
                            LOG.warning("|[AUTOMATIC] Restarting server and enabling updated plugins...");
                            Bukkit.getServer().shutdown();
                        }
                        else{
                            LOG.warning("|Profile error! Not matching to MANUAL or AUTOMATIC! -> " + profile);
                        }

                    } else{
                        local_dos.writeUTF("false");
                        LOG.info("|All plugins are up-to-date!");
                    }
                    LOG.info("|----------------------------------------|");
                    LOG.info(" ");
                }

            }
            else {
                LOG.severe(" [!] Authentication failed! Please check your config and be sure, that your server_key matches the key on our website [!] " + GLOBALDATA.OFFICIAL_WEBSITE);
                client.close();
                local_socket.close();
            }

            //Close connections
            client.close();
            dis.close();
            dos.close();
            local_socket.close();
            local_dis.close();
            local_dos.close();
            return true;

        } catch (SocketException ex) {
            LOG.severe(" [!] Authentication failed! Please check your config and be sure, that your server_key matches the key on our website [!] " + GLOBALDATA.OFFICIAL_WEBSITE);
            return false;
        } catch (Exception ex) {
            LOG.severe(" [!] Authentication failed! Please check your config and be sure, that your server_key matches the key on our website [!] " + GLOBALDATA.OFFICIAL_WEBSITE);
            try {
                //Close connections
                if (!client.isClosed()){
                    client.close();
                    dis.close();
                    dos.close();
                }

                if (!local_socket.isClosed()){
                    local_socket.close();
                    local_dis.close();
                    local_dos.close();
                }
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
