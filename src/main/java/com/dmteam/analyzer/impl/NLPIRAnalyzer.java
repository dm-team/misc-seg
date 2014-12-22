package com.dmteam.analyzer.impl;

import com.dmteam.analyzer.Analyzer;
import com.dmteam.analyzer.AnalyzerConfig;
import com.google.common.collect.Lists;
import com.lingjoin.divideWords.NlpirLibrary;
import com.lingjoin.divideWords.NlpirMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Administrator on 2014/12/10.
 */
public class NLPIRAnalyzer extends AbstracAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(NLPIRAnalyzer.class);

    private static boolean NLPIR_INIT = false;

    public NLPIRAnalyzer() {
        super.config = new AnalyzerConfig().addExtConfig(AnsjSegAnalyzer.TYPE, AnsjSegAnalyzer.ATYPE.NLP);
    }

    @Override
    public List<Word> segmentWords(String content) {

        innerInit();

        String participleResult= NlpirLibrary.CLibraryNlpir.Instance.NLPIR_ParagraphProcess(content, needPOSTagged());

        logger.debug("NLPIRAnalyzer participleResult: {}", participleResult);

        return participleResultToTerms(participleResult);
    }

    @Override
    public Analyzer setConfig(AnalyzerConfig config) {
        this.config = config;
        return this;
    }

    private static synchronized void innerInit() {
        if (NLPIR_INIT) {
            return;
        }

        if (!NlpirMethod.Nlpir_init()) {
            throw new IllegalStateException("NLPIR init fail.");
        }

        NLPIR_INIT = true;
    }

    private int needPOSTagged() {
        if (config == null) return 0;

        return "true".equals(config.getExtInfo("bPOSTagged")) ? 1 : 0;
    }

    private List<Word> participleResultToTerms(String participleResult) {

        String[] tokens = StringUtils.splitPreserveAllTokens(participleResult, null, 0);

        List<Word> words = Lists.newArrayListWithCapacity(tokens.length);

        for (String t : tokens) {

            String[] parts = StringUtils.split(t, "/");

            if (parts == null || parts.length < 2) continue;

            Word w = new Word(parts[0]);

            if (isIgnored(w)) continue;


            // TODO add other info
            w.POS = parts[1];

            words.add(w);
        }
        return words;
    }
}
