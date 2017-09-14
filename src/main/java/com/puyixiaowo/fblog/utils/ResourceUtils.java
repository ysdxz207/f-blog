package com.puyixiaowo.fblog.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Moses
 * @date 2017-08-04 12:59
 */
public class ResourceUtils {
    private static Properties properties;

    public static InputStream readFile(String path) {
        InputStream inputStream = ResourceUtils.class
        .getClassLoader().getResourceAsStream(path);

        if (inputStream == null) {
            throw new RuntimeException("Can not find file " + path);
        }


        return inputStream;
    }

    public static Properties load(String path) {

        properties = new Properties();
        try {
            properties.load(readFile(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static String get(String key) {
        if (properties == null) {
            throw new RuntimeException("Properties not load yet.");
        }

        return (String) properties.get(key);
    }

    public static URL getResource(String filepath) {
        return ResourceUtils.class.getClassLoader().getResource(filepath);
    }

    public static String[] getResourceFolderFiles (String folder) {
        List<String> filenameList = new ArrayList<>();
        final File jarFile = new File(ResourceUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {  // Run with JAR file
            try (JarFile jar = new JarFile(jarFile)){
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while(entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(folder + "/")) { //filter according to the path
                        filenameList.add(name.replace(folder + "/", ""));
                    }
                }

                return filenameList.toArray(new String[filenameList.size()]);
            } catch (Exception e) {

            }

        } else { // Run with IDE
            final URL url = getResource(folder);
            if (url != null) {
                try {
                    final File apps = new File(url.toURI());
                    return apps.list();
                } catch (URISyntaxException ex) {
                    // never happens
                }
            }
        }
        return new String[0];
    }
}
