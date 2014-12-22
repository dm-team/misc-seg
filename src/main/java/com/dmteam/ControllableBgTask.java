package com.dmteam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

/**
 * 后台任务类
 * Created by xh on 2014/12/17.
 */
public abstract class ControllableBgTask implements Runnable, Controllable {

    private static final Logger logger = LoggerFactory.getLogger(ControllableBgTask.class);

    private String taskName;

    private Thread taskThread;

    public void start() {
        taskThread.start();
    }

    public abstract void stop();

    protected void interrupt() {
        taskThread.interrupt();
    }

    protected ControllableBgTask(final String taskName) {
        this.taskName = taskName;
        this.taskThread = Executors.defaultThreadFactory().newThread(new Runnable() {
            @Override
            public void run() {

                logger.warn("task[" + taskName+"] start.");

                ControllableBgTask.this.run();

                logger.warn("task[" + taskName+"] safely stopped.");
            }
        });
        taskThread.setName(this.taskName);
    }

    public String getTaskName() {
        return this.taskName;
    }

}
