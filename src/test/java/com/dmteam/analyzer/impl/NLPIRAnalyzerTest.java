package com.dmteam.analyzer.impl;

import com.dmteam.analyzer.Analyzer;
import com.dmteam.analyzer.AnalyzerConfig;
import com.dmteam.analyzer.Analyzers;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class NLPIRAnalyzerTest {

    @Test
    public void wordSegment() throws IOException {

        AnalyzerConfig config = new AnalyzerConfig().addExtConfig("bPOSTagged", "true");

        Analyzer a = Analyzers.create(Analyzers.T.NLPIR).setConfig(config);


        List<Word> words = a.segmentWords(IOUtils.toString(Resources.getResource("word_seg_test.txt")));

        for (Word w : words) {
            System.out.println(w.content + " - " + w.POS);
        }
    }

}