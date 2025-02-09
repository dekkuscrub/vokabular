package com.practice.vokabular.service.svenska;

import com.practice.vokabular.model.Word;
import com.practice.vokabular.exception.WordServiceException;
import com.practice.vokabular.util.HtmlParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SvenskaService {
    
    private static final String SVENSKA_BASE_URL = "https://svenska.se/tri/f_saol.php";
    private final RestTemplate restTemplate;
    private final HtmlParser htmlParser;

    public SvenskaService(RestTemplate restTemplate, HtmlParser htmlParser) {
        this.restTemplate = restTemplate;
        this.htmlParser = htmlParser;
    }

    public Word fetchWordDetails(String term) {
        try {
            String url = buildUrl(term);
            String response = restTemplate.getForObject(url, String.class);
            if (response == null || response.isBlank()) {
                throw new WordServiceException("No response received from svenska.se");
            }
            return htmlParser.parseWordFromHtml(response, term);
        } catch (Exception e) {
            throw new WordServiceException("Failed to fetch word details from svenska.se", e);
        }
    }

    private String buildUrl(String term) {
        return UriComponentsBuilder
            .fromUriString(SVENSKA_BASE_URL)
            .queryParam("sok", term)
            .build()
            .toUriString();
    }
} 
