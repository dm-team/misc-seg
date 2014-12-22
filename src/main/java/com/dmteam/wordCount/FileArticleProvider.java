package com.dmteam.wordCount;

import com.dmteam.ControllableBgTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

/**
 * Created by xh on 2014/12/12.
 */
public class FileArticleProvider extends ControllableBgTask implements ArticleProvider {

    private static final Logger logger = LoggerFactory.getLogger(FileArticleProvider.class);

    private static final int DEFAULT_BUFFER_SIZE = 128;

    private ArrayBlockingQueue<Pair<String, String>> buffer;

    private String dirPath;

    private ExecutorService readFilePool;

    private boolean stop;

    @Override
    public void stop() {
        stop = true;
        readFilePool.shutdown();
        super.interrupt();
    }

    public FileArticleProvider(String dirPath) {
        super(FileArticleProvider.class.getName());
        this.dirPath = dirPath;
        this.buffer = new ArrayBlockingQueue<Pair<String, String>>(DEFAULT_BUFFER_SIZE);

        // 最多两个线程同时读磁盘
        this.readFilePool = Executors.newFixedThreadPool(2);
    }

    @Override
    public Pair<String, String> provide() {
        try {
            return buffer.take();
        } catch (InterruptedException e) {
            // if interrupted, notice the caller to stop with "null"
            return null;
        }
    }

    @Override
    public void noticeDone(String doneFile) {

        File f = new File(dirPath, doneFile+".buf");
        Path source = Paths.get(f.toURI());
        try {
            Files.move(source, source.resolveSibling(f.getName() + ".done"));
        } catch (IOException e) {
            logger.warn("file rename fail, {}", e);
        }
    }

    @Override
    public void run() {

        while (!stop && !Thread.interrupted()) {

            if (buffer.remainingCapacity() == 0) {
                sleep(3000);
                continue;
            }

            final int needload = buffer.remainingCapacity();

            File dir = new File(dirPath);

            File[] files = dir.listFiles(new FilenameFilter() {

                int n = needload;

                @Override
                public boolean accept(File dir, String name) {
                    if (n <= 0) return false;

                    if (!name.endsWith(".ctt")) return false;

                    n--;
                    return true;
                }
            });

            if (files.length == 0) {
                sleep(3000);
                continue;
            }

            parallelNIORead(files);

        }

    }


    private void parallelNIORead(File[] files) {

        final CountDownLatch countDownLatch = new CountDownLatch(files.length);

        for (File f : files) {
            final File file = f;
            readFilePool.submit(new Runnable() {
                @Override
                public void run() {

                    try {
                        String c;
                        try {
                            c = com.dmteam.utils.FileUtils.NIOReadFile(file);
                        } catch (IOException ioe) {
                            logger.warn("nio read file exception, {}", ioe);
                            return;
                        }

                        if (StringUtils.isBlank(c)) return;


                        // rename
                        Path source = Paths.get(file.toURI());
                        try {
                            Files.move(source, source.resolveSibling(file.getName() + ".buf"));
                        } catch (IOException e) {
                            logger.warn("file rename fail, {}", e);
                        }


                        try {
                            buffer.put(new ImmutablePair<String, String>(file.getName(), c));
                        } catch (InterruptedException e) {
                        }

                    } finally {
                        countDownLatch.countDown();
                    }

                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
        }
    }

    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            stop = true;
        }
    }
}
