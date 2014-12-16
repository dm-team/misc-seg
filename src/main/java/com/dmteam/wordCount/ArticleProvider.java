package com.dmteam.wordCount;

import org.apache.commons.lang3.tuple.Pair;


/**
 * 提供一组文本，用来分词
 * Created by xh on 2014/12/12.
 */
public interface ArticleProvider {

    /**
     * 阻塞的方式提供文本内容
     * pair: 文件名 -> 文件内容
     * @return 如果返回空字符串，表示通知调用方结束
     */
    Pair<String, String> provide();

    /**
     * 开始准备数据
     */
    void start();

    /**
     * 通知provider，对该文件的处理已经完成
     * @param doneFile
     */
    void noticeDone(String doneFile);
}
