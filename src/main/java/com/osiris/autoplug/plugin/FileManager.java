/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.osiris.autoplug.plugin.Constants.LOG;

/**
 * Search & find files!
 * (Not efficient code. Rework needed!)
 */
public class FileManager {
    @Nullable
    private File queryFile = null;

    public File autoplugJar(File dirToSearch) {
        String searchPattern = "*.jar";
        //Find the file
        findAutoPlugJarFileInDir(searchPattern, dirToSearch);
        //Return the result file
        return queryFile;
    }


    private void findAutoPlugJarFileInDir(String searchPattern, File dirToSearch) {

        try {
            final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + searchPattern);

            Files.walkFileTree(dirToSearch.toPath(), new SimpleFileVisitor<Path>() {

                @NotNull
                @Override
                public FileVisitResult visitFile(@NotNull Path path,
                                                 BasicFileAttributes attrs) throws IOException {
                    if (pathMatcher.matches(path.getFileName())
                            && jarContainsAutoPlugProperties(path.toFile())) {
                        queryFile = new File(path.toString());
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @NotNull
                @Override
                public FileVisitResult preVisitDirectory(@NotNull Path dir, @NotNull BasicFileAttributes attrs) throws IOException {

                    if (!dir.toString().equals(dirToSearch.toString()) && attrs.isDirectory()) {
                        return FileVisitResult.SKIP_SUBTREE;
                    } else {
                        return FileVisitResult.CONTINUE;
                    }
                }

                @NotNull
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                        throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean jarContainsAutoPlugProperties(File jar) {
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry ze = null;
        try {
            fis = new FileInputStream(jar);
            zis = new ZipInputStream(fis);
            ze = zis.getNextEntry();

            while (ze != null) {
                if (ze.getName().equals("autoplug.properties")) {
                    return true;
                }
                // Get next file in zip
                zis.closeEntry();
                ze = zis.getNextEntry();
            } // Loop end
            // Close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (Exception e) {
            LOG.warning("Failed to get information for: " + jar.getName() + " Exception-Message: " + e.getMessage());
        } finally {
            try {
                if (zis != null && ze != null)
                    zis.closeEntry();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (zis != null)
                    zis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fis != null) fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
