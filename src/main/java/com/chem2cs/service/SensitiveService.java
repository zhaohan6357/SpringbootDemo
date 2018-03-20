package com.chem2cs.service;

import com.chem2cs.controller.IndexController;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {
    private static  final Logger LOGGER=  LoggerFactory.getLogger(IndexController.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            InputStream is=Thread.currentThread().getContextClassLoader().
                    getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader=new InputStreamReader(is);
            BufferedReader bufferedReader=new BufferedReader(reader);
            String lineWord;
            while((lineWord=bufferedReader.readLine())!=null){
                addWord(lineWord.trim());
            }
            reader.close();
        }catch (Exception e){
            LOGGER.error("读取敏感词失败"+e.getMessage());
        }
    }
    private void addWord(String word){
        TrieNode node=rootNode;
        for(int i=0;i<word.length();i++){
            Character c=word.charAt(i);
            if(isSymbol(c)){
                continue;
            }
            TrieNode temp=node.getSubNode(c);
            if(temp==null){
                temp=new TrieNode();
                node.addSubNode(c,temp);
            }
            node=temp;
            if(i==word.length()-1)
                node.setKeyWordEnd(true);
        }
    }
    private class TrieNode{
        private boolean end=false;
        private Map<Character,TrieNode> subNode=new HashMap<>();
        public void addSubNode(Character key,TrieNode node){
            subNode.put(key,node);
        }
        public TrieNode getSubNode(Character key){
            return subNode.get(key);
        }
        boolean isKeyWordEnd(){
            return end;
        }
        void setKeyWordEnd(boolean end){
            this.end=end;
        }
    }

    private TrieNode rootNode=new TrieNode();
    private boolean isSymbol(char c){
        int ic=(int)c;
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic<0x2E80||ic>0x9FFF);
    }
    public String filter(String text){
        if(StringUtils.isBlank(text))
            return text;
        int pos=0;
        int begin=0;
        TrieNode tempNode=rootNode;
        StringBuilder sb=new StringBuilder();
        while(pos<text.length()){
            Character c=text.charAt(pos);
            if(isSymbol(c)){
                if(tempNode==rootNode){
                    sb.append(c);
                    begin++;
                }
                pos++;
                continue;
            }
            tempNode=tempNode.getSubNode(c);
            String replace="***";
            if(tempNode==null){
                sb.append(text.charAt(begin));
                pos=begin+1;
                begin=pos;
                tempNode=rootNode;
            }else if(tempNode.isKeyWordEnd()){
                sb.append(replace);
                pos++;
                begin=pos;
                tempNode=rootNode;
            }else{
                pos++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    public static void main(String[] args) {
        SensitiveService sensitiveService=new SensitiveService();
        sensitiveService.addWord("色情");
        System.out.println(sensitiveService.filter("   你好^_^色^_^情 "));
    }



}
