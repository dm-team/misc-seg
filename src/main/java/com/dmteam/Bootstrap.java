package com.dmteam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by xh on 2014/12/17.
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws IOException {

        logger.debug("i'm debug");
        logger.trace("i'm trace");
        logger.info("i'm info");
        logger.warn("i'm warn");
        logger.error("i'm error");




//        AnalyzerConfig config = new AnalyzerConfig().addExtConfig(AnsjSegAnalyzer.TYPE, AnsjSegAnalyzer.ATYPE.NLP);
//        Analyzer a = Analyzers.create(Analyzers.T.ANSJ_SEG).setConfig(config);
//
//        ArticleProvider provider = new FileArticleProvider("D:\\___folder\\NetEase");
//        provider.start();
//
//        final ArticleSegProcessor proc = new ArticleSegProcessor(a, provider);
//
//        new Thread(proc).start();
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (true) {
//
//                    System.out.println("let's go print count...");
//
//                    int i = 0;
//                    try {
//                        Thread.sleep(5000l);
//                    } catch (InterruptedException e) {
//                    }
//
//                    try {
//                        WordCounter wordCounter = proc.getWordCounter();
//                        List<Multiset.Entry<Word>> entries = wordCounter.topN(-1);
//
//                        FileWriter fileWriter = new FileWriter("D://out" + i++);
////
//                        for (Multiset.Entry<Word> entry : entries) {
//
//                            fileWriter.write(entry.getElement().content + " " + entry.getCount() + "\n");
//
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }).start();
    }
}
