package com.dmteam.analyzer.impl;

import com.dmteam.analyzer.Analyzer;
import com.dmteam.analyzer.AnalyzerConfig;
import com.google.common.collect.Lists;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

/**
 * Created by Administrator on 2014/12/10.
 */
public class AnsjSegAnalyzer extends AbstracAnalyzer {

    public enum ATYPE {
        BASE, TO, NLP
    }

    public static final String TYPE = "TYPE";

    public AnsjSegAnalyzer() {
        super.config = new AnalyzerConfig().addExtConfig(AnsjSegAnalyzer.TYPE, AnsjSegAnalyzer.ATYPE.NLP);
    }


    @Override
    public List<Word> segmentWords(String content) {

        List<Term> r = doParse(content);

        if (r == null) return null;

        return trans(r);
    }

    @Override
    public Analyzer setConfig(AnalyzerConfig config) {
        this.config = config;
        return this;
    }

    private List<Term> doParse(String content) {

        List<Term> r = null;

        ATYPE type;
        if (null == getExtInfo(TYPE)) {
            type = ATYPE.BASE;
        } else {
            type = (ATYPE)getExtInfo(TYPE);
        }

        switch (type) {

            case BASE: r = BaseAnalysis.parse(content); break;
            case TO: r = ToAnalysis.parse(content); break;
            case NLP: r = NlpAnalysis.parse(content); break;
        }

        return r;
    }

    private List<Word> trans(List<Term> l) {

        List<Word> r = Lists.newArrayListWithCapacity(l.size());

        for (Term t : l) {

            Word w = new Word(t.getRealName());
            if (isIgnored(w)) continue;

            // TODO
            w.content = t.getRealName();
            w.weight = t.getOffe();

            r.add(w);
        }

        return r;
    }

    private Object getExtInfo(String key) {
        if (config == null) return null;
        return config.getExtInfo(key);
    }
}
