package com.example.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author INSLYAB
 * @Date 2022/7/1 9:50
 */
@Component
public class SensitiveFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***";    //敏感词替换词
    private TrieNode rootNode = new TrieNode();  //根节点
    
    @PostConstruct
    public void init(){
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while((keyword = reader.readLine()) != null){
                this.addKeyword(keyword);
            }
        }catch (IOException e){
            logger.error("加载敏感词文件失败!" + e.getMessage());
        }

    }

    //将敏感词添加到前缀树
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        for(int i = 0; i < keyword.length(); i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            tempNode = subNode;
            if(i == keyword.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text  带过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        while(begin < text.length()){
            char c = text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                sb.append(text.charAt(begin));
                tempNode = rootNode;
                begin++;
                position = begin;
            } else if (tempNode.isKeywordEnd){
                sb.append(REPLACEMENT);
                tempNode = rootNode;
                position++;
                begin = position;
            } else {
                if(position < text.length() - 1) {
                    position++;
                }else{
                    sb.append(text.charAt(begin));
                    tempNode = rootNode;
                    begin++;
                    position = begin;
                }
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private boolean isSymbol(Character c){
        //c < 0x2E80 || c > 0x9FFF 东亚文字除外
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }
    
    //前缀树
    private class TrieNode{
        //关键词结束标志
        private Boolean isKeywordEnd = false;
        //子节点 key--下级字符 value--下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public Boolean getKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(Boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
        
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c, node);
        }
        
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
