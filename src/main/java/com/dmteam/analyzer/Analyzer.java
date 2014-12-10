package com.dmteam.analyzer;

import com.dmteam.analyzer.impl.Word;

import java.util.List;

/**
 * Created by Administrator on 2014/12/10.
 */
public interface Analyzer {

    /**
     * 将 content 内容，分成一组词
     * @param content
     * @return
     */
    List<Word> segmentWords(String content);

    /**
     * 设置 Analyzer 的相关配置
     * @param config
     * @return
     */
    Analyzer setConfig(AnalyzerConfig config);
}
