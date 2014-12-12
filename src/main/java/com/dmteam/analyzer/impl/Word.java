package com.dmteam.analyzer.impl;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2014/12/10.
 */
public class Word {

    // 标点符号pattern
    private static final Pattern p = Pattern.compile("\\p{P}");

    public String content; // 词的内容

    public String POS; // 词性

    public boolean isNew; // 是否新词

    public int weight; // 权重

    public Word(String c) {
        this.content = c;
    }

    public boolean isPunctuation() {
        return p.matcher(content).matches();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (!content.equals(word.content)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
