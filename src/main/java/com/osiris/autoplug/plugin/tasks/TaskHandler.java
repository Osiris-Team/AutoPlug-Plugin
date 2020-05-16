/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin.tasks;

public class TaskHandler {

    /** -> REQUEST HANDLER
     * Handles execution of requests
     */

    private String server_key;
    private String request;
    private boolean async;


    public TaskHandler(String server_key, String request, boolean async) {
        this.server_key = server_key;
        this.request = request;
        this.async = async;
    }

    public Boolean execute() {

        if (async) {
            //Executes the requests in async mode
            new Thread(() -> {
                request(request);
            }).start();
            return true;

        } else {
            //Executes the requests normally
            return request(request);
        }

    }

    private boolean request(String request) {
        try {
            switch (request) {
                case "exec_update":

                    UpdatePlugins updatePlugins = new UpdatePlugins(server_key);
                    return updatePlugins.start();

                case "another_task":

                        /*
                        ThreadExecUpdate threadExecUpdate = new ThreadExecUpdate(socket, dis, dos);
                        FutureTask<String> futureTask = new FutureTask<>(threadExecUpdate, "Startup-Plugin-Update complete!");
                        ExecutorService executor = Executors.newFixedThreadPool(1);
                        executor.submit(futureTask);
                        autoPlugLogger.listener_info("Thread created: " + executor.toString(), 35555);
                        */

                    return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
