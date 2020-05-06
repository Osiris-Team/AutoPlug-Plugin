/*
 *  Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin.tasks;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class DownloadUpdate {

    private static final Logger LOG = Logger.getLogger("AutoPlug");
    private String final_path;


    public String downloadJar(String download_url, String output_name, String latest_version) throws IOException {


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


        try {

            HtmlPage page = client.getPage(download_url);

            synchronized (page) {
                page.wait(7000);
            }
            //Print cookies for test purposes. Comment out in production.
            URL _url = new URL(download_url);
            for (Cookie c : client.getCookies(_url)) {
                String result = c.getName() + "=" + c.getValue();
            }

            String raw_url = client.getPage(download_url).getUrl().toString();
            //Gives us the string github.com from: https://github.com/
            int startIndex = raw_url.indexOf("https://");
            int endIndex = raw_url.indexOf("/");
            String url = raw_url.substring(startIndex + 1, endIndex);

            LOG.info("TEST RESULT: " + url + " from: " + raw_url);

            if (url.equals("github.com")) {
                client.getOptions().setJavaScriptEnabled(false);
            }

            //This prints the content after bypassing Cloudflare.
            InputStream in = client.getPage(download_url).getWebResponse().getContentAsStream();

            //IF you change the name here, also change it at UpdatePlugins, where you copy this to the main plugins folder
            final_path = System.getProperty("user.dir")+ "\\autoplug-cache" + "\\"+output_name+"["+latest_version+"]-AUTOPLUGGED-LATEST.jar";
            try (FileOutputStream fileOutputStream = new FileOutputStream(final_path)) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            }
            LOG.info("Download success! -> " + final_path);
            return final_path;

        } catch (FailingHttpStatusCodeException e) {
            LOG.severe(" - Download-link error. This means, that the author uses an external site (GitHub) to host his files [!]");
            LOG.severe(" - Currently this is unsupported, please go and download the file yourself [!] ");
            LOG.severe(" - Link: " + download_url + " [!]");
            e.printStackTrace();
            return final_path = "failed";
        } catch (MalformedURLException e) {
            LOG.severe(" - Download-link error. This means, that the author uses an external site (GitHub) to host his files [!]");
            LOG.severe(" - Currently this is unsupported, please go and download the file yourself [!] ");
            LOG.severe(" - Link: " + download_url + " [!]");
            e.printStackTrace();
            return final_path = "failed";
        } catch (IOException e) {
            LOG.severe(" - Download-link error. This means, that the author uses an external site (GitHub) to host his files [!]");
            LOG.severe(" - Currently this is unsupported, please go and download the file yourself [!] ");
            LOG.severe(" - Link: " + download_url + " [!]");
            e.printStackTrace();
            return final_path = "failed";
        } catch (InterruptedException e) {
            LOG.severe(" - Download-link error. This means, that the author uses an external site (GitHub) to host his files [!]");
            LOG.severe(" - Currently this is unsupported, please go and download the file yourself [!] ");
            LOG.severe(" - Link: " + download_url + " [!]");
            e.printStackTrace();
            return final_path = "failed";
        }
    }
}
