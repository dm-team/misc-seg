package com.dmteam.analyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/10.
 */
public class AnalyzerConfig {

    private Map<String, Object> extConfigInfo = null;

    public AnalyzerConfig() {}

    public AnalyzerConfig addExtConfig(String k, Object v) {

        if (extConfigInfo == null) {
            extConfigInfo = new HashMap<String, Object>();
        }

        this.extConfigInfo.put(k ,v);
        return this;
    }

    public Object getExtInfo(String k) {
        if (extConfigInfo == null) return null;

        return extConfigInfo.get(k);
    }
}
