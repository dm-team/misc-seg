package com.dmteam.wordCount;

import com.dmteam.ControllableBgTask;
import com.dmteam.system.Pools;
import com.dmteam.analyzer.Analyzer;
import com.dmteam.analyzer.impl.Word;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * 文本分词统计处理线程
 * Created by xh on 2014/12/15.
 */
public class ArticleSegProcessor extends ControllableBgTask {

    private final Analyzer a;

    private final WordCounter wc;

    private boolean stop;

    private ArticleProvider articleProvider;

    public ArticleSegProcessor(Analyzer a, ArticleProvider articleProvider) {
        super(ArticleSegProcessor.class.getName());
        this.a = a;
        this.wc = new WordCounter();
        this.articleProvider = articleProvider;
    }

    @Override
    public void run() {

        stop = false;

        while (!stop && !Thread.interrupted()) {

            Pair<String, String> pair = articleProvider.provide();

            // if null provided, stop.
            if (pair == null) {
                stop();
                continue;
            }

            process(pair);
        }

    }

    @Override
    public void stop() {
        stop = true;
        super.interrupt();
    }

    /**
     * 每篇文档单独提交到一个线程中做分词
     * @param pair
     */
    public void process(Pair<String, String> pair) {

        Pools.wordSegPool.submit(new SegWorker(pair));
    }

    public WordCounter getWordCounter() {
        return this.wc;
    }

    private class SegWorker implements Runnable {

        private Pair<String, String> p;

        public SegWorker(Pair<String, String> p) {
            this.p = p;
        }

        @Override
        public void run() {
            List<Word> words = a.segmentWords(p.getValue());
            wc.add(words);

            articleProvider.noticeDone(p.getKey());
        }
    }
}
