package com.dmteam.wordCount;

import com.dmteam.system.Pools;
import com.dmteam.analyzer.impl.Word;
import com.google.common.collect.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * HashMap 方式实现，如果内存不够可以改为trie树
 *
 * 统计单词出现的次数
 * Created by xh on 2014/12/11.
 */
public class WordCounter {

    private Multiset<Word> wordsMultiset = ConcurrentHashMultiset.create();

    private AtomicLong totalWords = new AtomicLong(0l);

    public WordCounter () {


    }

    public void add(List<Word> words) {
        if (CollectionUtils.isEmpty(words)) return;

        for (Word w : words) {
            w.content = Pools.stringInterner.intern(w.content);
            wordsMultiset.add(w);
        }
        totalWords.addAndGet(words.size());
    }

    public void add(Word word) {
        if (word == null) return;

        wordsMultiset.add(word);
        totalWords.addAndGet(1);
    }

    public int count(Word w) {
        return wordsMultiset.count(w);
    }

    /**
     * 出现次数最多的n个词
     * //TODO 可以用优先级队列提高性能
     * @param n 如果n<=0，则返回所有
     * @return
     */
    public List<Multiset.Entry<Word>> topN(int n) {


//        PriorityQueue<Multiset.Entry<String>> minQ = new PriorityQueue<Multiset.Entry<String>>(n+10,
//                new Comparator<Multiset.Entry<String>>() {
//            @Override
//            public int compare(Multiset.Entry<String> o1, Multiset.Entry<String> o2) {
//                return o1.getCount() - o2.getCount();
//            }
//        });
//
//        Set<Multiset.Entry<Word>> entries = wordsMultiset.entrySet();

        Set<Multiset.Entry<Word>> entries = wordsMultiset.entrySet();
        List<Multiset.Entry<Word>> sorted = Lists.newArrayList(entries);
        Collections.sort(sorted, new Comparator<Multiset.Entry<Word>>() {
            @Override
            public int compare(Multiset.Entry<Word> o1, Multiset.Entry<Word> o2) {
                return o2.getCount() - o1.getCount();
            }
        });

        if (n <= 0 || n > sorted.size()) return sorted;
        return sorted.subList(0, n);
    }
}
