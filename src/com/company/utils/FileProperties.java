package com.company.utils;

import net.jcip.annotations.ThreadSafe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@ThreadSafe //Properties is threadSafe
public class FileProperties extends Properties {
    private final File file;
    public FileProperties(String path) throws IOException {
        this.file = new File(path);

        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        this.load(bufferedInputStream);
    }

    public synchronized boolean saveToFile() {
        try {
            this.store(new BufferedOutputStream(new FileOutputStream(file)), null);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
