package com.dmteam.system;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xh on 2014/12/15.
 */
public class Pools {

    // 字符串常量池
    public static final Interner<String> stringInterner = Interners.newWeakInterner();

    // 分词线程使用的线程池
    public static final ExecutorService wordSegPool;

    static {
        int cpuNum = Runtime.getRuntime().availableProcessors();

        wordSegPool = new ThreadPoolExecutor(cpuNum,
                cpuNum * 2,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(cpuNum * 4),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }



}
