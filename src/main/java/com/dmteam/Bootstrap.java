package com.dmteam;

import com.dmteam.analyzer.Analyzer;
import com.dmteam.analyzer.Analyzers;
import com.dmteam.analyzer.impl.Word;
import com.dmteam.system.Pools;
import com.dmteam.system.SystemConfig;
import com.dmteam.wordCount.ArticleProvider;
import com.dmteam.wordCount.ArticleSegProcessor;
import com.dmteam.wordCount.FileArticleProvider;
import com.dmteam.wordCount.WordCounter;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Multiset;
import com.google.common.io.Resources;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by xh on 2014/12/17.
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    static {
        // init log4j
        System.setProperty("log4j.dir", new File(SystemConfig.SYSTEM_HOME_DIR, "logs").getAbsolutePath());
        PropertyConfigurator.configure(new File(SystemConfig.CONFIG_DIR, "log4j.properties").getAbsolutePath());
    }

    public static void main(String[] args) throws IOException {

        logger.info("##########################################################");
        logger.info("##                  【 MISC-SEG system 】               ##");
        logger.info("##                                                      ##");
        logger.info("##                     author: xh                       ##");
        logger.info("##         first edition date: 2014-12-18               ##");
        logger.info("##########################################################");

        Bootstrap bs = new Bootstrap();
        bs.start();
    }


    public void start() {


        ArticleProvider provider = new FileArticleProvider("D:\\___folder\\NetEase");

        ArticleSegProcessor proc = new ArticleSegProcessor(createAnalyzer(), provider);

        BgTaskManager manger = BgTaskManager.manger();

        manger.regist((ControllableBgTask) provider).regist(proc);

        manger.regist(Pools.wordSegPool);

        manger.startTasks();



//        forTest(proc);
    }

    void forTest(ArticleSegProcessor proc) {
        final ArticleSegProcessor pp = proc;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    System.out.println("let's go print count...");

                    int i = 0;
                    try {
                        Thread.sleep(10000l);
                    } catch (InterruptedException e) {
                    }

                    try {
                        WordCounter wordCounter = pp.getWordCounter();
                        List<Multiset.Entry<Word>> entries = wordCounter.topN(-1);

                        FileWriter fileWriter = new FileWriter("D://out" + i++);
                        for (Multiset.Entry<Word> entry : entries) {

                            fileWriter.write(entry.getElement().content + " " + entry.getCount() + "\n");

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private Analyzer createAnalyzer() {
        return Analyzers.create(Analyzers.T.valueOf(SystemConfig.wordSegType()));
    }


}
