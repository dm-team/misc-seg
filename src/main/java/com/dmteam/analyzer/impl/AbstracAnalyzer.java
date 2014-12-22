package com.dmteam.analyzer.impl;

import com.dmteam.analyzer.Analyzer;
import com.dmteam.analyzer.AnalyzerConfig;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by xh on 2014/12/11.
 */
public abstract class AbstracAnalyzer implements Analyzer {

    protected AnalyzerConfig config = null;

    @Override
    public Analyzer setConfig(AnalyzerConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public AnalyzerConfig getConfig() {
        return this.config;
    }

    @Override
    public boolean isIgnored(Word w) {

        if (StringUtils.isBlank(w.content)) return true;

        if (w.isPunctuation()) return true;

        return false;
    }
}
