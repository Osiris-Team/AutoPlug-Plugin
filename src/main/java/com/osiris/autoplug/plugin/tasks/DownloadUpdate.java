/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin.tasks;




import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.logging.Logger;

public class DownloadUpdate {

    private static final Logger LOG = Logger.getLogger("AutoPlug");
    private String final_path;


    public String downloadJar(String download_url, String output_name, String latest_version) {


        WebClient client = new WebClient(BrowserVersion.CHROME);

        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setRedirectEnabled(true);
        client.getCache().setMaxSize(0);
        client.waitForBackgroundJavaScript(10000);
        client.setJavaScriptTimeout(10000);
        client.waitForBackgroundJavaScriptStartingBefore(10000);

        //Prevents the whole html being printed to the console (We always get code 503 when dealing with cloudflare)
        client.getOptions().setPrintContentOnFailingStatusCode(false);
        //This will always happen if its another website we get as result
        client.getOptions().setThrowExceptionOnScriptError(false);


        try {

            HtmlPage page = client.getPage(download_url);

            synchronized (page) {
                page.wait(7000);
            }

            //Get response
            //InputStream in = client.getPage(download_url).getWebResponse().getContentAsStream();
            WebResponse response = client.getPage(download_url).getWebResponse();

            String content_type = response.getContentType();
            //Check if response is application(application/octet-stream) or html(text/html)
            //This is 100% a jar file
            if (content_type.equals("application/octet-stream")) {

                InputStream in = response.getContentAsStream();
                //IF you change the name here, also change it at UpdatePlugins, where you copy this to the main plugins folder
                //todo fix file path
                final_path = System.getProperty("user.dir")+ "/autoplug-cache" + "/"+output_name+"["+latest_version+"]-AUTOPLUGGED-LATEST.jar";
                try (FileOutputStream fileOutputStream = new FileOutputStream(final_path)) {
                    byte dataBuffer[] = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }
                }catch (IOException e){e.printStackTrace();}

                //LOG.info(" - Download success! -> " + final_path);
                return final_path;
            }
            else {
                LOG.warning(" [!] Couldn't determine if response is a website or file [!]");
                LOG.warning(" [!] Check it for yourself: " + download_url + " [!]");

                return final_path = "failed";
            }



        } catch (FailingHttpStatusCodeException e) {
            LOG.severe(" [!] Download-link error: FailingHttpStatusCodeException");
            LOG.severe(" [!] Please go and download the file yourself [!] ");
            LOG.severe(" [!] Link: " + download_url + " [!]");
            e.printStackTrace();
            return final_path = "failed";
        } catch (MalformedURLException e) {
            LOG.severe(" [!] Download-link error: MalformedURLException [!]");
            LOG.severe(" [!] Currently this is unsupported, please go and download the file yourself [!] ");
            LOG.severe(" [!] Link: " + download_url + " [!]");
            e.printStackTrace();
            return final_path = "failed";
        } catch (IOException e) {
            LOG.severe(" [!] Download-link error: IOException [!]");
            LOG.severe(" [!] Please go and download the file yourself [!] ");
            LOG.severe(" [!] Link: " + download_url + " [!]");
            e.printStackTrace();
            return final_path = "failed";
        } catch (InterruptedException e) {
            LOG.severe(" [!] Download-link error: InterruptedException [!]");
            LOG.severe(" [!] Please go and download the file yourself [!] ");
            LOG.severe(" [!] Link: " + download_url + " [!]");
            e.printStackTrace();
            return final_path = "failed";
        }
    }
}
