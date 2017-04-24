package com.company.loggers;

import com.company.Main;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

// FIXME: 23.04.2017 threadsafe
public class CompressedLogger extends TextLogger implements Closeable {
    private final File dstZipFile;
    private final BlockingQueue<String> stringBlockingQueue;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.M.dd_HH.mm.ss.SSS");
    private ZipOutputStream zipOutputStream; // FIXME: 23.04.2017 threadsafe

/*
	public CompressedLogger(File file) {
		super(file);

		this.dstZipFile = file;

		try {
			setUp();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}*/

    public CompressedLogger(String path) {
        super();
        stringBlockingQueue = this.getStringBlockingQueue();
        this.dstZipFile = new File(path);

        try {
            setUp();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setUp() throws FileNotFoundException {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                File oldFile = null; //todo delete on sinifh

                if (dstZipFile.exists()) {
                    try {
                        dstZipFile.createNewFile();
                        oldFile = new File(dstZipFile.getAbsolutePath());
                        oldFile.renameTo(new File(oldFile.getAbsoluteFile() + "_old"));
                        oldFile = new File(oldFile.getAbsolutePath() + "_old");
                        oldFile.deleteOnExit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    zipOutputStream = new ZipOutputStream(new FileOutputStream(dstZipFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (oldFile != null) {
                    try {

                        ZipFile zipFile = new ZipFile(oldFile, Charset.forName("Cp437"));
                        for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
                            ZipEntry zipEntry = new ZipEntry(e.nextElement().getName());
                            InputStream inputStream = zipFile.getInputStream(zipEntry);

                            zipOutputStream.putNextEntry(zipEntry);
                            byte[] buff = new byte[8192];
                            int i;
                            while ((i = inputStream.read(buff, 0, 8192)) > 0) {
                                zipOutputStream.write(buff, 0, i);
                            }
                            zipOutputStream.closeEntry();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                ZipEntry zipEntry = new ZipEntry(String.valueOf(simpleDateFormat.format(Main.LAUNCH_DATE)));
                try {
                    zipOutputStream.putNextEntry(zipEntry);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    String tmp;
                    while ((tmp = stringBlockingQueue.take()) != null) {
                        zipOutputStream.write(tmp.getBytes());
                        zipOutputStream.flush();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        zipOutputStream.closeEntry();
                        zipOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();

    }

    @Override
    public void close() throws IOException {
        zipOutputStream.closeEntry();
        zipOutputStream.close();
    }
}

