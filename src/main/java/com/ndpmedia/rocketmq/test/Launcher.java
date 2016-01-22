package com.ndpmedia.rocketmq.test;

import com.alibaba.rocketmq.broker.BrokerStartup;
import com.alibaba.rocketmq.namesrv.NamesrvStartup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Launcher {

    static {
        System.setProperty("rocketmq.namesrv.addr", "localhost:9876");
        System.setProperty("enable_ssl", "true");
        System.setProperty("log.home", System.getProperty("user.home"));
    }

    public static void main(String[] args) {
        final String home = System.getProperty("user.home") + File.separator + "rocketmq";
        System.setProperty("rocketmq.home.dir", home);

        checkAndCreateDirs(home);
        deployConfigResources(home);

        NamesrvStartup.main(args);

        String[] params = {"-c", home + File.separator + "conf" + File.separator + "broker.properties"};
        BrokerStartup.main(params);
    }

    private static File checkAndCreateDirs(String path) {
        File file = new File(path);
        if (!file.isDirectory() || !file.exists()) {
            if (!file.mkdirs()) {
                System.exit(1);
            }
        }

        return file;
    }

    private static File deployConfigResources(String home) {
        String confDir = home + File.separator + "conf";
        File confDirFile = checkAndCreateDirs(confDir);
        deployConfig(confDir, "broker.properties");
        deployConfig(confDir, "logback_broker.xml");
        deployConfig(confDir, "logback_namesrv.xml");
        return confDirFile;
    }

    private static void deployConfig(String dstFolder, String config) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            File dstBrokerConfigFile = new File(dstFolder, config);

            if (dstBrokerConfigFile.exists() && dstBrokerConfigFile.isFile()) {
                return;
            }

            bos = new BufferedOutputStream(new FileOutputStream(dstBrokerConfigFile, false));

            InputStream inputStream = Launcher.class.getClassLoader().getResourceAsStream(config);
            bis = new BufferedInputStream(inputStream);

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bos) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    // Ignore
                }
            }

            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    // ignore.
                }
            }
        }
    }
}
