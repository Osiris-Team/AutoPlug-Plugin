/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import com.osiris.dyml.DreamYaml;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static com.osiris.autoplug.plugin.Constants.CON;
import static com.osiris.autoplug.plugin.Constants.LOG;

/**
 * Communicates with the local-client and passes over the plugin information
 */
public class AutoPlugClientConnection {
    private static BufferedWriter bw;
    private String serverKey;
    private Socket socket;

    public AutoPlugClientConnection() {
        getServerKey();
        findAutoPlugClient();
    }

    public static synchronized void send(@NotNull String message) {
        try {
            message = CON.serverKey + " " + message;
            if (bw != null) {
                if (!message.contains(System.lineSeparator())) {
                    bw.write(message + System.lineSeparator());
                } else {
                    bw.write(message);
                }
                bw.flush();
            }
        } catch (Exception e) { // Do not use AL.warn because that would cause an infinite loop
        }
    }

    private void getServerKey() {
        DreamYaml config = new DreamYaml(System.getProperty("user.dir") + "/autoplug/general-config.yml");
        if (!config.getFile().exists())
            throw new RuntimeException("File '" + config.getFile() + "' is missing! Please make sure that the AutoPlug-Client was installed and setup correctly.");

        try {
            config.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        serverKey = config.get("general-config", "server", "key").asString();
    }

    /**
     * If there are multiple servers running(Bungeecord) in the same local network, we need to differentiate between them
     * so each AutoPlug-Plugin connects to the right AutoPlug-Client.
     * To do this, we iterate through all ports starting at 35565, and wait for a server-key that we can compare with ours.
     * If there is no server key in response or that key doesn't match, we move on.
     * Max port is 65535.
     */
    private void findAutoPlugClient() {

        LOG.info("Searching AutoPlug-Client on local network...");
        int i = 35565;
        boolean match = false;
        for (int j = 1; j <= 1000; j++) {
            try {
                socket = new Socket("localhost", i);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                LOG.info("Connected to AutoPlug-Client at port " + i + " and comparing keys...");

                // Send server key to AutoPlug-Client on that port and let it be compared
                if (dis.readUTF().equals(serverKey)) {
                    LOG.info("Found AutoPlug-Client.");
                    dos.writeUTF(serverKey);
                    socket.setSoTimeout(0);
                    bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    match = true;
                    break;
                } else {
                    i++;
                    socket.close();
                }
            } catch (Exception ignored) {
            }
        }

        if (!match)
            throw new RuntimeException("Checked ports 35565-" + i + " for the matching AutoPlug-Client, but failed to find it." +
                    " This either means that AutoPlug-Client is not running, or you did something wrong during the installation," +
                    " or your installation of AutoPlug-Client or AutoPlug-Plugin is outdated, or you have more that 1000 servers running on this network (not very likely).");
    }

    public Socket getSocket() {
        return socket;
    }

}
