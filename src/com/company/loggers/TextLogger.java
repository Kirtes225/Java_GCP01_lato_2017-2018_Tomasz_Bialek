package com.company.loggers;


import com.company.events.CrawlerEventType;
import com.example.Student;
import net.jcip.annotations.ThreadSafe;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@ThreadSafe
public class TextLogger implements Logger, Closeable {
    private final File dstFile;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private final BlockingQueue<String> stringBlockingQueue = new LinkedBlockingQueue<>();
    private BufferedWriter writer; // FIXME: 23.04.2017 threafsafe - final
    private Thread savingThread; /*new Thread(new Runnable() {
		@Override
		public void run() {
			BufferedWriter writer = null;
			String tmp;
			try {
				writer = new BufferedWriter(new FileWriter(dstFile));

				try {
					while ((tmp = stringBlockingQueue.take()) != null) {
						writer.write(tmp);
						writer.flush();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				e.printStackTrace();
				//return;
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	});*/

    public TextLogger(String path) {
        dstFile = new File(path);

        if (!dstFile.exists()) {
            try {
                dstFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        savingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter writer = null;
                String tmp;
                try {
                    writer = new BufferedWriter(new FileWriter(dstFile));

                    try {
                        while ((tmp = stringBlockingQueue.take()) != null) {
                            writer.write(tmp);
                            writer.flush();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    //return;
                } finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        savingThread.start();
    }

    public TextLogger(File file) {
        this.dstFile = file;

        if (!dstFile.exists()) {
            try {
                dstFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        savingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //BufferedWriter writer = null;
                String tmp;
                try {
                    writer = new BufferedWriter(new FileWriter(dstFile));

                    try {
                        while ((tmp = stringBlockingQueue.take()) != null) {
                            writer.write(tmp);
                            writer.flush();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    //return;
                } finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        savingThread.start();
    }

    public TextLogger() {
        this.dstFile = null;
    }

    public BlockingQueue<String> getStringBlockingQueue() {
        return stringBlockingQueue;
    }

    @Override
    public void log(CrawlerEventType crawlerEventType, Student student) {
        try {
            stringBlockingQueue.put(simpleDateFormat.format(new Date()) + "        [" + crawlerEventType + "]          " +
                    student.getFirstName() + "        " + student.getLastName() + "\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }
}