package com.dmteam.wordCount;

import com.dmteam.analyzer.impl.Word;

import java.io.IOException;
import java.util.*;


/**
 * 利用HashMap词频统计
 * Created by yak on 2014/12/10.
 */
public class WordCount {
    private HashMap<String ,Integer> wordMap = new HashMap<String, Integer>();
    public void count(List<Word> words) throws IOException{

        for(Word w: words){
           if(!wordMap.containsKey(w.content))
                wordMap.put(w.content,1);
           else{
               int val = wordMap.get(w.content);
               wordMap.put(w.content,val+1);
            }
        }
    }

    public void display(){
        List<Map.Entry<String,Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(wordMap.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });
        for(int i = 0;i < infoIds.size(); i++){
            String ent=infoIds.get(i).toString();
            System.out.println(ent);
        }
    }


}
