package com.dmteam.analyzer;

import com.dmteam.analyzer.impl.AnsjSegAnalyzer;
import com.dmteam.analyzer.impl.NLPIRAnalyzer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2014/12/10.
 */
public class Analyzers {

    public enum T {
        NLPIR,
        ANSJ_SEG
    }

    public static Analyzer create(T t) {

        Analyzer a = null;

        switch (t) {

            case NLPIR:
                a = new NLPIRAnalyzer();
                break;

            case ANSJ_SEG:
                a = new AnsjSegAnalyzer();
                break;
        }

        return a;
    }


    public static List<T> types() {
        return Arrays.asList(T.values()) ;
    }

}
