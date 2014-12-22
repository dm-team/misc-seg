package com.dmteam;

import com.dmteam.system.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xh on 2014/12/18.
 */
public class BgTaskManager implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BgTaskManager.class);

    private static BgTaskManager manger = new BgTaskManager();

    private static final File f = new File(SystemConfig.SYSTEM_HOME_DIR, ".shutdown.watcher");

    private Thread watchThread;

    private List<ControllableBgTask> tasks;

    private List<ExecutorService> managedPools;

    private BgTaskManager() {
        watchThread = Executors.defaultThreadFactory().newThread(this);
        watchThread.setName("ShutdownWatcher");

        tasks = new ArrayList<ControllableBgTask>();
        managedPools = new ArrayList<ExecutorService>();

    }

    public static BgTaskManager manger () {
        return BgTaskManager.manger;
    }

    public void startTasks() {
        clearWatchFile();

        logger.info("watch on file ["  + f.getAbsolutePath() + "].");

        fireOnStart();
        watchThread.start();
    }

    @Override
    public void run() {

        while (true) {

            sleep(1000);

            if (whetherQuit()) {

                logger.info("Shutdown info file [" + f.getAbsolutePath() + "] found, quiting...");
                break;
            }
        }

        fireOnStop();
        clearWatchFile();
    }

    public BgTaskManager regist(ControllableBgTask task) {
        tasks.add(task);
        return this;
    }

    public BgTaskManager regist(ExecutorService task) {
        managedPools.add(task);
        return this;
    }

    private void fireOnStart() {
        for (ControllableBgTask t : tasks) {
            t.start();
        }
    }

    private void fireOnStop() {
        for (ControllableBgTask t : tasks) {
            t.stop();
        }

        for (ExecutorService e : managedPools) {
            e.shutdown();
        }
    }

    private void clearWatchFile() {
        f.delete();
    }

    private boolean whetherQuit() {
        return f.exists();
    }

    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
        }
    }
}
