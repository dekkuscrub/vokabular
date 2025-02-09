package com.practice.vokabular.util;

import com.practice.vokabular.model.Word;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class HtmlParser {
    // Will contain utility methods for parsing svenska.se responses
    
    public Word parseWordFromHtml(String html, String searchTerm) {
        Document doc = Jsoup.parse(html);
        Word word = new Word();
        word.setTerm(searchTerm);
        
        // Extract base word form
        Element baseForm = doc.select("span.grundform").first();
        if (baseForm != null) {
            word.setTerm(baseForm.text().replaceAll("·­", "")); // Remove syllable markers
        }
        
        // Extract word type
        Element wordType = doc.select("a.ordklass").first();
        if (wordType != null) {
            word.setWordType(wordType.text());
        }
        
        // Extract definition
        Element definition = doc.select("span.def").first();
        if (definition != null) {
            word.setDefinition(definition.text());
        }
        
        // Extract example sentence if available
        Element example = doc.select("span.exempel").first();
        if (example != null) {
            word.setExampleSentence(example.text());
        }
        
        return word;
    }
} 
