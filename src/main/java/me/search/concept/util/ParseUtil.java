package me.search.concept.util;

import me.search.concept.model.BaseConcept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ParseUtil {

    /**
     * 解析原生的相应文本，除去第一行表头，每行三列，每列用,隔开
     * @param rawResponseText 原生文本
     * @return 解析后的对象
     */
    public static List<BaseConcept> parseBaseConcept(String rawResponseText) {
        List<BaseConcept> concepts = new ArrayList<>();
        String[] strs = rawResponseText.split("\\n");
        if (strs.length <= 0 || strs[0].startsWith("error")) {
            return null;
        }
        for (int i = 1; i < strs.length; i++) {
            String row = strs[i];
            String[] cols = row.split(",");
            BaseConcept concept = new BaseConcept(cols[0], cols[1], cols[2]);
            concepts.add(concept);
        }
        return concepts;
    }

    /**
     * 解析原生的响应报文，格式为每行一列
     * @param rawResponseText 原生文本
     * @return 解析后的对象
     */
    public static List<String> parseStockCode(String rawResponseText) {
        List<String> stockCodes = new ArrayList<>();
        String[] strs = rawResponseText.split("\\n");
        if (strs.length <= 0 || strs[0].startsWith("error")) {
            return null;
        }
        Collections.addAll( stockCodes, strs);
        return stockCodes;
    }

}
