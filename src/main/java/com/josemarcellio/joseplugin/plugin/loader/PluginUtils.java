package com.josemarcellio.joseplugin.plugin.loader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.Locale;

public class PluginUtils {

    private static final String OS_NAME_PROPERTY = "os.name";

    public static URI formURL(String urlString) throws MalformedURLException {
        try {
            return new URI(urlString);
        } catch (Exception e) {
            throw new MalformedURLException("Invalid URL: " + urlString);
        }
    }

    public static long download(InputStream inputStream, Path path) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
            ReadableByteChannel byteChannel = Channels.newChannel(inputStream);
            return fos.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        }
    }

    public static File getPluginFilePath(String url) {
        String os = System.getProperty(OS_NAME_PROPERTY).toLowerCase(Locale.ROOT);
        String directoryPath;
        if (os.contains("win")) {
            directoryPath = System.getenv("APPDATA") + "/plugins/";
        } else {
            directoryPath = System.getProperty("user.home") + "/plugins/";
        }

        File workDirectory = new File(directoryPath);
        if (!workDirectory.exists() && !workDirectory.mkdirs()) {
            throw new IllegalStateException("Unable to create directory " + workDirectory.getAbsolutePath());
        }

        String fileName = url.substring(url.lastIndexOf('/') + 1);
        return new File(workDirectory, fileName);
    }
}
